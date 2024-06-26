package com.example.clockapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private ArrayList<Alarm> alarmList;
    private OnItemClickListener listener;
    private Context context;
    private AlarmManager alarmManager;

    public interface OnItemClickListener {
        void onItemClick(Alarm alarm);
    }

    public AlarmAdapter(ArrayList<Alarm> alarmList, Context context, OnItemClickListener listener) {
        this.alarmList = alarmList;
        this.context = context;
        this.listener = listener;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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
        holder.alarmTime.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));
        holder.alarmPeriod.setText(alarm.getPeriod());
        holder.alarmDays.setText(Html.fromHtml(getFormattedDays(alarm)));
        holder.alarmSwitch.setChecked(alarm.isEnabled());
        holder.bind(alarm, listener);

        holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.setEnabled(isChecked);
            if (isChecked) {
                setAlarm(alarm);
            } else {
                cancelAlarm(alarm);
            }
            // Update alarm in repository
            AlarmRepository alarmRepository = new AlarmRepository(context);
            alarmRepository.updateAlarm(alarm);
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView alarmLabel, alarmTime, alarmPeriod, alarmDays;
        Switch alarmSwitch;
        CardView cardView;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmLabel = itemView.findViewById(R.id.alarmLabel);
            alarmTime = itemView.findViewById(R.id.alarmTime);
            alarmPeriod = itemView.findViewById(R.id.alarmPeriod);
            alarmDays = itemView.findViewById(R.id.alarmDays);
            alarmSwitch = itemView.findViewById(R.id.alarmSwitch);
        }

        public void bind(final Alarm alarm, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(alarm);
                }
            });
        }
    }

    private String getFormattedDays(Alarm alarm) {
        StringBuilder days = new StringBuilder();
        if (alarm.isRepeatSun()) days.append("<font color='#F0F757'>S</font> ");
        else days.append("S ");
        if (alarm.isRepeatMon()) days.append("<font color='#F0F757'>M</font> ");
        else days.append("M ");
        if (alarm.isRepeatTue()) days.append("<font color='#F0F757'>T</font> ");
        else days.append("T ");
        if (alarm.isRepeatWed()) days.append("<font color='#F0F757'>W</font> ");
        else days.append("W ");
        if (alarm.isRepeatThu()) days.append("<font color='#F0F757'>T</font> ");
        else days.append("T ");
        if (alarm.isRepeatFri()) days.append("<font color='#F0F757'>F</font> ");
        else days.append("F ");
        if (alarm.isRepeatSat()) days.append("<font color='#F0F757'>S</font> ");
        else days.append("S ");

        String daysString = days.toString().trim();
        if (daysString.isEmpty()) {
            daysString = "Everyday";
        }
        return daysString;
    }

    private void setAlarm(Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
