package com.example.shopping.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopping.R;
import com.example.shopping.activity.auth.UpdatePhoneNumberActivity;
import com.example.shopping.activity.profile.personal_profile.ChangeEmailFragment;
import com.example.shopping.activity.profile.personal_profile.Details_PersonalProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_PersonalProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_PersonalProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout personal_profile, address, bank, reset_password, change_email_address, update_phone_number;
    private String userId;

    public Profile_PersonalProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile_PersonalProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile_PersonalProfileFragment newInstance(String param1, String param2) {
        Profile_PersonalProfileFragment fragment = new Profile_PersonalProfileFragment();
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
        View view =  inflater.inflate(R.layout.fragment_profile__personal_profile, container, false);

        personal_profile = view.findViewById(R.id.personal_profile);
        address = view.findViewById(R.id.address);
        bank = view.findViewById(R.id.bank);
        reset_password = view.findViewById(R.id.reset_password);
        change_email_address = view.findViewById(R.id.change_email_address);
        update_phone_number = view.findViewById(R.id.update_phone_number);

        String userId = getArguments().getString("userId");

        personal_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Details_PersonalProfileFragment());
            }
        });

        change_email_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { replaceFragment(new ChangeEmailFragment()); }
        });

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                replaceFragment(new ChangePhoneNumberFragment());
            }
        });

        update_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdatePhoneNumberActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
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