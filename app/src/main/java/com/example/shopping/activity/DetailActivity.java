package com.example.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.shopping.R;
import com.example.shopping.model.Cart;
import com.example.shopping.model.Product;
import com.example.shopping.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {
    TextView name, price, description;
    Button btnAdd;
    ImageView img;
    Spinner spinner;
    Toolbar toolbar;
    Product product;
    NotificationBadge badge;
    private String userId, userName, userPhoneNumber, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        initView();
        ActionToolBar();
        initData();
        initControl();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");
        userPhoneNumber = getIntent().getStringExtra("userPhoneNumber");

    }

    private void initControl() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCart();
            }
        });
    }

    private void addCart() {
        if(Utils.cartList.size() > 0){
            boolean flag = false;
            int amount = Integer.parseInt(spinner.getSelectedItem().toString());
            for( int i = 0; i < Utils.cartList.size(); i++){
                if(Utils.cartList.get(i).getIdP() == product.getId()){
                    Utils.cartList.get(i).setAmount(amount + Utils.cartList.get(i).getAmount());
                    long price = Long.parseLong(product.getPrice()) * Utils.cartList.get(i).getAmount();
                    Utils.cartList.get(i).setPriceProduct(price);
                    flag = true;
                }
            }

            if(!flag){
                long price = Long.parseLong(product.getPrice()) * amount;
                Cart cart = new Cart();
                cart.setPriceProduct(price);
                cart.setAmount(amount);
                cart.setIdP(product.getId());
                cart.setNameProduct(product.getName());
                cart.setImgProduct(product.getImage());
                Utils.cartList.add(cart);

            }

        } else{
            int amount = Integer.parseInt(spinner.getSelectedItem().toString());
            long price = Long.parseLong(product.getPrice()) * amount;
            Cart cart = new Cart();
            cart.setPriceProduct(price);
            cart.setAmount(amount);
            cart.setIdP(product.getId());
            cart.setNameProduct(product.getName());
            cart.setImgProduct(product.getImage());
            Utils.cartList.add(cart);

        }

        int totalItem = 0;
        for(int i=0; i< Utils.cartList.size(); i++){
            totalItem = totalItem + Utils.cartList.get(i).getAmount();
        }
        badge.setText(String.valueOf(totalItem));

    }

    private void initData() {
        product = (Product) getIntent().getSerializableExtra("detail");
        name.setText(product.getName());
        description.setText(product.getDescription());
        Glide.with(getApplicationContext()).load(product.getImage()).into(img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        price.setText("đ" + decimalFormat.format(Double.parseDouble(product.getPrice())));
        Integer[] number = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,number);
        spinner.setAdapter(adapterspin);

    }

    private void initView() {
        name = findViewById(R.id.txtname);
        price = findViewById(R.id.txtprice);
        description = findViewById(R.id.txtdescription);
        btnAdd = findViewById(R.id.btnaddtocart);
        img = findViewById(R.id.imgDetail);
        spinner = findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_amount);
        FrameLayout frameLayoutcart = findViewById(R.id.framecart);
        frameLayoutcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userPhoneNumber", userPhoneNumber);
                startActivity(intent);
            }
        });
        if(Utils.cartList != null){
            int totalItem = 0;
            for(int i=0; i< Utils.cartList.size(); i++){
                totalItem = totalItem + Utils.cartList.get(i).getAmount();
            }

            badge.setText(String.valueOf(totalItem));
        }
    }

    private void ActionToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.cartList != null){
            int totalItem = 0;
            for(int i=0; i< Utils.cartList.size(); i++){
                totalItem = totalItem + Utils.cartList.get(i).getAmount();
            }

            badge.setText(String.valueOf(totalItem));
        }
    }
}