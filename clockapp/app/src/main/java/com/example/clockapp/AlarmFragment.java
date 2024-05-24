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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class AlarmFragment extends Fragment {

    private TextView selectedTimeTextView;
    private Button selectTimeBtn, setAlarmBtn, cancelAlarmBtn;
    private int hour, minute;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        selectedTimeTextView = view.findViewById(R.id.selectedTime);
        selectTimeBtn = view.findViewById(R.id.selectTimeBtn);
        setAlarmBtn = view.findViewById(R.id.setAlarmBtn);
        cancelAlarmBtn = view.findViewById(R.id.cancelAlarmBtn);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        selectTimeBtn.setOnClickListener(v -> showTimePickerDialog());
        setAlarmBtn.setOnClickListener(v -> setAlarm());
        cancelAlarmBtn.setOnClickListener(v -> cancelAlarm());

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
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getActivity(), "Alarm set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAlarm() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(getActivity(), "Alarm canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
