package com.example.shopping.activity.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu_ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu_ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView order_history, order_waiting_for_pay, order_waiting_for_delivery, order_delivery, order_rate;
    private RelativeLayout personal_profile, settings;
    private String userId;

    public Menu_ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Menu_ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Menu_ProfileFragment newInstance(String param1, String param2) {
        Menu_ProfileFragment fragment = new Menu_ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu__profile, container, false);

        order_history = view.findViewById(R.id.order_history);
        order_waiting_for_pay = view.findViewById(R.id.order_waiting_for_pay);
        order_waiting_for_delivery = view.findViewById(R.id.order_waiting_for_delivery);
        order_delivery = view.findViewById(R.id.order_delivery);
        order_rate = view.findViewById(R.id.order_rate);
        order_delivery = view.findViewById(R.id.order_delivery);
        personal_profile = view.findViewById(R.id.personal_profile);
        settings = view.findViewById(R.id.settings);

        String userId = getArguments().getString("userId");

        personal_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);

                Profile_PersonalProfileFragment personalProfileFragment = new Profile_PersonalProfileFragment();
                personalProfileFragment.setArguments(bundle);

                replaceFragment(personalProfileFragment);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Profile_SettingsFragment());
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.features, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}