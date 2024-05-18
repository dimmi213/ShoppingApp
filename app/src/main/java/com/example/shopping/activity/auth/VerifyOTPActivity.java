package com.example.shopping.activity.auth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopping.activity.MainActivity;
import com.example.shopping.databinding.ActivityVerifyOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {
    private EditText input1, input2, input3, input4, input5, input6;
    private TextView phone_number, resendOTP;

    private AppCompatButton update_phone_number;
    private ProgressBar progressBar;
    private ActivityVerifyOtpactivityBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference userRef;

    private String verificationId;
    private String userId;
    private int failedAttempts = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        input1 = binding.input1;
        input2 = binding.input2;
        input3 = binding.input3;
        input4 = binding.input4;
        input5 = binding.input5;
        input6 = binding.input6;
        phone_number = binding.phoneNumber;
        update_phone_number = binding.updatePhoneNumber;
        resendOTP = binding.resendOTP;
        progressBar = binding.progressBar;

        phone_number.setText(String.format("+84 %s", getIntent().getStringExtra("mobile")));
        firebaseAuth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");
        userId = getIntent().getStringExtra("userId");

        setUpOTPinputs();

        update_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = input1.getText().toString().trim() +
                        input2.getText().toString().trim() +
                        input3.getText().toString().trim() +
                        input4.getText().toString().trim() +
                        input5.getText().toString().trim() +
                        input6.getText().toString().trim();
                if (otp.length() == 6){
                    if (verificationId != null){
                        progressBar.setVisibility(View.VISIBLE);
                        update_phone_number.setVisibility(View.INVISIBLE);
                        verifyOTP(otp);
                    }
                } else if (!otp.equals(verificationId)) {
                    Toast.makeText(VerifyOTPActivity.this, "Xác thực không chính xác.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "Vui lòng nhập mã OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+84" + getIntent().getStringExtra("mobile"),
                    60,
                    TimeUnit.SECONDS,
                    VerifyOTPActivity.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Toast.makeText(VerifyOTPActivity.this, "Xác thực thành công.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(VerifyOTPActivity.this, "Xác thực thất bại.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            verificationId = newVerificationId;
                            Toast.makeText(VerifyOTPActivity.this, "Mã OTP đã được gửi lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
        });



    }

    private void setUpOTPinputs(){
        input1.addTextChangedListener(new OTPTextWatcher(input1, input2));
        input2.addTextChangedListener(new OTPTextWatcher(input2, input3));
        input3.addTextChangedListener(new OTPTextWatcher(input3, input4));
        input4.addTextChangedListener(new OTPTextWatcher(input4, input5));
        input5.addTextChangedListener(new OTPTextWatcher(input5, input6));
        input6.addTextChangedListener(new OTPTextWatcher(input6, null));
    }

    private class OTPTextWatcher implements TextWatcher {
        private View currentView;
        private View nextView;

        OTPTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().trim().isEmpty() && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    //Verify otp
    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                update_phone_number.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    String phoneNumber = "+84" + getIntent().getStringExtra("mobile");
                    updatePhoneNumber(phoneNumber);

                    Toast.makeText(VerifyOTPActivity.this, "Xác thực thành công.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    failedAttempts++;
                    Toast.makeText(VerifyOTPActivity.this, "Xác thực thất bại.", Toast.LENGTH_SHORT).show();
                    if (failedAttempts >= 3) {
                        startActivity(new Intent(VerifyOTPActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    private void registerUserByPhone(FirebaseUser firebaseUser) {
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser.getUid();

        String newPhoneNumber = "+84" + getIntent().getStringExtra("mobile");

        DocumentReference userRef = db.collection("users").document(userId);
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("userPhoneNumber", newPhoneNumber);

        userRef.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: user " + userId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: user " + e.toString());
            }
        });
    }

    private void updatePhoneNumber(String newPhoneNumber) {
        db = FirebaseFirestore.getInstance();
        userId = firebaseUser != null ? firebaseUser.getUid() : null;
        if (userId != null) {
            newPhoneNumber = "+84" + getIntent().getStringExtra("mobile");
            userRef = db.collection("users").document(userId);
            userRef.update("userPhoneNumber", newPhoneNumber)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: user phone number updated" + userId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: failed to update user phone number" + userId + ", " + e.toString());
                        }
                    });
        } else {
            Log.d(TAG, "onFailure: userId is null, cannot update phone number");
//            registerUserByPhone(firebaseUser);
        }
    }
}