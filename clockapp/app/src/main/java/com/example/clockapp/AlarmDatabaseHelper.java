package com.example.clockapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alarms.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_ALARMS = "alarms";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_MINUTE = "minute";
    private static final String COLUMN_LABEL = "label";
    private static final String COLUMN_ENABLED = "enabled";
    private static final String COLUMN_REPEAT_SUN = "repeat_sun";
    private static final String COLUMN_REPEAT_MON = "repeat_mon";
    private static final String COLUMN_REPEAT_TUE = "repeat_tue";
    private static final String COLUMN_REPEAT_WED = "repeat_wed";
    private static final String COLUMN_REPEAT_THU = "repeat_thu";
    private static final String COLUMN_REPEAT_FRI = "repeat_fri";
    private static final String COLUMN_REPEAT_SAT = "repeat_sat";

    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database", "Creating database with version " + DATABASE_VERSION);
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HOUR + " INTEGER, " +
                COLUMN_MINUTE + " INTEGER, " +
                COLUMN_LABEL + " TEXT, " +
                COLUMN_ENABLED + " INTEGER, " +
                COLUMN_REPEAT_SUN + " INTEGER, " +
                COLUMN_REPEAT_MON + " INTEGER, " +
                COLUMN_REPEAT_TUE + " INTEGER, " +
                COLUMN_REPEAT_WED + " INTEGER, " +
                COLUMN_REPEAT_THU + " INTEGER, " +
                COLUMN_REPEAT_FRI + " INTEGER, " +
                COLUMN_REPEAT_SAT + " INTEGER)";
        db.execSQL(CREATE_ALARMS_TABLE);
        Log.d("Database", "Table created: " + CREATE_ALARMS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Database", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }
}
