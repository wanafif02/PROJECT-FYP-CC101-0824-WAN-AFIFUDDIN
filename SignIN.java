package com.example.gymmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIN extends AppCompatActivity {

    private EditText emailUser, pwdUser;
    private Button login;
    private TextView register;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private static final String TAG = "SignIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailUser = findViewById(R.id.eTEmail);
        pwdUser = findViewById(R.id.eTPassword);
        progressBar = findViewById(R.id.progressBar);
        login = findViewById(R.id.btnLogin1);
        register = findViewById(R.id.textView3);

        authProfile = FirebaseAuth.getInstance();
        //Register in login
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignIN.this, "You can register now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignIN.this, SignUP.class));
            }
        });
        //login user
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = emailUser.getText().toString();
                String textPwd = pwdUser.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(SignIN.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    emailUser.setError("Email is required");
                    emailUser.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(SignIN.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    emailUser.setError(" valid email is required");
                    emailUser.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(SignIN.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    pwdUser.setError("Password is required");
                    pwdUser.requestFocus();
                } else if (textPwd.length() < 6) {
                    Toast.makeText(SignIN.this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                    pwdUser.setError("Password is required");
                    pwdUser.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPwd);
                }
            }
        });
    }

    private void loginUser(String email, String pwd) {

    /* // Check if the email and password belong to a manager account
        if (email.equals("admin@gymbookapp.com") && pwd.equals("admin123")) {
            // If the credentials are correct, start the dashboard activity for managers
            Toast.makeText(SignIN.this, "You are logged in as a Admin", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SignIN.this, AdminDashboard.class));
            return;
        }*/
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(SignIN.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if (firebaseUser != null) {
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(firebaseUser.getUid());
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String role = dataSnapshot.child("roleUser").getValue(String.class);
                                    if (role != null && role.equals("Resident")) {
                                        startActivity(new Intent(SignIN.this, UserDashboard.class));
                                        finish();
                                    } else if (role != null && role.equals("Admin")) {
                                        startActivity(new Intent(SignIN.this, AdminDashboard.class));
                                        finish();
                                    }
                                    else {
                                    }
                                } else {
                                    // Handle if user data doesn't exist
                                    Toast.makeText(SignIN.this, "user data doesn't exist", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error
                            }
                        });
                    }
                } else {
                    // Handle login failure
                    Toast.makeText(SignIN.this, "Failed Logged in", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

  /*  @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser = authProfile.getCurrentUser();
        if(currentUser != null){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String role = dataSnapshot.child("roleUser").getValue(String.class);
                        if (role != null && role.equals("Resident")) {
                            startActivity(new Intent(SignIN.this, UserDashboard.class));
                            finish();
                        } else if (role != null && role.equals("Admin")) {
                            startActivity(new Intent(SignIN.this, AdminDashboard.class));
                            finish();
                        }
                        else {
                        }
                    } else {
                        // Handle if user data doesn't exist
                        Toast.makeText(SignIN.this, "user data doesn't exist", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Toast.makeText(SignIN.this, "Failed Logged in", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            // User is not logged in, continue with login process or display login screen
            Toast.makeText(SignIN.this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    } */
}