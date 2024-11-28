package com.example.gymmobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UserProfile extends AppCompatActivity {

    private TextView welcomeName, nameUser, unitUser, emailUser, genderUser, phoneUser;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = UserProfile.class.getSimpleName();
    private SwipeRefreshLayout swipeContainer;
    private ImageView uploadpicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        //Logout
        Button buttonLogout = findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                Toast.makeText(UserProfile.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserProfile.this, SignIN.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        welcomeName = findViewById(R.id.welcomeName);
        nameUser = findViewById(R.id.nameUser);
        unitUser = findViewById(R.id.unitUser);
        emailUser = findViewById(R.id.emailUser);
        genderUser = findViewById(R.id.genderUser);
        phoneUser = findViewById(R.id.phoneUser);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(UserProfile.this, "Something went wrong! User's details are not available at the moment",
                    Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        uploadpicture = findViewById(R.id.imageView_profile_dp);
        uploadpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfile.this, "You update your picture now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfile.this, UserProfilePicture.class));

            }
        });

        //Update profile
        Button buttonUpdateProfile = findViewById(R.id.update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(UserProfile.this, "You update your detail now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfile.this, UserProfileUpdate.class));

            }
        });

        //refresh
        swipeToRefresh();
    }

    private void swipeToRefresh() {

        //lookk up for the swipe container
        SwipeRefreshLayout swipeContainer = findViewById(R.id.swipeContainer);

        //setup refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //code to refresh
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
                swipeContainer.setRefreshing(false);
            }
        });

        //configure color refresh
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String userID = firebaseUser.getUid();

        //Extract user reference from database for register
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SignClass readUserDetails = snapshot.getValue(SignClass.class);
                String name = firebaseUser.getDisplayName();
                String email = firebaseUser.getEmail();
                String gender = readUserDetails.genderUser;
                String phone = readUserDetails.phoneUser;
                String unitUser1 = readUserDetails.unitUser;

                genderUser.setText(gender);
                phoneUser.setText(phone);

                welcomeName.setText("Hai " + name + "!");
                nameUser.setText(name);
                emailUser.setText(email);
                unitUser.setText(unitUser1);

                //set user dp after upload
                Uri uri = firebaseUser.getPhotoUrl();

                //ImageView set ImageUri
                Picasso.get().load(uri).into(uploadpicture);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do something when back button is pressed
        // You can call finish() to close the current activity
        super.onBackPressed();
    }
}

