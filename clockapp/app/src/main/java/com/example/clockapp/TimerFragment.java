package com.example.clockapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class TimerFragment extends Fragment {

    private TimerDatabaseHelper timerDatabaseHelper;
    private RecyclerView rvTimers;
    private TimerListAdapter adapter;
    private ArrayList<TimerItem> timerItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        timerDatabaseHelper = new TimerDatabaseHelper(getContext());
        rvTimers = view.findViewById(R.id.rvTimers);
        FloatingActionButton fabAddTimer = view.findViewById(R.id.fabAddTimer);

        timerItems = timerDatabaseHelper.getAllTimers();
        adapter = new TimerListAdapter(getContext(), timerItems);
        rvTimers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTimers.setAdapter(adapter);

        fabAddTimer.setOnClickListener(v -> showAddTimerDialog());

        return view;
    }

    private void showAddTimerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_timer, null);
        builder.setView(dialogView);

        final EditText etLabel = dialogView.findViewById(R.id.etLabel);
        final NumberPicker pickerMinutes = dialogView.findViewById(R.id.pickerMinutes);
        final NumberPicker pickerSeconds = dialogView.findViewById(R.id.pickerSeconds);

        pickerMinutes.setMinValue(0);
        pickerMinutes.setMaxValue(59);
        pickerSeconds.setMinValue(0);
        pickerSeconds.setMaxValue(59);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String label = etLabel.getText().toString();
            int minutes = pickerMinutes.getValue();
            int seconds = pickerSeconds.getValue();
            long totalTime = (minutes * 60 + seconds) * 1000;

            if (!label.isEmpty() && totalTime > 0) {
                TimerItem newTimer = new TimerItem(label, totalTime, totalTime, false, false);
                timerDatabaseHelper.addTimer(newTimer);
                timerItems.add(newTimer);
                adapter.notifyItemInserted(timerItems.size() - 1);
            } else {
                Toast.makeText(getContext(), "Please enter a valid label and time.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
