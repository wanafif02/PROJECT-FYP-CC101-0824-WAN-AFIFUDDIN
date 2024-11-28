package com.example.gymmobileapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class AdminScheduleAdapter extends RecyclerView.Adapter<AdminScheduleAdapter.ViewHolder> {

    LinkedList<AdminScheduleClass> listItem;
    private Context context;

    public AdminScheduleAdapter(LinkedList<AdminScheduleClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_gym_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AdminScheduleClass model = listItem.get(position);

        holder.timeGym.setText(model.getTimeGym());

        if ("Available".equals(model.getStatusTime())) {
            holder.maintainance.setVisibility(View.VISIBLE);
            holder.maintainanceSettel.setVisibility(View.GONE);
        }
        else {
            holder.maintainance.setVisibility(View.GONE);
            holder.maintainanceSettel.setVisibility(View.VISIBLE);
        }

        holder.maintainance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Schedule Gym").child(model.getTimeID());
                ref.child("statusTime").setValue("Under Maintenance")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, model.getTimeGym() + " - Under Maintenance", Toast.LENGTH_SHORT).show();

                               /* FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference userRef = database.getReference().child("Notification");
                                String notiID = userRef.push().getKey();
                                String timeID = model.getTimeID();
                                String timeGym = model.getTimeGym();

                                userRef.child(notiID).child("notiID").setValue(notiID);
                                userRef.child(notiID).child("timeID").setValue(timeID);
                                userRef.child(notiID).child("timeGym").setValue(timeGym);
                                userRef.child(notiID).child("statusTime").setValue("Under Maintainance"); */
                                String selectedTime = model.getTimeGym();
                                String statusTime = "Under Maintenance";
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference classRef = database.getReference().child("Notification");
                                String notiID = classRef.push().getKey();
                                classRef.child(notiID).child("notiID").setValue(notiID);
                                classRef.child(notiID).child("selectedMaintenance").setValue(selectedTime);
                                classRef.child(notiID).child("status").setValue(statusTime);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to Change The Schedule", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        holder.maintainanceSettel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("Schedule Gym").child(model.getTimeID());
                ref.child("statusTime").setValue("Available")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, model.getTimeGym() + " - Available", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to Change The Schedule", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeGym;
        ImageView maintainance, maintainanceSettel;
       // EditText dateGym;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            maintainance = itemView.findViewById(R.id.imgmaintainance);
            maintainanceSettel = itemView.findViewById(R.id.imgmaintainancex);
            timeGym = itemView.findViewById(R.id.timeGym);
           // dateGym = itemView.findViewById(R.id.dateGym);

        }
    }
}
