package com.example.clockapp;

import static com.example.clockapp.AlarmReceiver.SNOOZE_TIME;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

public class AlarmActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("TURN_OFF".equals(action)) {
            Toast.makeText(context, "Alarm turned off", Toast.LENGTH_SHORT).show();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(0); // Cancel the notification
        } else if ("SNOOZE".equals(action)) {
            snoozeAlarm(context);
        }
    }

    private void snoozeAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long snoozeTimeInMillis = System.currentTimeMillis() + SNOOZE_TIME;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, snoozeTimeInMillis, pendingIntent);

        Toast.makeText(context, "Alarm snoozed for 10 minutes", Toast.LENGTH_SHORT).show();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(0); // Cancel the notification
    }

}
