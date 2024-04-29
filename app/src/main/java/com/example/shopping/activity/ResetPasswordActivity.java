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

public class ResetPasswordActivity extends AppCompatActivity {
    EditText new_password;
    AppCompatButton button_reset_password;
    TextView login;
    ImageView google, facebook;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        new_password = findViewById(R.id.new_password);
        login = findViewById(R.id.login);
        button_reset_password = findViewById(R.id.button_reset_password);
        google = findViewById(R.id.google);
        facebook = findViewById(R.id.facebook);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
            }
        });
    }
}
