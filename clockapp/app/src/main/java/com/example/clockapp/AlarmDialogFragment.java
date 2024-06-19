package com.example.clockapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlarmDialogFragment extends DialogFragment {

    private EditText editTextLabel;
    private CheckBox checkBoxSun, checkBoxMon, checkBoxTue, checkBoxWed, checkBoxThu, checkBoxFri, checkBoxSat;
    private AlarmDialogListener listener;
    private Alarm alarm;

    public interface AlarmDialogListener {
        void onDialogPositiveClick(Alarm alarm, String label, boolean[] repeatDays);
        void onDialogDeleteClick(Alarm alarm);
    }

    public void setListener(AlarmDialogListener listener) {
        this.listener = listener;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alarm, null);

        editTextLabel = view.findViewById(R.id.editTextLabel);
        checkBoxSun = view.findViewById(R.id.checkBoxSun);
        checkBoxMon = view.findViewById(R.id.checkBoxMon);
        checkBoxTue = view.findViewById(R.id.checkBoxTue);
        checkBoxWed = view.findViewById(R.id.checkBoxWed);
        checkBoxThu = view.findViewById(R.id.checkBoxThu);
        checkBoxFri = view.findViewById(R.id.checkBoxFri);
        checkBoxSat = view.findViewById(R.id.checkBoxSat);

        if (alarm != null) {
            editTextLabel.setText(alarm.getLabel());
            checkBoxSun.setChecked(alarm.isRepeatSun());
            checkBoxMon.setChecked(alarm.isRepeatMon());
            checkBoxTue.setChecked(alarm.isRepeatTue());
            checkBoxWed.setChecked(alarm.isRepeatWed());
            checkBoxThu.setChecked(alarm.isRepeatThu());
            checkBoxFri.setChecked(alarm.isRepeatFri());
            checkBoxSat.setChecked(alarm.isRepeatSat());
        }

        builder.setView(view)
                .setTitle(alarm == null ? "Set Alarm Details" : "Edit Alarm Details")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String label = editTextLabel.getText().toString();
                        boolean[] repeatDays = new boolean[7];
                        repeatDays[0] = checkBoxSun.isChecked();
                        repeatDays[1] = checkBoxMon.isChecked();
                        repeatDays[2] = checkBoxTue.isChecked();
                        repeatDays[3] = checkBoxWed.isChecked();
                        repeatDays[4] = checkBoxThu.isChecked();
                        repeatDays[5] = checkBoxFri.isChecked();
                        repeatDays[6] = checkBoxSat.isChecked();
                        listener.onDialogPositiveClick(alarm, label, repeatDays);
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (alarm != null) {
                            listener.onDialogDeleteClick(alarm);
                        }
                    }
                });

        return builder.create();
    }
}
