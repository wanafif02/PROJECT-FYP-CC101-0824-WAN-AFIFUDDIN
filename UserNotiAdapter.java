package com.example.gymmobileapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class UserNotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LinkedList<UserNotiClass> listItem;
    private Context context;
    private static final int VIEW_TYPE_BOOKING_CANCEL = 0;
    private static final int VIEW_TYPE_UNDER_MAINTENANCE = 1;

    public UserNotiAdapter(LinkedList<UserNotiClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* if (viewType == VIEW_TYPE_BOOKING_CANCEL) {
            View view = LayoutInflater.from(context).inflate(R.layout.user_notification, parent, false);
            return new BookingCancelViewHolder(view);
        } else if (viewType == VIEW_TYPE_UNDER_MAINTENANCE) {
            View view = LayoutInflater.from(context).inflate(R.layout.user_notification_maintainance, parent, false);
            return new MaintenanceViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type"); */

        View view = LayoutInflater.from(context).inflate(R.layout.user_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*UserNotiClass item = listItem.get(position);

        /*if (holder instanceof BookingCancelViewHolder) {
            ((BookingCancelViewHolder) holder).bindData(item);
        } else if (holder instanceof MaintenanceViewHolder) {
            ((MaintenanceViewHolder) holder).bindData(item);
        } */
        UserNotiClass item = listItem.get(position);
        if (holder instanceof NotificationViewHolder) {
            ((NotificationViewHolder) holder).bindData(item);
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    // ViewHolder for both notification types
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title, date_time;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date_time = itemView.findViewById(R.id.date_time);
        }

        void bindData(UserNotiClass item) {
            // Handle booking cancellation
            if ("Booking Has Been Cancel By Admin".equals(item.getStatusGym())) {
                title.setText("Booking Cancelled");
                date_time.setText("The booking has been cancelled by the admin.");
            }
            // Handle under maintenance
            else if ("Under Maintainance".equals(item.getStatus())) {
                title.setText("Under Maintenance");
                date_time.setText("We apologize for the inconvenience. The service is under maintenance. Date: " + item.getSelectedMaintenance());
            }
        }
    }

/*    // ViewHolder for Booking Cancel
    static class BookingCancelViewHolder extends RecyclerView.ViewHolder {

        public BookingCancelViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bindData(UserNotiClass item) {
            // Populate BookingCancelViewHolder views here
        }
    }

    // ViewHolder for Maintenance
    static class MaintenanceViewHolder extends RecyclerView.ViewHolder {
        TextView date_time;

        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date_time = itemView.findViewById(R.id.date_time);
        }

        void bindData(UserNotiClass item) {
            date_time.setText("We apologize for the inconvenience, the service is under maintenance. Date: " + item.getSelectedMaintaince());
        }
    } */
}



/*public class UserNotiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LinkedList<UserNotiClass> listItem;
    private Context context;
    private static final int VIEW_TYPE_BOOKING_CANCEL = 0;
    private static final int VIEW_TYPE_UNDER_MAINTENANCE = 1;


    public UserNotiAdapter(LinkedList<UserNotiClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the view type based on some condition in your data
        UserNotiClass item = listItem.get(position);
        if ("Under Maintainance".equals(item.getStatus())) {
            return VIEW_TYPE_UNDER_MAINTENANCE;
        } else if ("Booking Has Been Cancel By Admin".equals(item.getStatusGym())) {
            return VIEW_TYPE_BOOKING_CANCEL;
        }
        return -1; // Default view type
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_BOOKING_CANCEL) {
            View view = LayoutInflater.from(context).inflate(R.layout.user_notification, parent, false);
            return new BookingCancelViewHolder(view);
        } else if (viewType == VIEW_TYPE_UNDER_MAINTENANCE) {
            View view = LayoutInflater.from(context).inflate(R.layout.user_notification_maintainance, parent, false);
            return new MaintenanceViewHolder(view);
        }

        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserNotiClass item = listItem.get(position);

        if (holder instanceof BookingCancelViewHolder) {
            ((BookingCancelViewHolder) holder).bindData(item);
        } else if (holder instanceof MaintenanceViewHolder) {
            ((MaintenanceViewHolder) holder).bindData(item);
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    // ViewHolder for Booking Cancel
    static class BookingCancelViewHolder extends RecyclerView.ViewHolder {

        public BookingCancelViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        void bindData(UserNotiClass item) {

        }
    }

    // ViewHolder for Maintenance
    static class MaintenanceViewHolder extends RecyclerView.ViewHolder {
        TextView date_time;

        public MaintenanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date_time = itemView.findViewById(R.id.date_time);
        }

        void bindData(UserNotiClass item) {
            // Assuming item has a method getDateTime() to fetch the relevant date and time for display
            date_time.setText("We apologize for the inconvenience, the service is under maintenance. Date: " + item.getSelectedMaintaince());
        }
    }
}*/
