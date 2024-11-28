package com.example.gymmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private Handler mDelayHandler;
    private static final long SPLASH_DELAY = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mDelayHandler = new Handler();
        mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashScreen.this, SignOption.class);
            startActivity(intent);
            finish();
        }
    };

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
                        if (role != null && role.equals("User")) {
                            startActivity(new Intent(SplashScreen.this, UserDashboard.class));
                            finish();
                        } else if (role != null && role.equals("Admin")) {
                            startActivity(new Intent(SplashScreen.this, AdminDashboard.class));
                            finish();
                        }
                        else {
                        }
                    } else {
                        // Handle if user data doesn't exist
                        Toast.makeText(SplashScreen.this, "user data doesn't exist", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(SplashScreen.this, "Failed Logged in", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            // User is not logged in, continue with login process or display login screen
            Toast.makeText(SplashScreen.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}