package com.example.gymmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class AdminBookingAdapter extends RecyclerView.Adapter<AdminBookingAdapter.ViewHolder> {

    LinkedList<UserGymClass> listItem;
    private Context context;

    public AdminBookingAdapter(LinkedList<UserGymClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserGymClass model = listItem.get(position);

        holder.dateGym.setText(model.getDateGym());
        holder.timeGym.setText(model.getTimeGym());
        holder.unitUser.setText(model.getUnitUser());
        holder.statusGym.setText(model.getStatusGym());

        if ("Booking".equals(model.getStatusGym())) {
            holder.imgcancel.setVisibility(View.VISIBLE);
        }
        else {
            holder.imgcancel.setVisibility(View.GONE);
        }

        holder.imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference().child("List Resident Booking");
                bookingRef.child(model.getGymID()).child("statusGym").setValue("Booking Has Been Cancel By Admin")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Booking Has Been Cancel By Admin", Toast.LENGTH_SHORT).show();

                                String userID = model.getUserID();
                                String gymID = model.getGymID();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference userRef = database.getReference().child("Notification");
                                String notiID = userRef.push().getKey();
                                UserNotiClass classRef2 = new UserNotiClass(userID, gymID, notiID);
                                userRef.child(notiID).setValue(classRef2);
                                userRef.child(notiID).child("statusGym").setValue("Booking Has Been Cancel By Admin");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to Reject", Toast.LENGTH_SHORT).show();
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

        TextView dateGym, timeGym, unitUser, statusGym;
        ImageView imgcancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateGym = itemView.findViewById(R.id.dateGym);
            timeGym = itemView.findViewById(R.id.timeGym);
            unitUser = itemView.findViewById(R.id.unitUser);
            imgcancel = itemView.findViewById(R.id.imgcancel);
            statusGym = itemView.findViewById(R.id.statusGym);

        }
    }
}
