package com.example.gymmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignOption extends AppCompatActivity {

    // Initialize all components
    private Button btnSignup;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_option);

        // Declare all components
        btnSignup = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogIn);

        // Function for button Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignOption.this, SignIN.class);
                startActivity(i);
            }
        });

        // Function for button Signup
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignOption.this, SignUP.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseAuth authProfile = FirebaseAuth.getInstance();
        FirebaseUser currentUser = authProfile.getCurrentUser();
        if(currentUser != null){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String role = dataSnapshot.child("roleUser").getValue(String.class);
                        if (role != null && role.equals("Resident")) {
                            startActivity(new Intent(SignOption.this, UserDashboard.class));
                            finish();
                        } else if (role != null && role.equals("Admin")) {
                            startActivity(new Intent(SignOption.this, AdminDashboard.class));
                            finish();
                        }
                        else {
                            Toast.makeText(SignOption.this, "Failed to login", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Handle if user data doesn't exist
                        Toast.makeText(SignOption.this, "user data doesn't exist", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(SignOption.this, "Failed Logged in", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            // User is not logged in, continue with login process or display login screen
            Toast.makeText(SignOption.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}