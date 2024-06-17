package com.example.clockapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private ArrayList<Alarm> alarmList;

    public AlarmAdapter(ArrayList<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.alarmLabel.setText(alarm.getLabel());
        holder.alarmTime.setText(String.format("%02d:%02d %s", alarm.getHour(), alarm.getMinute(), alarm.getPeriod()));
        holder.alarmDays.setText(alarm.getDays());
        holder.alarmSwitch.setChecked(alarm.isEnabled());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView alarmLabel, alarmTime, alarmDays;
        Switch alarmSwitch;
        CardView cardView;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmLabel = itemView.findViewById(R.id.alarmLabel);
            alarmTime = itemView.findViewById(R.id.alarmTime);
            alarmDays = itemView.findViewById(R.id.alarmDays);
            alarmSwitch = itemView.findViewById(R.id.alarmSwitch);
        }
    }
}
