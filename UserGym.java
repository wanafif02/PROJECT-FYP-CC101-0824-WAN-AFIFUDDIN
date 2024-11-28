package com.example.gymmobileapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.LinkedList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UserGym extends AppCompatActivity {

    private EditText dateBook, remarkBook;
    private Spinner paxBook;
    private Button bookBtn;
    private RelativeLayout chooseTime;
    private TextView timeBook;
    String timeID, timeGymChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gym);

        //navigation bottom
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.page2);
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

        dateBook = findViewById(R.id.dateBook);
        chooseTime = findViewById(R.id.timeBook1);
        timeBook = findViewById(R.id.timeBook);
        paxBook = findViewById(R.id.paxBook);
        remarkBook = findViewById(R.id.remarkBook);
        bookBtn = findViewById(R.id.bookBtn);

         Intent intent = getIntent();
         timeID = intent.getStringExtra("timeID");
         timeGymChoose = intent.getStringExtra("timeGym");
         timeBook.setText(timeGymChoose);

        SharedPreferences preferences = getSharedPreferences("UserGymPrefs", MODE_PRIVATE);
        String savedDate = preferences.getString("selectedDate", "");

        // Restore the date if it exists
        if (!TextUtils.isEmpty(savedDate)) {
            dateBook.setText(savedDate);
        }

        dateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //date picker dialog
                DatePickerDialog picker = new DatePickerDialog(UserGym.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        dateBook.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        String formattedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        // Save the date in SharedPreferences
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("selectedDate", formattedDate);
                        editor.apply();
                    }
                }, year, month, day);

                // Set minimum date to current date
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                picker.show();
            }
        });

        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateGym = dateBook.getText().toString();

                if (TextUtils.isEmpty(dateGym)) {
                    Toast.makeText(UserGym.this, "Please choose the date first", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), UserGymTime.class);
                    intent.putExtra("dateGym", dateGym);
                    startActivity(intent);
                }

            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedDate = dateBook.getText().toString();
                String selectedTime = timeBook.getText().toString();

                if (TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(selectedTime)) {
                    Toast.makeText(UserGym.this, "Please choose both date and time", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference notificationRef = database.getReference().child("Notification");

                // Check if the selected date is under maintenance
                Query query = notificationRef.orderByChild("dateGym").equalTo(selectedDate);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // If a record exists for the selected date, it means it's under maintenance
                            Toast.makeText(UserGym.this, "This date is under maintenance. Please choose another date.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Proceed with booking as the date is not under maintenance
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference classAdd = database.getReference().child("Booking Gym");

                            // Use both date and time as the key
                            String dateTimeKey = selectedDate + "-" + selectedTime;
                            DatabaseReference bookingRef = classAdd.child(dateTimeKey);

                            bookingRef.child("date").setValue(selectedDate);
                            bookingRef.child("time").setValue(selectedTime);

                            // Maximum pax limit per time slot
                            int maxPaxPerSlot = 15;

                            bookingRef.child("totalPaxGym").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int currentTotalPax = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;

                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    String userID = firebaseUser.getUid();

                                    DatabaseReference userRef = database.getReference().child("Users").child(userID);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                // Retrieve user details
                                                SignClass user = snapshot.getValue(SignClass.class);
                                                String nameBook = user.getNameUser();
                                                String unitBook = user.getUnitUser();
                                                String emailUser = user.getEmailUser();
                                                String phoneBook = user.getPhoneUser();
                                                String genderBook = user.getGenderUser();
                                                String roleUser = user.getRoleUser();
                                                String imgUserBook = user.getImgUser();

                                                // Create unique booking ID
                                                String bookID = bookingRef.push().getKey();
                                                String paxValue = paxBook.getSelectedItem().toString();
                                                int paxCount = Integer.parseInt(paxValue);

                                                if (currentTotalPax + paxCount <= maxPaxPerSlot) {

                                                    // Store the booking details under the current date
                                                    bookingRef.child(bookID).child("userID").setValue(userID);
                                                    bookingRef.child(bookID).child("nameUser").setValue(nameBook);
                                                    bookingRef.child(bookID).child("unitUser").setValue(unitBook);
                                                    bookingRef.child(bookID).child("emailUser").setValue(emailUser);
                                                    bookingRef.child(bookID).child("phoneUser").setValue(phoneBook);
                                                    bookingRef.child(bookID).child("genderUser").setValue(genderBook);
                                                    bookingRef.child(bookID).child("roleUser").setValue(roleUser);
                                                    bookingRef.child(bookID).child("imgUser").setValue(imgUserBook);
                                                    bookingRef.child(bookID).child("countPax").setValue(paxCount); // Store pax count for this booking

                                                    // Update the total pax count for the day
                                                    bookingRef.child("totalPaxGym").setValue(currentTotalPax + paxCount)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        // Booking successful, show confirmation
                                                                        Toast.makeText(view.getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(getApplicationContext(), UserUpcoming.class));

                                                                    } else {
                                                                        Toast.makeText(view.getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                    // Update the total pax count and remaining pax for the day
                                                    int newTotalPax = currentTotalPax + paxCount;
                                                    bookingRef.child("remainingPax").setValue(maxPaxPerSlot - newTotalPax)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        // Booking successful, show confirmation
                                                                        Toast.makeText(view.getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();

                                                                        // If remaining pax is now 0, show a message or disable further bookings
                                                                        if (maxPaxPerSlot - newTotalPax == 0) {
                                                                            Toast.makeText(view.getContext(), "Booking limit reached for today. No more bookings allowed.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(view.getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                    String dateGym = dateBook.getText().toString();
                                                    String timeGym = timeBook.getText().toString();
                                                    int paxGym = Integer.parseInt(paxBook.getSelectedItem().toString());
                                                    String remarkGym = remarkBook.getText().toString();
                                                    String statusGym = "Booking";

                                                    DatabaseReference userRef = database.getReference().child("List Resident Booking");
                                                    String gymID = userRef.push().getKey();
                                                    UserGymClass classRef2 = new UserGymClass(
                                                            userID, nameBook, unitBook,emailUser, phoneBook, genderBook, roleUser, imgUserBook,
                                                            gymID, dateGym, timeGym, paxGym, remarkGym, statusGym);
                                                    userRef.child(gymID).setValue(classRef2);

                                                    DatabaseReference scheduleGym = database.getReference().child("Schedule Gym").child(timeID);
                                                    // Update the total pax count and remaining pax for the day
                                                    scheduleGym.child("remainingPax").setValue(maxPaxPerSlot - newTotalPax);
                                                    //scheduleGym.child("countPax").setValue(paxCount);
                                                } else {
                                                    Toast.makeText(view.getContext(), "Time slot is fully booked.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserGym.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

       /* bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedDate = dateBook.getText().toString();
                String selectedTime = timeBook.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Notification");
                Query query = myRef.orderByChild("dateGym").equalTo(selectedDate);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(selectedTime)) {
                            Toast.makeText(UserGym.this, "Please choose both date and time", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference classAdd = database.getReference().child("Booking Gym");

                        // Use both date and time as the key
                        String dateTimeKey = selectedDate + "-" + selectedTime;
                        DatabaseReference bookingRef = classAdd.child(dateTimeKey);

                        bookingRef.child("date").setValue(selectedDate);
                        bookingRef.child("time").setValue(selectedTime);

                        // Maximum pax limit per time slot
                        int maxPaxPerSlot = 15;

                        bookingRef.child("totalPaxGym").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int currentTotalPax = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;

                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                String userID = firebaseUser.getUid();

                                DatabaseReference userRef = database.getReference().child("Users").child(userID);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Retrieve user details
                                            SignClass user = snapshot.getValue(SignClass.class);
                                            String nameBook = user.getNameUser();
                                            String unitBook = user.getUnitUser();
                                            String emailUser = user.getEmailUser();
                                            String phoneBook = user.getPhoneUser();
                                            String genderBook = user.getGenderUser();
                                            String roleUser = user.getRoleUser();
                                            String imgUserBook = user.getImgUser();

                                            // Create unique booking ID
                                            String bookID = bookingRef.push().getKey();
                                            String paxValue = paxBook.getSelectedItem().toString();
                                            int paxCount = Integer.parseInt(paxValue);

                                            if (currentTotalPax + paxCount <= maxPaxPerSlot) {

                                                // Store the booking details under the current date
                                                bookingRef.child(bookID).child("userID").setValue(userID);
                                                bookingRef.child(bookID).child("nameUser").setValue(nameBook);
                                                bookingRef.child(bookID).child("unitUser").setValue(unitBook);
                                                bookingRef.child(bookID).child("emailUser").setValue(emailUser);
                                                bookingRef.child(bookID).child("phoneUser").setValue(phoneBook);
                                                bookingRef.child(bookID).child("genderUser").setValue(genderBook);
                                                bookingRef.child(bookID).child("roleUser").setValue(roleUser);
                                                bookingRef.child(bookID).child("imgUser").setValue(imgUserBook);
                                                bookingRef.child(bookID).child("countPax").setValue(paxCount); // Store pax count for this booking

                                                // Update the total pax count for the day
                                                bookingRef.child("totalPaxGym").setValue(currentTotalPax + paxCount)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Booking successful, show confirmation
                                                                    Toast.makeText(view.getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(getApplicationContext(), UserUpcoming.class));

                                                                } else {
                                                                    Toast.makeText(view.getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                // Update the total pax count and remaining pax for the day
                                                int newTotalPax = currentTotalPax + paxCount;
                                                bookingRef.child("remainingPax").setValue(maxPaxPerSlot - newTotalPax)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Booking successful, show confirmation
                                                                    Toast.makeText(view.getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();

                                                                    // If remaining pax is now 0, show a message or disable further bookings
                                                                    if (maxPaxPerSlot - newTotalPax == 0) {
                                                                        Toast.makeText(view.getContext(), "Booking limit reached for today. No more bookings allowed.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(view.getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                String dateGym = dateBook.getText().toString();
                                                String timeGym = timeBook.getText().toString();
                                                int paxGym = Integer.parseInt(paxBook.getSelectedItem().toString());
                                                String remarkGym = remarkBook.getText().toString();
                                                String statusGym = "Booking";

                                                DatabaseReference userRef = database.getReference().child("List Resident Booking");
                                                String gymID = userRef.push().getKey();
                                                UserGymClass classRef2 = new UserGymClass(
                                                        userID, nameBook, unitBook,emailUser, phoneBook, genderBook, roleUser, imgUserBook,
                                                        gymID, dateGym, timeGym, paxGym, remarkGym, statusGym);
                                                userRef.child(gymID).setValue(classRef2);

                                                DatabaseReference scheduleGym = database.getReference().child("Schedule Gym").child(timeID);
                                                // Update the total pax count and remaining pax for the day
                                                scheduleGym.child("remainingPax").setValue(maxPaxPerSlot - newTotalPax);
                                                //scheduleGym.child("countPax").setValue(paxCount);
                                            } else {
                                                Toast.makeText(view.getContext(), "Time slot is fully booked.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                      //  Toast.makeText(UserGym.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(UserGym.this, "Under Maintaince Can Not Choose This Date.", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }); */

    /* bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference classAdd = database.getReference().child("Booking Gym");

                // Use the formatted date string from dateBook for the database reference key
                String selectedDate = dateBook.getText().toString();
                DatabaseReference bookingsForTodayRef = classAdd.child(selectedDate);
                bookingsForTodayRef.child("dateID").setValue(selectedDate);

                // Maximum pax limit for a day
                int maxPaxPerDay = 15;

                bookingsForTodayRef.child("totalPaxGym").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Retrieve the current number of pax booked for the day, or default to 0 if not set
                        int currentTotalPax = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userID = firebaseUser.getUid();

                        DatabaseReference classRef = database.getReference().child("Users").child(userID);
                        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Retrieve user details
                                    SignClass classRef1 = snapshot.getValue(SignClass.class);
                                    String nameBook = classRef1.getNameUser();
                                    String unitBook = classRef1.getUnitUser();
                                    String emailUser = classRef1.getEmailUser();
                                    String phoneBook = classRef1.getPhoneUser();
                                    String genderBook = classRef1.getGenderUser();
                                    String roleUser = classRef1.getRoleUser();
                                    String imgUserBook = classRef1.getImgUser();

                                    // Create a unique booking ID using push key
                                    String bookID = bookingsForTodayRef.push().getKey();

                                    // Get the pax value from user input
                                    String paxValue = paxBook.getSelectedItem().toString();
                                    int paxCount = Integer.parseInt(paxValue);

                                    // Check if there is enough space for the requested pax count
                                    if (currentTotalPax + paxCount <= maxPaxPerDay) {

                                        // Store the booking details under the current date
                                        bookingsForTodayRef.child(bookID).child("userID").setValue(userID);
                                        bookingsForTodayRef.child(bookID).child("nameUser").setValue(nameBook);
                                        bookingsForTodayRef.child(bookID).child("unitUser").setValue(unitBook);
                                        bookingsForTodayRef.child(bookID).child("emailUser").setValue(emailUser);
                                        bookingsForTodayRef.child(bookID).child("phoneUser").setValue(phoneBook);
                                        bookingsForTodayRef.child(bookID).child("genderUser").setValue(genderBook);
                                        bookingsForTodayRef.child(bookID).child("roleUser").setValue(roleUser);
                                        bookingsForTodayRef.child(bookID).child("imgUser").setValue(imgUserBook);
                                        bookingsForTodayRef.child(bookID).child("countPax").setValue(paxCount); // Store pax count for this booking

                                        // Update the total pax count for the day
                                        bookingsForTodayRef.child("totalPaxGym").setValue(currentTotalPax + paxCount)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Booking successful, show confirmation
                                                            Toast.makeText(view.getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(view.getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        // Update the total pax count and remaining pax for the day
                                        int newTotalPax = currentTotalPax + paxCount;
                                        bookingsForTodayRef.child("remainingPax").setValue(maxPaxPerDay - newTotalPax)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Booking successful, show confirmation
                                                            Toast.makeText(view.getContext(), "Booking successful!", Toast.LENGTH_SHORT).show();

                                                            // If remaining pax is now 0, show a message or disable further bookings
                                                            if (maxPaxPerDay - newTotalPax == 0) {
                                                                Toast.makeText(view.getContext(), "Booking limit reached for today. No more bookings allowed.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(view.getContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        String dateGym = dateBook.getText().toString();
                                        String timeGym = timeBook.getText().toString();
                                        String paxGym = paxBook.getSelectedItem().toString();
                                        String remarkGym = remarkBook.getText().toString();
                                        String statusGym = "Booking";

                                        DatabaseReference userRef = database.getReference().child("List Resident Booking");
                                        String gymID = userRef.push().getKey();
                                        UserGymBookClass classRef2 = new UserGymBookClass(
                                                userID, nameBook, unitBook,emailUser, phoneBook, genderBook, roleUser, imgUserBook,
                                                gymID, dateGym, timeGym, paxGym, remarkGym, statusGym);
                                        userRef.child(gymID).setValue(classRef2);

                                        DatabaseReference scheduleGym = database.getReference().child("Schedule Gym").child(timeID);
                                        // Update the total pax count and remaining pax for the day
                                        //int newTotalPax1 = currentTotalPax + paxCount;
                                        scheduleGym.child("remainingPax").setValue(maxPaxPerDay - newTotalPax);
                                        scheduleGym.child("countPax").setValue(paxCount);
                                        scheduleGym.child("dateID").setValue(selectedDate);

                                    } else {
                                        // Not enough available pax left for the requested count
                                        Toast.makeText(view.getContext(), "Booking limit reached for today. No more bookings allowed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error fetching user details
                                Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error when fetching booking data
                        Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }); */


    }

    @Override
    public void onBackPressed() {
        // Do something when back button is pressed
        // You can call finish() to close the current activity
        super.onBackPressed();
    }

}