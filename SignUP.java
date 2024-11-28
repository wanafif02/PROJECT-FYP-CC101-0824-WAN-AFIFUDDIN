package com.example.gymmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUP extends AppCompatActivity {

    private EditText nameUser, unitUser, phoneUser, emailUser, pwdUser, cpwdUser;
    private ProgressBar progressBar;
    private TextView login;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressBar = findViewById(R.id.progressBar);
        nameUser = findViewById(R.id.nameUser);
        unitUser = findViewById(R.id.unitUser);
        phoneUser = findViewById(R.id.editTextPhone);
        emailUser = findViewById(R.id.eTEmail);
        pwdUser = findViewById(R.id.editTextTextPassword);
        cpwdUser = findViewById(R.id.editTextTextPassword2);

        //LOGIN
        login = findViewById(R.id.textView3);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUP.this, "You can login now", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUP.this, SignIN.class));
            }
        });

        Button buttonRegister = findViewById(R.id.btnSignUp1);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameUser1= nameUser.getText().toString();
                String unitUser1= unitUser.getText().toString();
                String phoneUser1 = phoneUser.getText().toString();
                String emailUser1 = emailUser.getText().toString();
                String pwdUser1 = pwdUser.getText().toString();
                String cpwdUser1 = cpwdUser.getText().toString();

                if (TextUtils.isEmpty(nameUser1)) {
                    showErrorAndFocus(nameUser, "Please enter your full name");
                }  else if (TextUtils.isEmpty(emailUser1)) {
                    showErrorAndFocus(emailUser, "Please enter your email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser1).matches()) {
                    showErrorAndFocus(emailUser, "Please enter a valid email");
                } else if (TextUtils.isEmpty(pwdUser1)) {
                    showErrorAndFocus(pwdUser, "Please enter your password");
                } else if (pwdUser1.length() < 6) {
                    showErrorAndFocus(pwdUser, "Password should be at least 6 characters");
                } else if (!TextUtils.equals(pwdUser1, cpwdUser1)) {
                    showErrorAndFocus(cpwdUser, "Passwords do not match");
                    pwdUser.setText("");
                    cpwdUser.setText("");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(nameUser1, unitUser1, phoneUser1, emailUser1, pwdUser1);
                }
            }
        });
    }

    private void registerUser(String nameUser1, String unitUser1,  String phoneUser1, String emailUser1, String pwdUser1) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        //create user profile
        auth.createUserWithEmailAndPassword(emailUser1, pwdUser1).addOnCompleteListener(SignUP.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseuser = auth.getCurrentUser();
                    String userID = firebaseuser.getUid();
                    String roleUser = "Resident";
                    //update display name of user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nameUser1).build();
                    firebaseuser.updateProfile(profileChangeRequest);

                    // user data into realtime database
                    SignClass classRef = new SignClass(userID, nameUser1, unitUser1, phoneUser1, emailUser1, pwdUser1, roleUser);

                    //extract user reference from database for register user
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(firebaseuser.getUid());

                    referenceProfile.setValue(classRef).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                Toast.makeText(SignUP.this, " Resident registered successfully", Toast.LENGTH_LONG).show();
                                //open user profile after succes
                                Intent intent = new Intent (SignUP.this, UserDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // close register

                            } else {

                                Toast.makeText(SignUP.this, " Resident registered failed. Please try again", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        pwdUser.setError("Your password is too weak, kindly use a mix of alphabets, numbers, and special characters");
                        pwdUser.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        emailUser.setError("Your email is invalid or already in use. kindly re-enter");
                        emailUser.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        emailUser.setError("Resident is already registered with this email. Use another email.");
                        emailUser.requestFocus();
                    } catch (Exception e){
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignUP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    // Method to show error message and set focus
    private void showErrorAndFocus(EditText editText, String errorMessage) {
        editText.setError(errorMessage);
        editText.requestFocus();
    }


}