package com.example.clockapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class StopWatchFragment extends Fragment {

    private TextView tvStopwatch;
    private Button btnStartStop, btnLapReset;
    private ListView lvLaps;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private long startTime = 0;
    private ArrayList<String> laps;
    private LapAdapter lapAdapter;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            millis = millis % 1000;

            tvStopwatch.setText(String.format("%02d : %02d : %03d", minutes, seconds, millis));
            handler.postDelayed(this, 10);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);

        tvStopwatch = view.findViewById(R.id.tvStopwatch);
        btnStartStop = view.findViewById(R.id.btnStartStop);
        btnLapReset = view.findViewById(R.id.btnLapReset);
        lvLaps = view.findViewById(R.id.lvLaps);

        laps = new ArrayList<>();
        lapAdapter = new LapAdapter(getActivity(), laps);
        lvLaps.setAdapter(lapAdapter);

        btnStartStop.setOnClickListener(v -> {
            if (isRunning) {
                stopStopwatch();
            } else {
                startStopwatch();
            }
        });

        btnLapReset.setOnClickListener(v -> {
            if (isRunning) {
                addLap();
            } else {
                resetStopwatch();
            }
        });

        return view;
    }

    private void startStopwatch() {
        isRunning = true;
        startTime = System.currentTimeMillis();
        handler.post(runnable);
        btnStartStop.setText("Stop");
        btnStartStop.setBackgroundColor(Color.RED);
        btnLapReset.setText("Lap");
        btnLapReset.setBackgroundColor(Color.parseColor("#34344A"));
    }

    private void stopStopwatch() {
        isRunning = false;
        handler.removeCallbacks(runnable);
        btnStartStop.setText("Start");
        btnStartStop.setBackgroundColor(Color.parseColor("#F0F757"));
        btnLapReset.setText("Reset");
        btnLapReset.setBackgroundColor(Color.parseColor("#34344A"));
    }

    private void resetStopwatch() {
        tvStopwatch.setText("00 : 00.000");
        laps.clear();
        lapAdapter.notifyDataSetChanged();
    }

    private void addLap() {
        String currentTime = tvStopwatch.getText().toString();
        laps.add(currentTime);
        lapAdapter.notifyDataSetChanged();
    }
}
