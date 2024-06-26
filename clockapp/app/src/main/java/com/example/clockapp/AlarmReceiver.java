package com.example.clockapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "alarm_channel";
    static final int SNOOZE_TIME = 10 * 60 * 1000; // 10 minutes in milliseconds

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        Intent turnOffIntent = new Intent(context, AlarmActionReceiver.class);
        turnOffIntent.setAction("TURN_OFF");
        PendingIntent turnOffPendingIntent = PendingIntent.getBroadcast(context, 0, turnOffIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent snoozeIntent = new Intent(context, AlarmActionReceiver.class);
        snoozeIntent.setAction("SNOOZE");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Sử dụng âm thanh tùy chỉnh từ thư mục res/raw
        Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_sound);

        // Tạo MediaPlayer để tính toán độ dài âm thanh
        MediaPlayer mediaPlayer = MediaPlayer.create(context, alarmSound);
        int duration = mediaPlayer.getDuration(); // Độ dài của âm thanh tính bằng milliseconds
        mediaPlayer.release();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_white)
                .setContentTitle("Alarm")
                .setContentText("Your alarm is ringing!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setTimeoutAfter(duration) // Đặt thời gian hiển thị thông báo bằng độ dài của âm thanh
                .addAction(R.drawable.ic_turn_off, "Turn Off", turnOffPendingIntent)
                .addAction(R.drawable.ic_snooze, "Snooze", snoozePendingIntent);

        if (ContextCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, builder.build());
        } else {
            // Handle the case where permission is not granted
            // You might want to inform the user or take appropriate action
            // Here we're just showing a toast message
            Toast.makeText(context, "Notification permission is not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alarm Channel";
            String description = "Channel for Alarm notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
