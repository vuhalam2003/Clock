package com.example.clockapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public AlarmAdapter(Context context, List<Alarm> alarms) {
        super(context, 0, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alarm alarm = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
        }

        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        timeTextView.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));

        return convertView;
    }
}
