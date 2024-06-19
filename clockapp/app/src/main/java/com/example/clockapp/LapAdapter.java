package com.example.clockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LapAdapter extends ArrayAdapter<String> {

    public LapAdapter(Context context, ArrayList<String> laps) {
        super(context, 0, laps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String lap = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lap_item, parent, false);
        }

        TextView tvLapNumber = convertView.findViewById(R.id.tvLapNumber);
        TextView tvLapTime = convertView.findViewById(R.id.tvLapTime);

        tvLapNumber.setText("Lap " + (position + 1));
        tvLapTime.setText(lap);

        return convertView;
    }
}
