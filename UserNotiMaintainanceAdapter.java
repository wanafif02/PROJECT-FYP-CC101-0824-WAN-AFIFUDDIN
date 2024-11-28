package com.example.gymmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class UserNotiMaintainanceAdapter extends RecyclerView.Adapter<UserNotiMaintainanceAdapter.ViewHolder> {

    LinkedList<UserNotiClass> listItem;
    private Context context;

    public UserNotiMaintainanceAdapter(LinkedList<UserNotiClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserNotiClass item = listItem.get(position);

       /* holder.date_time.setText("We apologize for the inconvenience, the "
                + model.getSelectedMaintaince() +" Under Maintaince"); */
        // Handle booking cancellation
        if ("Booking Has Been Cancel By Admin".equals(item.getStatusGym())) {
            holder.title.setText("Booking Cancelled");
            holder.date_time.setText("Your booking has been canceled. We apologize for the inconvenience.");
        }
        // Handle under maintenance
        else if ("Under Maintenance".equals(item.getStatus())) {
            holder.title.setText("Under Maintenance");
            holder.date_time.setText("We apologize for the inconvenience. The service is under maintenance. Date: " + item.getSelectedMaintenance());
        }

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date_time = itemView.findViewById(R.id.date_time);
        }
    }
}
