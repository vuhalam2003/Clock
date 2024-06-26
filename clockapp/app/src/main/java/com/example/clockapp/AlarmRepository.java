package com.example.clockapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class AlarmRepository {
    private AlarmDatabaseHelper dbHelper;

    public AlarmRepository(Context context) {
        dbHelper = new AlarmDatabaseHelper(context);
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hour", alarm.getHour());
        values.put("minute", alarm.getMinute());
        values.put("label", alarm.getLabel());
        values.put("enabled", alarm.isEnabled() ? 1 : 0);
        values.put("repeat_sun", alarm.isRepeatSun() ? 1 : 0);
        values.put("repeat_mon", alarm.isRepeatMon() ? 1 : 0);
        values.put("repeat_tue", alarm.isRepeatTue() ? 1 : 0);
        values.put("repeat_wed", alarm.isRepeatWed() ? 1 : 0);
        values.put("repeat_thu", alarm.isRepeatThu() ? 1 : 0);
        values.put("repeat_fri", alarm.isRepeatFri() ? 1 : 0);
        values.put("repeat_sat", alarm.isRepeatSat() ? 1 : 0);

        long id = db.insert("alarms", null, values);
        Log.d("Database", "Alarm added with id: " + id);
        db.close();
    }

    public ArrayList<Alarm> getAlarms() {
        ArrayList<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("alarms", null, null, null, null, null, null);

        if (cursor != null) {
            try {
                Log.d("Database", "Fetching alarms, number of columns: " + cursor.getColumnCount());
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames) {
                    Log.d("Database", "Column: " + columnName);
                }

                if (cursor.moveToFirst()) {
                    do {
                        Log.d("Database", "Processing row");
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                        int hour = cursor.getInt(cursor.getColumnIndexOrThrow("hour"));
                        int minute = cursor.getInt(cursor.getColumnIndexOrThrow("minute"));
                        String label = cursor.getString(cursor.getColumnIndexOrThrow("label"));
                        boolean enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0;
                        boolean repeatSun = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_sun")) > 0;
                        boolean repeatMon = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_mon")) > 0;
                        boolean repeatTue = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_tue")) > 0;
                        boolean repeatWed = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_wed")) > 0;
                        boolean repeatThu = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_thu")) > 0;
                        boolean repeatFri = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_fri")) > 0;
                        boolean repeatSat = cursor.getInt(cursor.getColumnIndexOrThrow("repeat_sat")) > 0;

                        alarms.add(new Alarm(id, hour, minute, label, enabled, repeatSun, repeatMon, repeatTue, repeatWed, repeatThu, repeatFri, repeatSat));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e("Database", "Error fetching alarms", e);
            } finally {
                cursor.close();
            }
        } else {
            Log.d("Database", "Cursor is null");
        }
        db.close();
        return alarms;
    }

    public void updateAlarm(Alarm alarm) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hour", alarm.getHour());
        values.put("minute", alarm.getMinute());
        values.put("label", alarm.getLabel());
        values.put("enabled", alarm.isEnabled() ? 1 : 0);
        values.put("repeat_sun", alarm.isRepeatSun() ? 1 : 0);
        values.put("repeat_mon", alarm.isRepeatMon() ? 1 : 0);
        values.put("repeat_tue", alarm.isRepeatTue() ? 1 : 0);
        values.put("repeat_wed", alarm.isRepeatWed() ? 1 : 0);
        values.put("repeat_thu", alarm.isRepeatThu() ? 1 : 0);
        values.put("repeat_fri", alarm.isRepeatFri() ? 1 : 0);
        values.put("repeat_sat", alarm.isRepeatSat() ? 1 : 0);

        db.update("alarms", values, "_id = ?", new String[]{String.valueOf(alarm.getId())});
        Log.d("Database", "Alarm updated with id: " + alarm.getId());
        db.close();
    }

    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            Log.d("Database", "Deleting alarm with id: " + alarm.getId());
            db.delete("alarms", "_id = ?", new String[]{String.valueOf(alarm.getId())});
            Log.d("Database", "Alarm deleted");
        } catch (Exception e) {
            Log.e("Database", "Error deleting alarm", e);
        } finally {
            db.close();
        }
    }
}
