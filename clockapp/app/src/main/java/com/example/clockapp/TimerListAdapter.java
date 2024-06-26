package com.example.clockapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TimerListAdapter extends RecyclerView.Adapter<TimerListAdapter.TimerViewHolder> {

    private final List<TimerItem> timerItems;
    private final Context context;

    public TimerListAdapter(Context context, List<TimerItem> timerItems) {
        this.context = context;
        this.timerItems = timerItems;
    }

    @NonNull
    @Override
    public TimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timer, parent, false);
        return new TimerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerViewHolder holder, int position) {
        TimerItem timerItem = timerItems.get(position);
        holder.bind(timerItem);
    }

    @Override
    public int getItemCount() {
        return timerItems.size();
    }

    class TimerViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvLabel;
        private final TextView tvTime;
        private final ProgressBar progressBar;
        private final Button btnStartPause;
        private CountDownTimer countDownTimer;

        public TimerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            tvTime = itemView.findViewById(R.id.tvTime);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnStartPause = itemView.findViewById(R.id.btnStartPause);

            itemView.setOnClickListener(v -> showDeleteDialog(getAdapterPosition()));
        }

        public void bind(TimerItem timerItem) {
            tvLabel.setText(timerItem.getLabel());
            tvTime.setText(formatTime(timerItem.getRemainingTime()));
            progressBar.setMax((int) timerItem.getTimeInMillis() / 1000);
            progressBar.setProgress((int) timerItem.getRemainingTime() / 1000);

            if (timerItem.isRunning()) {
                btnStartPause.setText(timerItem.isPaused() ? "Resume" : "Pause");
            } else {
                btnStartPause.setText("Start");
            }

            btnStartPause.setOnClickListener(v -> {
                if (!timerItem.isRunning()) {
                    startTimer(timerItem);
                } else {
                    if (timerItem.isPaused()) {
                        resumeTimer(timerItem);
                    } else {
                        pauseTimer(timerItem);
                    }
                }
            });
        }

        private void startTimer(TimerItem timerItem) {
            timerItem.setRunning(true);
            timerItem.setPaused(false);
            btnStartPause.setText("Pause");

            countDownTimer = new CountDownTimer(timerItem.getRemainingTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerItem.setRemainingTime(millisUntilFinished);
                    tvTime.setText(formatTime(millisUntilFinished));
                    progressBar.setProgress((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    timerItem.setRunning(false);
                    timerItem.setPaused(false);
                    timerItem.setRemainingTime(timerItem.getTimeInMillis());
                    tvTime.setText("00:00");
                    progressBar.setProgress(0);
                    btnStartPause.setText("Start");

                    NotificationHelper.showNotification(context, "Timer Finished", "Timer \"" + timerItem.getLabel() + "\" is up!");
                }
            }.start();
        }

        private void pauseTimer(TimerItem timerItem) {
            countDownTimer.cancel();
            timerItem.setPaused(true);
            btnStartPause.setText("Resume");
        }

        private void resumeTimer(TimerItem timerItem) {
            timerItem.setPaused(false);
            btnStartPause.setText("Pause");

            countDownTimer = new CountDownTimer(timerItem.getRemainingTime(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerItem.setRemainingTime(millisUntilFinished);
                    tvTime.setText(formatTime(millisUntilFinished));
                    progressBar.setProgress((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    timerItem.setRunning(false);
                    timerItem.setPaused(false);
                    timerItem.setRemainingTime(timerItem.getTimeInMillis());
                    tvTime.setText("00:00");
                    progressBar.setProgress(0);
                    btnStartPause.setText("Start");

                    NotificationHelper.showNotification(context, "Timer Finished", "Timer \"" + timerItem.getLabel() + "\" is up!");
                }
            }.start();
        }

        private String formatTime(long millis) {
            int minutes = (int) (millis / 1000) / 60;
            int seconds = (int) (millis / 1000) % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }

        private void showDeleteDialog(int position) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Timer")
                    .setMessage("Are you sure you want to delete this timer?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        timerItems.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
