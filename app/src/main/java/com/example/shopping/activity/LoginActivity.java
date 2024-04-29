package com.example.shopping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shopping.R;

public class LoginActivity extends AppCompatActivity {
    EditText email_address, password;
    AppCompatButton login;
    TextView forget_password, signup;
    ImageView google, facebook;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_address = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forget_password = findViewById(R.id.forget_password);
        signup = findViewById(R.id.signup);
        google = findViewById(R.id.google);
        facebook = findViewById(R.id.facebook);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
