package com.example.clockapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;

public class StopWatchFragment extends Fragment {

    private TextView tvStopwatch;
    private Button btnStartStop;
    private Button btnLap;
    private ListView lvLaps;
    private Button btnReset;


    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;
    private ArrayList<String> lapTimes;
    private ArrayAdapter<String> lapsAdapter;

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (millis % 1000);

            tvStopwatch.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 10);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);

        tvStopwatch = view.findViewById(R.id.tvStopwatch);
        btnStartStop = view.findViewById(R.id.btnStartStop);
        btnLap = view.findViewById(R.id.btnLap);
        lvLaps = view.findViewById(R.id.lvLaps);
        btnReset = view.findViewById(R.id.btnReset);


        lapTimes = new ArrayList<>();
        lapsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, lapTimes);
        lvLaps.setAdapter(lapsAdapter);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    btnStartStop.setText("Start");
                    btnStartStop.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
                    stopStopwatch();
                } else {
                    btnStartStop.setText("Stop");
                    btnStartStop.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
                    startStopwatch();
                }
                isRunning = !isRunning;
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    lapTimes.add(tvStopwatch.getText().toString());
                    lapsAdapter.notifyDataSetChanged();
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStopwatch();
            }
        });

        return view;
    }


    private void startStopwatch() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 0);
    }

    private void stopStopwatch() {
        handler.removeCallbacks(updateTimer);
    }
    private void resetStopwatch() {
        stopStopwatch();
        isRunning = false;
        tvStopwatch.setText("00:00:000");
        lapTimes.clear();
        lapsAdapter.notifyDataSetChanged();
    }
}
