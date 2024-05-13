package com.example.shopping.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.shopping.activity.profile.my_orders.AllOrdersFragment;
import com.example.shopping.activity.profile.my_orders.Cancelled_OrdersFragment;
import com.example.shopping.activity.profile.my_orders.DeliveryFragment;
import com.example.shopping.activity.profile.my_orders.Done_OrdersFragment;
import com.example.shopping.activity.profile.my_orders.Waiting_For_DeliveryFragment;
import com.example.shopping.activity.profile.my_orders.Waiting_For_Pay_OrdersFragment;

public class Profile_MyOrdersAdapter extends FragmentStatePagerAdapter {
    public Profile_MyOrdersAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllOrdersFragment();
            case 1:
                return new Waiting_For_Pay_OrdersFragment();
            case 2:
                return new Waiting_For_DeliveryFragment();
            case 3:
                return new DeliveryFragment();
            case 4:
                return new Done_OrdersFragment();
            case 5:
                return new Cancelled_OrdersFragment();
            default:
                return new AllOrdersFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title ="";
        switch (position) {
            case 0:
                title = "Tất cả";
                break;
            case 1:
                title = "Chờ thanh toán";
                break;
            case 2:
                title = "Chờ vận chuyển";
                break;
            case 3:
                title = "Vận chuyển";
                break;
            case 4:
                title = "Hoàn thành";
                break;
            case 5:
                title = "Đã hủy";
                break;
        }
        return title;
    }
}
