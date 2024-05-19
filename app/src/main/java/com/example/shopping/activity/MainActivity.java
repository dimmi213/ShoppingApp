package com.example.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopping.Interface.ItemClickListener;
import com.example.shopping.R;
import com.example.shopping.model.Cart;
import com.example.shopping.model.Product;
import com.example.shopping.utils.Utils;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.nex3z.notificationbadge.NotificationBadge;
import com.example.shopping.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ReadableBottomBar readableBottomBar;
    TextView name, price, description;
    Button btnAdd;
    ImageView img;
    Spinner spinner;
    Toolbar toolbar;
    Product product;
    NotificationBadge badge;
    ItemClickListener itemClickListener;
    List<Product> productList;
    ProductAdapter productAdapter;
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        badge = findViewById(R.id.menu_amount);
        frameLayout = findViewById(R.id.framecart);

        productList= new ArrayList<>();
        if(Utils.cartList == null){
            Utils.cartList = new ArrayList<>();
        } else{
            int totalItem = 0;
            for(int i=0; i< Utils.cartList.size(); i++){
                totalItem = totalItem + Utils.cartList.get(i).getAmount();
            }
            badge.setText(String.valueOf(totalItem));
        }

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        readableBottomBar = findViewById(R.id.readableBottomBar);

        // bắt đầu quá trình thay đổi fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Thay thế nội dung hiện tại bằng HomeFragment
        fragmentTransaction.replace(R.id.content, new HomeFragment());
        // xác nhận thay đổi
        fragmentTransaction.commit();

        readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();

                switch (i){
                    case 0:
                        fragmentTransaction.replace(R.id.content, new HomeFragment());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction.replace(R.id.content, new NotiFragment());
                        fragmentTransaction.commit();
                        break;

                    case 2:
                        fragmentTransaction.replace(R.id.content, new ProfileFragment());
                        fragmentTransaction.commit();
                        break;
                }
            }
        });

        if (isConnected(this)) {
            Toast.makeText(getApplicationContext(), "Connect internet success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Connect internet fail", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    protected void onResume(){
        super.onResume();
        int totalItem = 0;
        for(int i=0; i< Utils.cartList.size(); i++){
            totalItem = totalItem + Utils.cartList.get(i).getAmount();
        }
        badge.setText(String.valueOf(totalItem));
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
        badge.setText(String.valueOf(Utils.cartList.size()));

    }


    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }
        }

        return false;
    }
}