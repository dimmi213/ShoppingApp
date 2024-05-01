package com.example.shopping.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopping.activity.MainActivity;
import com.example.shopping.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText email_address;
    AppCompatButton forget_password;
    TextView login;
    RelativeLayout google, facebook;

    ActivityForgetPasswordBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFireStore;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFireStore = FirebaseFirestore.getInstance();

        email_address = binding.emailAddress;
        forget_password = binding.forgetPassword;
        google = binding.google;
        facebook = binding.facebook;
        login = binding.login;

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_address = binding.emailAddress.getText().toString();
                String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (email_address.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Không được để trống.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email_address.matches(email_pattern)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Email không đúng định dạng.", Toast.LENGTH_SHORT).show();
                }
                FirebaseFirestore.getInstance().collection("users")
                        .whereEqualTo("email", email_address)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        firebaseAuth.sendPasswordResetEmail(email_address).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> resetTask) {
                                                if (resetTask.isSuccessful()) {
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                                    if (user != null && user.isEmailVerified()) {
                                                        Toast.makeText(ForgetPasswordActivity.this, "Mở hòm thư để đặt lại mật khẩu.", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(ForgetPasswordActivity.this, "Vui lòng xác thực email trước khi đặt lại mật khẩu.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(ForgetPasswordActivity.this, "Gửi email đặt lại mật khẩu thất bại: " + resetTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(ForgetPasswordActivity.this, "Email không tồn tại.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "Lỗi khi kiểm tra email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
            }
        });
    }
}