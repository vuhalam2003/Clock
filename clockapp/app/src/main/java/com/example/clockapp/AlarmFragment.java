package com.example.clockapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener;
public class AlarmFragment extends Fragment implements AlarmDialogFragment.AlarmDialogListener {

    private FloatingActionButton addAlarmBtn;
    private int hour, minute;
    private AlarmManager alarmManager;
    private RecyclerView alarmRecyclerView;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;
    private AlarmRepository alarmRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        addAlarmBtn = view.findViewById(R.id.addAlarmBtn);
        alarmRecyclerView = view.findViewById(R.id.alarmRecyclerView);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        alarmRepository = new AlarmRepository(getActivity());
        alarmList = new ArrayList<>(alarmRepository.getAlarms());

        alarmAdapter = new AlarmAdapter(alarmList, getContext(), new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alarm alarm) {
                showEditAlarmDialog(alarm);
            }
        });
        alarmRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        alarmRecyclerView.setAdapter(alarmAdapter);

        addAlarmBtn.setOnClickListener(v -> showTimePickerDialog());

        return view;
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minuteOfHour) -> {
            hour = hourOfDay;
            minute = minuteOfHour;
            showAlarmDialog();
        }, currentHour, currentMinute, false);

        timePickerDialog.show();
    }

    private void showAlarmDialog() {
        AlarmDialogFragment dialog = new AlarmDialogFragment();
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), "AlarmDialogFragment");
    }

    private void showEditAlarmDialog(Alarm alarm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        builder.setTitle("Edit Alarm");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_alarm, null);
        EditText editTextLabel = dialogView.findViewById(R.id.editTextLabel);
        CheckBox checkBoxSun = dialogView.findViewById(R.id.checkBoxSun);
        CheckBox checkBoxMon = dialogView.findViewById(R.id.checkBoxMon);
        CheckBox checkBoxTue = dialogView.findViewById(R.id.checkBoxTue);
        CheckBox checkBoxWed = dialogView.findViewById(R.id.checkBoxWed);
        CheckBox checkBoxThu = dialogView.findViewById(R.id.checkBoxThu);
        CheckBox checkBoxFri = dialogView.findViewById(R.id.checkBoxFri);
        CheckBox checkBoxSat = dialogView.findViewById(R.id.checkBoxSat);

        editTextLabel.setText(alarm.getLabel());
        checkBoxSun.setChecked(alarm.isRepeatSun());
        checkBoxMon.setChecked(alarm.isRepeatMon());
        checkBoxTue.setChecked(alarm.isRepeatTue());
        checkBoxWed.setChecked(alarm.isRepeatWed());
        checkBoxThu.setChecked(alarm.isRepeatThu());
        checkBoxFri.setChecked(alarm.isRepeatFri());
        checkBoxSat.setChecked(alarm.isRepeatSat());

        builder.setView(dialogView);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String label = editTextLabel.getText().toString();
            boolean[] repeatDays = new boolean[7];
            repeatDays[0] = checkBoxSun.isChecked();
            repeatDays[1] = checkBoxMon.isChecked();
            repeatDays[2] = checkBoxTue.isChecked();
            repeatDays[3] = checkBoxWed.isChecked();
            repeatDays[4] = checkBoxThu.isChecked();
            repeatDays[5] = checkBoxFri.isChecked();
            repeatDays[6] = checkBoxSat.isChecked();

            alarm.setLabel(label);
            alarm.setRepeatSun(repeatDays[0]);
            alarm.setRepeatMon(repeatDays[1]);
            alarm.setRepeatTue(repeatDays[2]);
            alarm.setRepeatWed(repeatDays[3]);
            alarm.setRepeatThu(repeatDays[4]);
            alarm.setRepeatFri(repeatDays[5]);
            alarm.setRepeatSat(repeatDays[6]);

            alarmRepository.updateAlarm(alarm);
            alarmAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Delete", (dialog, which) -> {
            alarmRepository.deleteAlarm(alarm);
            alarmList.remove(alarm);
            alarmAdapter.notifyDataSetChanged();
        });
        builder.setNeutralButton("Cancel", null);

        builder.create().show();
    }

    @Override
    public void onDialogPositiveClick(Alarm alarm, String label, boolean[] repeatDays) {
        if (alarm == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), alarmList.size(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                Alarm newAlarm = new Alarm(hour, minute, label, true, repeatDays[0], repeatDays[1], repeatDays[2], repeatDays[3], repeatDays[4], repeatDays[5], repeatDays[6]);
                alarmRepository.addAlarm(newAlarm);
                alarmList.add(newAlarm);
                alarmAdapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Alarm set for " + String.format("%02d:%02d", hour, minute), Toast.LENGTH_SHORT).show();
            }
        } else {
            alarm.setLabel(label);
            alarm.setRepeatSun(repeatDays[0]);
            alarm.setRepeatMon(repeatDays[1]);
            alarm.setRepeatTue(repeatDays[2]);
            alarm.setRepeatWed(repeatDays[3]);
            alarm.setRepeatThu(repeatDays[4]);
            alarm.setRepeatFri(repeatDays[5]);
            alarm.setRepeatSat(repeatDays[6]);

            alarmRepository.updateAlarm(alarm);
            alarmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogDeleteClick(Alarm alarm) {
        alarmRepository.deleteAlarm(alarm);
        alarmList.remove(alarm);
        alarmAdapter.notifyDataSetChanged();
    }
}
