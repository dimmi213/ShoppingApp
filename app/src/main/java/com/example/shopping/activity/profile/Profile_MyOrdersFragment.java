package com.example.shopping.activity.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.shopping.R;
import com.example.shopping.adapter.Profile_MyOrdersAdapter;
import com.google.android.material.tabs.TabLayout;

public class Profile_MyOrdersFragment extends Fragment {
    private TabLayout tab_layout_my_orders;
    private ViewPager view_pager_my_orders;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_my_orders, container, false);
        tab_layout_my_orders = view.findViewById(R.id.tab_layout_my_orders);
        view_pager_my_orders = view.findViewById(R.id.view_page_my_orders);

        Profile_MyOrdersAdapter profile_myOrdersAdapter = new Profile_MyOrdersAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        view_pager_my_orders.setAdapter(profile_myOrdersAdapter);
        tab_layout_my_orders.setupWithViewPager(view_pager_my_orders);

        return view;
    }

}
