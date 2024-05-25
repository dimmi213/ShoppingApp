package com.example.shopping.activity.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.shopping.R;
import com.example.shopping.retrofit.ApiShopping;
import com.example.shopping.retrofit.RetrofitClient;
import com.example.shopping.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class Profile_MyOrdersActivity extends AppCompatActivity {
    private TabLayout tab_layout_my_orders;
    private ViewPager view_pager_my_orders;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopping apiShopping;
    RecyclerView recyclerview_details;
    Toolbar toolbar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_my_orders);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        tab_layout_my_orders = findViewById(R.id.tab_layout_my_orders);
        view_pager_my_orders = findViewById(R.id.view_page_my_orders);
        apiShopping = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopping.class);
        recyclerview_details = findViewById(R.id.recyclerview_details);
        toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerview_details.setLayoutManager(linearLayoutManager);


//        Profile_MyOrdersAdapter profile_myOrdersAdapter = new Profile_MyOrdersAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//
//        view_pager_my_orders.setAdapter(profile_myOrdersAdapter);
//        tab_layout_my_orders.setupWithViewPager(view_pager_my_orders);
    }
    
}
