package com.example.shopping.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopping.databinding.ActivityUpdatePhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class UpdatePhoneNumberActivity extends AppCompatActivity {

    private EditText new_phone_number;
    private AppCompatButton update_phone_number;
    private ProgressBar progressBar;

    private ActivityUpdatePhoneNumberBinding binding;
    private String verificationId;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new_phone_number = binding.newPhoneNumber;
        update_phone_number = binding.updatePhoneNumber;
        progressBar = binding.progressBar;

        userId = getIntent().getStringExtra("userId");

        update_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_phone_number.getText().toString().trim().isEmpty()){
                    Toast.makeText(UpdatePhoneNumberActivity.this, "Nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                update_phone_number.setVisibility(View.INVISIBLE);

                String phoneNumber = "+84" + new_phone_number.getText().toString().trim();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                    60,
                        TimeUnit.SECONDS,
                UpdatePhoneNumberActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){


                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            progressBar.setVisibility(View.GONE);
                            update_phone_number.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            progressBar.setVisibility(View.GONE);
                            update_phone_number.setVisibility(View.VISIBLE);
                            Toast.makeText(UpdatePhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            progressBar.setVisibility(View.GONE);
                            update_phone_number.setVisibility(View.VISIBLE);

                            verificationId = s;

                            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                            intent.putExtra("mobile", new_phone_number.getText().toString());
                            intent.putExtra("verificationId", verificationId);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    }
                );
            }
        });
    }
}