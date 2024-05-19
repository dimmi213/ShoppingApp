package com.example.shopping.activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.shopping.R;
import com.example.shopping.activity.auth.LoginActivity;
import com.example.shopping.activity.profile.Menu_ProfileFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView profile_image_URL;
    TextView name_profile, email_address;
    RelativeLayout personal_profile, setiings;
    LinearLayout my_orders;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AppCompatButton logout;
    private String userId;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image_URL = view.findViewById(R.id.profile_image_URL);
        name_profile = view.findViewById(R.id.name_profile);
        email_address = view.findViewById(R.id.email_address);
        logout = view.findViewById(R.id.logout);

        String userId = getArguments().getString("userId");

        FrameLayout featuresLayout = view.findViewById(R.id.features);

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        Menu_ProfileFragment menuProfileFragment = new Menu_ProfileFragment();
        menuProfileFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().replace(R.id.features, menuProfileFragment).commit();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseUser != null) {
            List<? extends UserInfo> providerData = firebaseUser.getProviderData();

            for (UserInfo userInfo : providerData) {
                String providerId = userInfo.getProviderId();

                if (providerId.equals(EmailAuthProvider.PROVIDER_ID)) {
                    fetchUserDataByEmailAndPassword();
                } else if (providerId.equals(GoogleAuthProvider.PROVIDER_ID)) {
                    fetchUserDataByGoogle();
                } else if (providerId.equals(FacebookAuthProvider.PROVIDER_ID)) {
                    fetchUserDataByFacebook();
                }
            }
        } else {
            name_profile.setText("");
            email_address.setText("");
            profile_image_URL.setImageResource(R.drawable.default_profile_image);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

                firebaseAuth.signOut();
                googleSignInClient.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void fetchUserDataByEmailAndPassword(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        String username = documentSnapshot.getString("userName");
                        name_profile.setText(username);
                    }
                    if (documentSnapshot.contains("userEmail")) {
                        String email = documentSnapshot.getString("userEmail");
                        email_address.setText(email);
                    }if (documentSnapshot.contains("profileImageUrl")) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Picasso.get()
                                    .load(profileImageUrl)
                                    .into(profile_image_URL);
                        } else {
                            profile_image_URL.setImageResource(R.drawable.default_profile_image);
                        }
                    }
                } else {
                    name_profile.setText("");
                    email_address.setText("");
                    profile_image_URL.setImageResource(R.drawable.default_profile_image);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure fetch user: " + e.getMessage());
            }
        });
    }

    private void fetchUserDataByGoogle(){
        String username = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        String profileImageUrl = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;

        name_profile.setText(username);
        email_address.setText(email);
        if (profileImageUrl != null) {
            Picasso.get()
                    .load(profileImageUrl)
                    .into(profile_image_URL);
        } else {
            profile_image_URL.setImageResource(R.drawable.default_profile_image);
        }
    }


    private void fetchUserDataByFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String name_profile_text = object.getString("name_profile");
                            name_profile.setText(name_profile_text);
                            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Picasso.get().load(url).into(profile_image_URL);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}