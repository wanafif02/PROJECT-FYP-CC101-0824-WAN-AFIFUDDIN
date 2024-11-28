package com.example.gymmobileapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class UserUpcomingAdapter extends RecyclerView.Adapter<UserUpcomingAdapter.ViewHolder> {

    LinkedList<UserGymClass> listItem;
    private Context context;

    public UserUpcomingAdapter(LinkedList<UserGymClass> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_upcoming, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserGymClass model = listItem.get(position);

        holder.dateGym.setText(model.getDateGym());
        holder.timeGym.setText(model.getTimeGym());
        holder.statusGym.setText(model.getStatusGym());
        holder.paxGym.setText(String.valueOf(model.getPaxGym()) + " pax");


    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateGym, timeGym, paxGym, statusGym;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateGym = itemView.findViewById(R.id.dateGym);
            timeGym = itemView.findViewById(R.id.timeGym);
            paxGym = itemView.findViewById(R.id.paxGym);
            statusGym = itemView.findViewById(R.id.statusGym);

        }
    }
}
