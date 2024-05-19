package com.example.shopping.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {

    ReadableBottomBar readableBottomBar;
    private String userId, userName, userPhoneNumber, userEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userId = firebaseUser.getUid();
        userName = firebaseUser.getDisplayName();
        userEmail = firebaseUser.getEmail();
        userPhoneNumber = firebaseUser.getPhoneNumber();

        readableBottomBar = findViewById(R.id.readableBottomBar);

        // bắt đầu quá trình thay đổi fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //Thay thế nội dung hiện tại bằng HomeFragment
        fragmentTransaction.replace(R.id.content, new HomeFragment());
        // xác nhận thay đổi
        fragmentTransaction.commit();

        readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();

                switch (i){
                    case 0:
                        fragmentTransaction.replace(R.id.content, new HomeFragment());
                        fragmentTransaction.commit();
                        break;

                    case 1:
                        fragmentTransaction.replace(R.id.content, new NotiFragment());
                        fragmentTransaction.commit();
                        break;

                    case 2:
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", userId);
                        bundle.putString("userName", userName);
                        bundle.putString("userEmail", userEmail);
                        bundle.putString("userPhoneNumber", userPhoneNumber);
                        ProfileFragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);

                        fragmentTransaction.replace(R.id.content, profileFragment);
                        fragmentTransaction.commit();
                        break;
                }
            }
        });

        if (isConnected(this)) {
            Toast.makeText(getApplicationContext(), "Connect internet success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Connect internet fail", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            }
        }

        return false;
    }
}