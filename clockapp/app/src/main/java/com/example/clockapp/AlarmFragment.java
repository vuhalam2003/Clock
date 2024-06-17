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
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.text.TextUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmFragment extends Fragment {

    private FloatingActionButton addAlarmBtn;
    private LinearLayout addAlarmDetailsLayout;
    private EditText editTextLabel;
    private MultiAutoCompleteTextView multiAutoCompleteTextViewDays;
    private Button buttonSaveAlarm;
    private int hour, minute;
    private AlarmManager alarmManager;
    private RecyclerView alarmRecyclerView;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        addAlarmBtn = view.findViewById(R.id.addAlarmBtn);
        addAlarmDetailsLayout = view.findViewById(R.id.addAlarmDetailsLayout);
        editTextLabel = view.findViewById(R.id.editTextLabel);
        multiAutoCompleteTextViewDays = view.findViewById(R.id.multiAutoCompleteTextViewDays);
        buttonSaveAlarm = view.findViewById(R.id.buttonSaveAlarm);
        alarmRecyclerView = view.findViewById(R.id.alarmRecyclerView);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(alarmList);
        alarmRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        alarmRecyclerView.setAdapter(alarmAdapter);

        addAlarmBtn.setOnClickListener(v -> {
            showAlarmDetailsLayout();
            showTimePickerDialog();
        });

        return view;
    }

    private void showAlarmDetailsLayout() {
        addAlarmBtn.setVisibility(View.GONE);
        addAlarmDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void hideAlarmDetailsLayout() {
        addAlarmBtn.setVisibility(View.VISIBLE);
        addAlarmDetailsLayout.setVisibility(View.GONE);
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minuteOfHour) -> {
            hour = hourOfDay;
            minute = minuteOfHour;
            // setAlarm(hour, minute); // Do not set alarm immediately, wait for user to save
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    public void saveAlarm(View view) {
        String label = editTextLabel.getText().toString().trim();
        String days = multiAutoCompleteTextViewDays.getText().toString().trim();

        if (label.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter an alarm label", Toast.LENGTH_SHORT).show();
            return;
        }

        if (days.isEmpty()) {
            Toast.makeText(getActivity(), "Please select repeat days", Toast.LENGTH_SHORT).show();
            return;
        }

        // Split days input by comma and trim spaces
        String[] daysArray = days.split(",");
        for (int i = 0; i < daysArray.length; i++) {
            daysArray[i] = daysArray[i].trim();
        }

        // Join days array to a single string with spaces
        String formattedDays = TextUtils.join(" ", daysArray);

        // Add the alarm to the list
        alarmList.add(new Alarm(hour, minute, label, formattedDays, true));  // Add label, days, and switch state as needed
        alarmAdapter.notifyDataSetChanged();

        // Optionally, set alarm here if needed

        Toast.makeText(getActivity(), "Alarm set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();

        // Hide the alarm details layout after saving
        hideAlarmDetailsLayout();
    }
}
