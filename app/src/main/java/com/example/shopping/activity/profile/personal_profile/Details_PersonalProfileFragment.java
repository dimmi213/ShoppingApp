package com.example.shopping.activity.profile.personal_profile;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shopping.R;
import com.example.shopping.activity.auth.UpdatePhoneNumberActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details_PersonalProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details_PersonalProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RelativeLayout relative_userName, relative_userGender, relative_userDoB, relative_userPhoneNumber, relative_userEmail, account_link;
    private EditText userName_edit, userGender_edit, userDoB_edit, userPhoneNumber_edit, userEmail_edit;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatePickerDialog datePickerDialog;

    public Details_PersonalProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details_PersonalProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Details_PersonalProfileFragment newInstance(String param1, String param2) {
        Details_PersonalProfileFragment fragment = new Details_PersonalProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details__personal_profile, container, false);

        relative_userName = view.findViewById(R.id.relative_userName);
        relative_userGender = view.findViewById(R.id.relative_userGender);
        relative_userDoB = view.findViewById(R.id.relative_userDoB);
        relative_userPhoneNumber = view.findViewById(R.id.relative_userPhoneNumber);
        relative_userEmail = view.findViewById(R.id.relative_userEmail);
        account_link = view.findViewById(R.id.account_link);

        userName_edit = view.findViewById(R.id.userName_edit);
        userGender_edit = view.findViewById(R.id.userGender_edit);
        userPhoneNumber_edit = view.findViewById(R.id.userPhoneNumber_edit);
        userDoB_edit = view.findViewById(R.id.userDoB_edit);
        userEmail_edit = view.findViewById(R.id.userEmail_edit);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //FETCH DATA
        fectchData();

        //UPDATE NAME
        userName_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String newName = userName_edit.getText().toString();
                    updateUserName(newName);
                    fectchData();
                }
            }
        });


        //EDIT GENDER
        relative_userGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGender();
            }
        });

        //EDIT DATE OF BIRTH
        relative_userDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
                fectchData();
            }
        });

        //UPDATE PHONE NUMBER
        relative_userPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdatePhoneNumberActivity.class);
                intent.putExtra("userId", firebaseUser.getUid());
                startActivity(intent);
            }
        });

        //CHANGE EMAIL
        relative_userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.features, new ChangeEmailFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                fectchData();
            }
        });

        return view;
    }

    private void fectchData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        String username = documentSnapshot.getString("userName");
                        userName_edit.setText(username);
                    }
                    if (documentSnapshot.contains("userEmail")) {
                        String email = documentSnapshot.getString("userEmail");
                        userEmail_edit.setText(email);
                    }
                    if (documentSnapshot.contains("userGender")) {
                        String gender = documentSnapshot.getString("userGender");
                        userGender_edit.setText(gender);
                    }
                    if (documentSnapshot.contains("userDoB")) {
                        String dateOfBirth = documentSnapshot.getString("userDoB");
                        userDoB_edit.setText(dateOfBirth);
                    }
                    if (documentSnapshot.contains("userPhoneNumber")) {
                        String phoneNumber = documentSnapshot.getString("userPhoneNumber");
                        userPhoneNumber_edit.setText(phoneNumber);
                    }
                } else {
                    userName_edit.setText("");
                    userGender_edit.setText("");
                    userDoB_edit.setText("");
                    userPhoneNumber_edit.setText("");
                    userEmail_edit.setText("");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure fetch user: " + e.getMessage());
            }
        });
    }

    //SHOW GENDER
    private void showGender() {
        PopupMenu popupMenu = new PopupMenu(getContext(), relative_userGender);
        popupMenu.inflate(R.menu.popup_gender_details);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String gender = "";
                int id = item.getItemId();
                if (id == R.id.male) {
                    gender = "Nam";
                } else if (id == R.id.female) {
                    gender = "Nữ";
                }
                updateUserGender(gender);
                return true;
            }
        });
        popupMenu.show();
    }

    //SHOW DAY OF BIRTH
    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                userDoB_edit.setText(date);
                updateDateOfBirth(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);

        datePickerDialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }


    private String getMonthFormat(int month) {
        switch (month) {
            case 0:
                return "JAN";
            case 1:
                return "FEB";
            case 2:
                return "MAR";
            case 3:
                return "APR";
            case 4:
                return "MAY";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AUG";
            case 8:
                return "SEP";
            case 9:
                return "OCT";
            case 10:
                return "NOV";
            case 11:
                return "DEC";
            default:
                return "JAN";
        }
    }


    //UPDATE GENDER

    private void updateUserGender(String gender) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

        userRef.update("userGender", gender)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess gender:");
                        userGender_edit.setText(gender);
                        fectchData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure gender:", e);
                    }
                });
    }

    //UPDATE NAME
    private void updateUserName(String newName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

        userRef.update("userName", newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess userName:");
                        fectchData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure userName:", e);
                    }
                });
    }

    //UPDATE DAY OF BIRTH
    private void updateDateOfBirth(String dateOfBirth) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(firebaseUser.getUid());

        userRef.update("userDoB", dateOfBirth)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess DoB:");
                        fectchData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure DoB:", e);
                    }
                });
    }
}