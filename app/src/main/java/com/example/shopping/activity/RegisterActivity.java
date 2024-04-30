package com.example.shopping.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopping.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email_address, password, confirm_password;
    AppCompatButton signup;
    TextView login;
    ImageView google, facebook;

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFireStore;
    String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFireStore = FirebaseFirestore.getInstance();

        username = binding.username;
        email_address = binding.emailAddress;
        password = binding.password;
        confirm_password = binding.confirmPassword;
        signup = binding.signup;
        google = binding.google;
        facebook = binding.facebook;
        login = binding.login;

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.username.getText().toString();
                String email_address = binding.emailAddress.getText().toString();
                String password = binding.password.getText().toString();
                String confirm_password = binding.confirmPassword.getText().toString();

                if (username.isEmpty() || email_address.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Không được để trống.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirm_password)) {
                    Toast.makeText(RegisterActivity.this, "Xác nhận mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email_address, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user != null) {
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> emailTask) {
                                                if (emailTask.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Đăng kí thành công. Mở hòm thư để xác thực.", Toast.LENGTH_SHORT).show();
                                                    userID = firebaseAuth.getCurrentUser().getUid();
                                                    DocumentReference documentReference = firebaseFireStore.collection("users").document(userID);
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("username", username);
                                                    user.put("email", email_address);
                                                    user.put("password", password);
                                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d(TAG, "onSuccess: user " + userID);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "onFailure: user " + e.toString());
                                                        }
                                                    });
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Gửi email xác thực thất bại: " + emailTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Email đã tồn tại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
