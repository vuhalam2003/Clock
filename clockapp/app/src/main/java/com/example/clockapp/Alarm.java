package com.example.clockapp;

public class Alarm {
    private int hour;
    private int minute;
    private String label;
    private String days;
    private boolean enabled;

    public Alarm(int hour, int minute, String label, String days, boolean enabled) {
        this.hour = hour;
        this.minute = minute;
        this.label = label;
        this.days = days;
        this.enabled = enabled;
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

    public String getDays() {
        return days;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPeriod() {
        return hour < 12 ? "AM" : "PM";
    }
}
