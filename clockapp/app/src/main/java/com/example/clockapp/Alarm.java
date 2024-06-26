package com.example.clockapp;

public class Alarm {
    private int id;
    private int hour;
    private int minute;
    private String label;
    private boolean enabled;
    private boolean repeatSun, repeatMon, repeatTue, repeatWed, repeatThu, repeatFri, repeatSat;

    // Constructor for creating a new alarm
    public Alarm(int hour, int minute, String label, boolean enabled,
                 boolean repeatSun, boolean repeatMon, boolean repeatTue,
                 boolean repeatWed, boolean repeatThu, boolean repeatFri,
                 boolean repeatSat) {
        this.hour = hour;
        this.minute = minute;
        this.label = label;
        this.enabled = enabled;
        this.repeatSun = repeatSun;
        this.repeatMon = repeatMon;
        this.repeatTue = repeatTue;
        this.repeatWed = repeatWed;
        this.repeatThu = repeatThu;
        this.repeatFri = repeatFri;
        this.repeatSat = repeatSat;
    }

    // Constructor for retrieving an existing alarm from the database
    public Alarm(int id, int hour, int minute, String label, boolean enabled,
                 boolean repeatSun, boolean repeatMon, boolean repeatTue,
                 boolean repeatWed, boolean repeatThu, boolean repeatFri,
                 boolean repeatSat) {
        this(hour, minute, label, enabled, repeatSun, repeatMon, repeatTue, repeatWed, repeatThu, repeatFri, repeatSat);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPeriod() {
        return hour < 12 ? "AM" : "PM";
    }

    public String getDays() {
        StringBuilder days = new StringBuilder();
        if (repeatSun) days.append("S ");
        if (repeatMon) days.append("M ");
        if (repeatTue) days.append("T ");
        if (repeatWed) days.append("W ");
        if (repeatThu) days.append("T ");
        if (repeatFri) days.append("F ");
        if (repeatSat) days.append("S ");
        return days.toString().trim();
    }

    public boolean isRepeatSun() {
        return repeatSun;
    }

    public boolean isRepeatMon() {
        return repeatMon;
    }

    public boolean isRepeatTue() {
        return repeatTue;
    }

    public boolean isRepeatWed() {
        return repeatWed;
    }

    public boolean isRepeatThu() {
        return repeatThu;
    }

    public boolean isRepeatFri() {
        return repeatFri;
    }

    public boolean isRepeatSat() {
        return repeatSat;
    }

    public void setRepeatSun(boolean repeatSun) {
        this.repeatSun = repeatSun;
    }

    public void setRepeatMon(boolean repeatMon) {
        this.repeatMon = repeatMon;
    }

    public void setRepeatTue(boolean repeatTue) {
        this.repeatTue = repeatTue;
    }

    public void setRepeatWed(boolean repeatWed) {
        this.repeatWed = repeatWed;
    }

    public void setRepeatThu(boolean repeatThu) {
        this.repeatThu = repeatThu;
    }

    public void setRepeatFri(boolean repeatFri) {
        this.repeatFri = repeatFri;
    }

    public void setRepeatSat(boolean repeatSat) {
        this.repeatSat = repeatSat;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
