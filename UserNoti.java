package com.example.gymmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UserNoti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_noti);

        //navigation bottom
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.page1);
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

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

// Set up LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(UserNoti.this));

// Initialize adapter
        UserNotiMaintainanceAdapter userAdapter = new UserNotiMaintainanceAdapter(new LinkedList<UserNotiClass>(), UserNoti.this);
        recyclerView.setAdapter(userAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Notification");

// Combined list for both queries
        LinkedList<UserNotiClass> combinedList = new LinkedList<>();

// Query 1: Filter by userID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        Query queryUser = myRef.orderByChild("userID").equalTo(userID);
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinkedList<UserNotiClass> userList = new LinkedList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserNotiClass classRef = dataSnapshot.getValue(UserNotiClass.class);
                    if (classRef != null) {
                        userList.add(classRef);
                    }
                }

                // Merge the user-specific list into the combined list
                combinedList.clear();  // Clear the list to avoid duplicates from previous updates
                combinedList.addAll(userList);  // Add the user-specific data

                // Update adapter with the merged list
                userAdapter.listItem = combinedList;
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserNoti.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Query 2: Filter by status
        String status = "Under Maintenance";
        Query queryStatus = myRef.orderByChild("status").equalTo(status);
        queryStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinkedList<UserNotiClass> statusList = new LinkedList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserNotiClass classRef = dataSnapshot.getValue(UserNotiClass.class);
                    if (classRef != null) {
                        statusList.add(classRef);
                    }
                }

                // Merge the status-specific list into the combined list, avoiding duplicates
                for (UserNotiClass statusItem : statusList) {
                    if (!combinedList.contains(statusItem)) {
                        combinedList.add(statusItem);  // Add status item if not already in the list
                    }
                }

                // Update adapter with the merged list
                userAdapter.listItem = combinedList;
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserNoti.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


     /*  RecyclerView recyclerView = findViewById(R.id.recycler_view);

// Set up LayoutManagers
        recyclerView.setLayoutManager(new LinearLayoutManager(UserNoti.this));

// Initialize adapters
        UserNotiMaintainanceAdapter userAdapter = new UserNotiMaintainanceAdapter(new LinkedList<UserNotiClass>(), UserNoti.this);
// Set adapters to RecyclerViews
        recyclerView.setAdapter(userAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Notification");

        // Query 1: Filter by userID
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        Query queryUser = myRef.orderByChild("userID").equalTo(userID);
        queryUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinkedList<UserNotiClass> userList = new LinkedList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserNotiClass classRef = dataSnapshot.getValue(UserNotiClass.class);
                    if (classRef != null) {
                        userList.add(classRef);
                    }
                }

                // Update the first adapter
                userAdapter.listItem = userList;
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserNoti.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Query 2: Filter by status
        String status = "Under Maintainance";
        Query queryStatus = myRef.orderByChild("status").equalTo(status);
        queryStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinkedList<UserNotiClass> statusList = new LinkedList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserNotiClass classRef = dataSnapshot.getValue(UserNotiClass.class);
                    if (classRef != null) {
                        statusList.add(classRef);
                    }
                }

                // Update the second adapter
                userAdapter.listItem = statusList;
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserNoti.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */


       /* RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UserNoti.this));
        UserNotiAdapter mAdapter = new UserNotiAdapter(new LinkedList<UserNotiClass>(), UserNoti.this);
        mRecyclerView.setAdapter(mAdapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Notification");
        Query query = myRef.orderByChild("userID").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinkedList<UserNotiClass> listData = new LinkedList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserNotiClass classRef = dataSnapshot.getValue(UserNotiClass.class);
                    listData.add(classRef);
                }
                mAdapter.listItem = listData;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(UserNoti.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        String status = "Under Maintainance";

        DatabaseReference myRef1 = database.getReference().child("Notification");
        Query query1 = myRef1.orderByChild("status").equalTo(status);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LinkedList<UserNotiClass> listData = new LinkedList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserNotiClass classRef = dataSnapshot.getValue(UserNotiClass.class);
                    listData.add(classRef);
                }
                mAdapter.listItem = listData;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(UserNoti.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/



    }

    @Override
    public void onBackPressed() {
        // Do something when back button is pressed
        // You can call finish() to close the current activity
        super.onBackPressed();
    }
}