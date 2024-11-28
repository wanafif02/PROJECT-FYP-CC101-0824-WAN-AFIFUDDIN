package com.example.gymmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UserProfileUpdate extends AppCompatActivity {

    private EditText nameUser, unitUser, phoneUser;
    private RadioGroup genderUser;
    private RadioButton genderSelected;
    private String nameUser1, unitUser1, phoneUser1, genderUser1;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);

        //navigation bottom
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.page3);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.page1) {
                    startActivity(new Intent(getApplicationContext(), UserNoti.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.page2) {
                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.page3) {
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });


        //BACK FUNCTION
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement back button functionality here
                onBackPressed(); // Call onBackPressed method to navigate back
            }
        });

        progressBar = findViewById(R.id.progressBar);
        nameUser = findViewById(R.id.nameUser);
        unitUser = findViewById(R.id.unitUser);
        phoneUser = findViewById(R.id.phoneUser);
        genderUser = findViewById(R.id.genderUser);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //show profile
        showProfile(firebaseUser);

        //update profile
        Button buttonUpdateProfile = findViewById(R.id.update);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {

        int selectedGenderId= genderUser.getCheckedRadioButtonId();
        genderSelected = findViewById(selectedGenderId);

        //obtain data by user enter
        genderUser1 = genderSelected.getText().toString();
        nameUser1 = nameUser.getText().toString();
        unitUser1 = unitUser.getText().toString();
        phoneUser1 = phoneUser.getText().toString();

        String userID = firebaseUser.getUid();
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        progressBar.setVisibility(View.VISIBLE);

        profileRef.child("nameUser").setValue(nameUser1);
        profileRef.child("unitUser").setValue(unitUser1);
        profileRef.child("phoneUser").setValue(phoneUser1);
        profileRef.child("genderUser").setValue(genderUser1);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nameUser1).build();
        firebaseUser.updateProfile(profileUpdates);

        Toast.makeText(UserProfileUpdate.this, "Updating Successful!", Toast.LENGTH_SHORT).show();
        onBackPressed();
        progressBar.setVisibility(View.GONE);

        /*if (TextUtils.isEmpty(nameUser1)) {
            showErrorAndFocus(nameUser, "Please enter your full name");
        } /*else if (TextUtils.isEmpty(phoneUser1)) {
            showErrorAndFocus(phoneUser, "Please enter your mobile number");
        }/* else if (phoneUser1.length() != 10) {
            showErrorAndFocus(phoneUser, "Mobile number should be 10 digits");
        } else if (!phoneUser1.matches("[0-9]{10}")) {
            showErrorAndFocus(phoneUser, "Please enter a valid mobile number");
        }
        else {

        } */
    }

    //fetch data from firebase and display
    private void showProfile(FirebaseUser firebaseUser) {

        String userIDofRegistered = firebaseUser.getUid();

        //extract user reference from database for " register user"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userIDofRegistered);

        progressBar.setVisibility(View.VISIBLE);

        referenceProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SignClass readWriteUserDetails = snapshot.getValue(SignClass.class);

                if(readWriteUserDetails != null) {
                    nameUser1 =firebaseUser.getDisplayName();
                    phoneUser1 = readWriteUserDetails.getPhoneUser();
                    genderUser1 = readWriteUserDetails.getGenderUser();
                    unitUser1 = readWriteUserDetails.getUnitUser();
                    phoneUser.setText(phoneUser1);
                    nameUser.setText(nameUser1);
                    unitUser.setText(unitUser1);
                }
                else {
                    //show gender through radio button
                    if (genderUser1.equals("Male")) {
                        genderSelected = findViewById(R.id.radio_male);
                    }
                    else {
                        genderSelected = findViewById(R.id.radio_female);
                    }
                    genderSelected.setChecked(true);

                    Toast.makeText(UserProfileUpdate.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UserProfileUpdate.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    // Method to show error message and set focus
    private void showErrorAndFocus(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }

    @Override
    public void onBackPressed() {
        // Do something when back button is pressed
        // You can call finish() to close the current activity
        super.onBackPressed();
    }
}