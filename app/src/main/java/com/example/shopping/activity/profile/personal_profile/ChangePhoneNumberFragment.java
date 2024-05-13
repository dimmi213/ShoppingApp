package com.example.shopping.activity.profile.personal_profile;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePhoneNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePhoneNumberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText new_phone_number;
    AppCompatButton update_phone_number;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    PhoneAuthCredential phoneAuthCredential;
    String VerificationId;

    public ChangePhoneNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePhoneNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePhoneNumberFragment newInstance(String param1, String param2) {
        ChangePhoneNumberFragment fragment = new ChangePhoneNumberFragment();
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
        View view = inflater.inflate(R.layout.fragment_change_phone_number, container, false);

        new_phone_number = view.findViewById(R.id.new_phone_number);
        update_phone_number = view.findViewById(R.id.update_phone_number);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());
        String newPhoneNumber = "+84" + new_phone_number.getText().toString().trim();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks
                callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.features, new VerifyPhoneNumberFragment());
                transaction.addToBackStack(null);
                transaction.commit();

                userRef.update("userPhoneNumber", newPhoneNumber)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "New phone number saved to Firestore.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error updating phone number in Firestore: ", e);
                            }
                        });

                final String code = phoneAuthCredential.getSmsCode();
                if (code != null){
                    phoneAuthCredential = PhoneAuthProvider.getCredential(VerificationId, code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                Toast.makeText(getContext(), "Xác thực thất bại", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                Log.d(TAG, "onCodeSent:" + verificationId);

                VerificationId = verificationId;
            }
        };
        update_phone_number.setOnClickListener(new View.OnClickListener() {
            String phoneNumber = "+84" + new_phone_number.getText().toString().trim();
            @Override
            public void onClick(View v) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(firebaseAuth)
                                .setPhoneNumber(phoneNumber)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(getActivity())
                                .setCallbacks(callbacks)
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });


        return view;
    }
}