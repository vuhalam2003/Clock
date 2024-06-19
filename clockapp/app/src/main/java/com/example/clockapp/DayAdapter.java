package com.example.clockapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private List<String> daysOfWeek;
    private boolean[] checkedDays;

    public DayAdapter(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        this.checkedDays = new boolean[daysOfWeek.size()];
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.checkBoxDay.setText(daysOfWeek.get(position));
        holder.checkBoxDay.setChecked(checkedDays[position]);
        holder.checkBoxDay.setOnCheckedChangeListener((buttonView, isChecked) -> checkedDays[position] = isChecked);
    }

    @Override
    public int getItemCount() {
        return daysOfWeek.size();
    }

    public boolean[] getCheckedDays() {
        return checkedDays;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxDay;

        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxDay = itemView.findViewById(R.id.checkBoxDay);
        }
    }
}
