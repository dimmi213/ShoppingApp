package com.example.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.adapter.CartAdapter;
import com.example.shopping.model.Cart;
import com.example.shopping.model.EventBus.CalculateTotalEvent;
import com.example.shopping.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    TextView cartempty, totalprice;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnbuy;
    CartAdapter adapter;
    List<Cart> cartList;
    long totalpriceP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        totalpriceP = 0;
        for(int i=0; i<Utils.cartList.size(); i++){
            totalpriceP = totalpriceP + (Utils.cartList.get(i).getPriceProduct() * Utils.cartList.get(i).getAmount());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalprice.setText(decimalFormat.format(totalpriceP));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Utils.cartList.size() == 0){
            cartempty.setVisibility(View.VISIBLE);
        } else {
            adapter = new CartAdapter(getApplicationContext(), Utils.cartList);
            recyclerView.setAdapter(adapter);
        }

        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                intent.putExtra("totalprice", totalpriceP);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        cartempty = findViewById(R.id.txtcartempty);
        totalprice = findViewById(R.id.txttotalprice);
        toolbar = findViewById(R.id.toobar);
        recyclerView = findViewById(R.id.recyclerviewcart);
        btnbuy = findViewById(R.id.btnbbuy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventCalculatePrice(CalculateTotalEvent event){
        if(event != null){
            calculateTotalPrice();
        }
    }
}