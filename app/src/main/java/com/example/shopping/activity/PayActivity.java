package com.example.shopping.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shopping.R;
import com.example.shopping.retrofit.ApiShopping;
import com.example.shopping.retrofit.RetrofitClient;
import com.example.shopping.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PayActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttotalprice, txtphonenumber, txtemail;
    EditText editaddress;
    AppCompatButton btnorder;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopping apiShopping;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;
    DocumentReference userRef;
    private String userId, userPhoneNumber, userEmail;
    long totalprice;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userId = firebaseUser.getUid();

        fetchData();
        initView();
        countItem();
        initControl();
    }

    private void fetchData() {
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    if (documentSnapshot.contains("userEmail")) {
                        String email = documentSnapshot.getString("userEmail");
                        txtphonenumber.setText(email);
                    }
                    if (documentSnapshot.contains("userPhoneNumber")) {
                        String phoneNumber = documentSnapshot.getString("userPhoneNumber");
                        txtemail.setText(phoneNumber);
                    }
                } else {
                    txtphonenumber.setText("");
                    txtemail.setText("");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure fetch user: " + e.getMessage());
            }
        });
    }


    private void countItem() {
        totalItem = 0;
        for(int i=0; i< Utils.cartList.size(); i++){
            totalItem = totalItem + Utils.cartList.get(i).getAmount();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        totalprice = getIntent().getLongExtra("totalprice", 0);
        txttotalprice.setText(decimalFormat.format(totalprice));
//        txtemail.setText("mymin00007@gmail.com");
////        txtemail.setText(Utils.user_current.getEmail());
//        txtphonenumber.setText("0379874924");
        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_address = editaddress.getText().toString().trim();
                if (TextUtils.isEmpty(str_address)) {
                    Toast.makeText(getApplicationContext(), "Please input your address", Toast.LENGTH_SHORT).show();
                } else {
                    //post data
//                    String str_email = Utils.user_curent.getEmail();
//                    String str_sdt = Utils.user_current.getMobile();
//                    int id = Utils.user_current.getId();
                    String str_email = userEmail;
                    String str_sdt = userPhoneNumber;
                    String id = userId;

                    Log.d("test", new Gson().toJson(Utils.cartList));
                    compositeDisposable.add(apiShopping.createOder(str_email, str_sdt, String.valueOf(totalprice), id, str_address,totalItem,new Gson().toJson(Utils.cartList))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView() {
        apiShopping = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopping.class);
        toolbar = findViewById(R.id.toobar);
        txttotalprice = findViewById(R.id.txttotalprice);
        txtemail = findViewById(R.id.txtemail);
        txtphonenumber = findViewById(R.id.txtphonenumber);
        editaddress = findViewById(R.id.edtaddress);
        btnorder = findViewById(R.id.btnorder);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}