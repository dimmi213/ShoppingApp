package com.example.shopping.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.shopping.R;
import com.example.shopping.activity.profile.Profile_MyOrdersFragment;
import com.example.shopping.activity.profile.Profile_PersonalProfileFragment;
import com.example.shopping.activity.profile.Profile_SettingsFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AppCompatButton button_my_account, button_my_orders, button_setting;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        button_my_account = view.findViewById(R.id.button_my_account);
        button_my_orders = view.findViewById(R.id.button_my_orders);
        button_setting = view.findViewById(R.id.button_setting);

        button_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Profile_PersonalProfileFragment(), R.id.fragment_profile_my_account);
            }
        });

        button_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Profile_MyOrdersFragment(), R.id.fragment_profile_my_orders);
            }
        });

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Profile_SettingsFragment(), R.id.fragment_profile_settings);
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String userName = bundle.getString("userName");
            String userEmail = bundle.getString("userEmail");
            String profileImageURL = bundle.getString("profileImageURL");

            ImageView imageProfile = view.findViewById(R.id.profile_image_URL);
            TextView nameProfile = view.findViewById(R.id.name_profile);
            TextView emailProfile = view.findViewById(R.id.email_bio_profile);

            Glide.with(this).load(profileImageURL).into(imageProfile);
            nameProfile.setText(userName);
            emailProfile.setText(userEmail);
        }

        return view;
    }

    private void replaceFragment(Fragment fragment, int containerId) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }
}