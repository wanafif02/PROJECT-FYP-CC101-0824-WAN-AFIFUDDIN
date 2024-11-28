package com.example.gymmobileapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class UserGymTimeAdapter extends RecyclerView.Adapter<UserGymTimeAdapter.ViewHolder> {

    LinkedList<UserGymTimeClass> listItem;
    private Context context;

    public UserGymTimeAdapter(LinkedList<UserGymTimeClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_gym_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserGymTimeClass model = listItem.get(position);

        holder.timeGym.setText(model.getTimeGym());
        holder.timeGymMaintainance.setText(model.getTimeGym());

        int remainingPax1 = model.getRemainingPax(); // Calculate remaining spots
        // Determine the display based on the remaining pax
        if (remainingPax1 > 15) {
            // Display the total limit if remaining exceeds 15
            holder.totalPaxGym.setText("15 pax limit");
        } else if (remainingPax1 > 0) {
            // Display the remaining spots if within the limit of 15
            holder.totalPaxGym.setText(remainingPax1 + " (pax left)");
        } else {
            // Notify if no spots are available
            holder.totalPaxGym.setText(remainingPax1 + " (pax left)");
        }

        // In your ViewHolder, ensure to set the OnClickListener
        holder.itemView.setOnClickListener(v -> {

            if ("Under Maintainance".equals(model.getStatusTime())) {
                Toast.makeText(context, "This time slot is under maintenance and cannot be booked.", Toast.LENGTH_SHORT).show();
                holder.undermaintainance.setVisibility(View.VISIBLE);
                holder.timeavailable.setVisibility(View.GONE);

            } else {
                holder.undermaintainance.setVisibility(View.GONE);
                holder.timeavailable.setVisibility(View.VISIBLE);
                // Check if remaining pax is valid (greater than 0 and less than or equal to 15)
                if (remainingPax1 > 0 && remainingPax1 <= 15) {

                    // Get the current time in 12-hour format with AM/PM
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // Format to include AM/PM
                    String currentTimeStr = sdf.format(new Date());

                    // Get the time range from the list item
                    String timeGym = model.getTimeGym(); // This should be something like "8:00 AM - 9:00 AM"

                    // Split the timeGym into start and end times
                    String[] timeRange = timeGym.split(" - ");
                    String startTimeStr = timeRange[0]; // e.g., "8:00 AM"
                    String endTimeStr = timeRange[1];   // e.g., "9:00 AM"

                    try {
                        // Parse the start and end times into Date objects
                        Date startTime = sdf.parse(startTimeStr);
                        Date endTime = sdf.parse(endTimeStr);
                        Date currentTime = sdf.parse(currentTimeStr); // Current time as Date object

                        // Check if the current time is within the range
                        if (currentTime.after(startTime) && currentTime.before(endTime)) {
                            // If the current time is within the range, perform the action
                            Toast.makeText(context, "You can choose this slot!", Toast.LENGTH_SHORT).show();

                            // Navigate to the booking screen (UserGym) with time details
                            Intent intent = new Intent(context, UserGym.class);
                            intent.putExtra("timeID", model.getTimeID());
                            intent.putExtra("timeGym", model.getTimeGym());
                            context.startActivity(intent);

                        } else if (currentTime.after(endTime)  ||  currentTime.before(endTime)) {
                            // If the current time is within the range, perform the action
                            Toast.makeText(context, "You can choose this slot!", Toast.LENGTH_SHORT).show();
                            // Navigate to the booking screen (UserGym) with time details
                            Intent intent = new Intent(context, UserGym.class);
                            intent.putExtra("timeID", model.getTimeID());
                            intent.putExtra("timeGym", model.getTimeGym());
                            context.startActivity(intent);

                        }
                        else {
                            // If the current time is not within the range, notify the user
                            Toast.makeText(context, "Cannot choose, time is not within range", Toast.LENGTH_SHORT).show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace(); // Handle any parsing errors
                        Toast.makeText(context, "Error occurred while parsing time", Toast.LENGTH_SHORT).show();
                    }

                } else if (remainingPax1 <= 0) {
                    // If there are no available pax left, inform the user
                    Toast.makeText(context, "Booking limit reached for today. No more bookings allowed.", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle case where remainingPax1 > 15, if required
                    Toast.makeText(context, "Booking limit exceeded for this slot.", Toast.LENGTH_SHORT).show();
                }
            }


        });

       /* DONT DELTE // In your ViewHolder, ensure to set the OnClickListener
        holder.itemView.setOnClickListener(v -> {

                // Maximum pax limit for a day
                int maxPaxPerDay = 15;

                if (model.getRemainingPax() <= maxPaxPerDay) {

            // Get the current time in 12-hour format with AM/PM
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // Format to include AM/PM
            String currentTimeStr = sdf.format(new Date());

            // Get the time range from the list item
            String timeGym = model.getTimeGym(); // This should be something like "8:00 AM - 9:00 AM"

            // Split the timeGym into start and end times
            String[] timeRange = timeGym.split(" - ");
            String startTimeStr = timeRange[0]; // e.g., "8:00 AM"
            String endTimeStr = timeRange[1];   // e.g., "9:00 AM"

            try {
                // Parse the start and end times into Date objects
                Date startTime = sdf.parse(startTimeStr);
                Date endTime = sdf.parse(endTimeStr);
                Date currentTime = sdf.parse(currentTimeStr); // Current time as Date object
                // Check if the current time is between startTime and endTime
                if (currentTime.after(startTime) && currentTime.before(endTime)) {
                    // If the current time is within the range, perform the action
                    Toast.makeText(context, "You can choose", Toast.LENGTH_SHORT).show();
                    // Perform the action for the item (e.g., navigate to UserProfile)
                    Intent intent = new Intent(context, UserGym.class);
                    intent.putExtra("timeID", model.getTimeID());
                    intent.putExtra("timeGym", model.getTimeGym());
                    context.startActivity(intent);

                } else if (currentTime.before(endTime) || currentTime.equals(endTime)) {
                    // If the current time is within the range, perform the action
                    Toast.makeText(context, "You can choose", Toast.LENGTH_SHORT).show();
                    // Perform the action for the item (e.g., navigate to UserProfile)
                    Intent intent = new Intent(context, UserGym.class);
                    intent.putExtra("timeID", model.getTimeID());
                    intent.putExtra("timeGym", model.getTimeGym());
                    context.startActivity(intent);
                } else {
                    // If the current time is not within the range, notify the user
                    Toast.makeText(context, "Cannot choose, time is not within range", Toast.LENGTH_SHORT).show();
                }

            } catch (ParseException e) {
                e.printStackTrace(); // Handle any parsing errors
                Toast.makeText(context, "Error occurred while parsing time", Toast.LENGTH_SHORT).show();
            }

                } else {
                    Toast.makeText(context, "Booking limit reached for today. No more bookings allowed.", Toast.LENGTH_SHORT).show();
                }
        }); */

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView totalPaxGym, timeGym, timeGymMaintainance;
        RelativeLayout undermaintainance, timeavailable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            totalPaxGym = itemView.findViewById(R.id.totalPaxGym);
            timeGym = itemView.findViewById(R.id.timeGym);
            timeGymMaintainance = itemView.findViewById(R.id.timeGymMaintainance);
            undermaintainance = itemView.findViewById(R.id.undermaintainance);
            timeavailable = itemView.findViewById(R.id.timeavailable);

        }
    }
}
