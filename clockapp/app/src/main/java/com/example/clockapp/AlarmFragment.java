package com.example.clockapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmFragment extends Fragment {

    private TextView selectedTimeTextView;
    private Button selectTimeBtn, setAlarmBtn, cancelAlarmBtn;
    private int hour, minute;
    private AlarmManager alarmManager;
    private ListView alarmListView;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        selectedTimeTextView = view.findViewById(R.id.selectedTime);
        selectTimeBtn = view.findViewById(R.id.selectTimeBtn);
        setAlarmBtn = view.findViewById(R.id.setAlarmBtn);
        cancelAlarmBtn = view.findViewById(R.id.cancelAlarmBtn);
        alarmListView = view.findViewById(R.id.alarmListView);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(getActivity(), alarmList);
        alarmListView.setAdapter(alarmAdapter);

        selectTimeBtn.setOnClickListener(v -> showTimePickerDialog());
        setAlarmBtn.setOnClickListener(v -> setAlarm());
        cancelAlarmBtn.setOnClickListener(v -> cancelAllAlarms());

        return view;
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minuteOfHour) -> {
            hour = hourOfDay;
            minute = minuteOfHour;
            selectedTimeTextView.setText(String.format("%02d : %02d", hour, minute));
        }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), alarmList.size(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            alarmList.add(new Alarm(hour, minute));
            alarmAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "Alarm set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAllAlarms() {
        if (alarmManager != null) {
            for (int i = 0; i < alarmList.size(); i++) {
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
            alarmList.clear();
            alarmAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "All alarms canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
