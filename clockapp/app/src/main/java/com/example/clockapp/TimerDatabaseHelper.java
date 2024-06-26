package com.example.clockapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TimerDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "timers.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TIMERS = "timers";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LABEL = "label";
    private static final String COLUMN_TOTAL_TIME = "total_time";
    private static final String COLUMN_REMAINING_TIME = "remaining_time";
    private static final String COLUMN_IS_RUNNING = "is_running";
    private static final String COLUMN_IS_PAUSED = "is_paused";

    public TimerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMERS_TABLE = "CREATE TABLE " + TABLE_TIMERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LABEL + " TEXT, " +
                COLUMN_TOTAL_TIME + " INTEGER, " +
                COLUMN_REMAINING_TIME + " INTEGER, " +
                COLUMN_IS_RUNNING + " INTEGER, " +
                COLUMN_IS_PAUSED + " INTEGER)";
        db.execSQL(CREATE_TIMERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS);
        onCreate(db);
    }

    public void addTimer(TimerItem timer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, timer.getLabel());
        values.put(COLUMN_TOTAL_TIME, timer.getTotalTime());
        values.put(COLUMN_REMAINING_TIME, timer.getRemainingTime());
        values.put(COLUMN_IS_RUNNING, timer.isRunning() ? 1 : 0);
        values.put(COLUMN_IS_PAUSED, timer.isPaused() ? 1 : 0);
        db.insert(TABLE_TIMERS, null, values);
        db.close();
    }

    public ArrayList<TimerItem> getAllTimers() {
        ArrayList<TimerItem> timerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TIMERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String label = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LABEL));
                long totalTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_TIME));
                long remainingTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_TIME));
                boolean isRunning = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_RUNNING)) == 1;
                boolean isPaused = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PAUSED)) == 1;

                TimerItem timer = new TimerItem(id, label, totalTime, remainingTime, isRunning, isPaused);
                timerList.add(timer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return timerList;
    }

    public void deleteTimer(TimerItem timer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TIMERS, COLUMN_ID + " = ?", new String[]{String.valueOf(timer.getId())});
        db.close();
    }
}
