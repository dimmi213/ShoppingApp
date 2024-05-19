package com.example.shopping.activity.auth;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shopping.databinding.ActivityRegisterBinding;
import com.example.shopping.retrofit.ApiShopping;
import com.example.shopping.retrofit.RetrofitClient;
import com.example.shopping.utils.Utils;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email_address, password, confirm_password;
    AppCompatButton signup;
    TextView login;
    ImageView google, facebook, twitter;

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFireStore;
    String userId;
    ApiShopping apiShopping;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFireStore = FirebaseFirestore.getInstance();
        apiShopping = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopping.class);

        username = binding.username;
        email_address = binding.emailAddress;
        password = binding.password;
        confirm_password = binding.confirmPassword;
        signup = binding.signup;
        google = binding.google;
        facebook = binding.facebook;
        login = binding.login;
        twitter = binding.twitter;

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.username.getText().toString();
                String email_address = binding.emailAddress.getText().toString();
                String password = binding.password.getText().toString();
                String confirm_password = binding.confirmPassword.getText().toString();

                String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (!email_address.matches(email_pattern)) {
                    Toast.makeText(RegisterActivity.this, "Địa chỉ email không đúng định dạng.", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getApplicationContext(), "Không được để trống tên.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email_address)) {
                    Toast.makeText(getApplicationContext(), "Không được để trống email.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Không được để trống mật khẩu.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Không được để trống xác nhận mật khẩu.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!password.equals(confirm_password)) {
                        Toast.makeText(getApplicationContext(), "Xác nhận mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
                        compositeDisposable.add(apiShopping.postUserRegister("", email_address, username, password, null)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            if (userModel.isSuccess()){
                                                Toast.makeText(RegisterActivity.this, "Đăng kí thành công.", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        },
                                        throwable -> {
                                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }
                }

                firebaseAuth.createUserWithEmailAndPassword(email_address, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    userId = firebaseAuth.getCurrentUser().getUid();
                                    if (user != null) {
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> emailTask) {
                                                if (emailTask.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Đăng kí thành công. Mở hòm thư để xác thực.", Toast.LENGTH_SHORT).show();
                                                    DocumentReference documentReference = firebaseFireStore.collection("users").document(userId);
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("userId", userId);
                                                    user.put("userName", username);
                                                    user.put("userEmail", email_address);
                                                    user.put("profileImageUrl", null);
                                                    user.put("userPassword", password);
                                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                                    saveInMySQLDB();
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

            private void saveInMySQLDB() {
                String userId = firebaseAuth.getCurrentUser().getUid();

                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                String url = Utils.BASE_URL + "postUserRegister.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response from server: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", userId);
                        params.put("userName", username.getText().toString());
                        params.put("userEmail", email_address.getText().toString());
                        params.put("userPassword", password.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
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
