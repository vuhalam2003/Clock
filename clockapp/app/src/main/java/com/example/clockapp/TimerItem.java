package com.example.clockapp;

public class TimerItem {
    private int id;
    private String label;
    private long totalTime;
    private long remainingTime;
    private boolean isRunning;
    private boolean isPaused;

    public TimerItem(String label, long totalTime, long remainingTime, boolean isRunning, boolean isPaused) {
        this.label = label;
        this.totalTime = totalTime;
        this.remainingTime = remainingTime;
        this.isRunning = isRunning;
        this.isPaused = isPaused;
    }

    public TimerItem(int id, String label, long totalTime, long remainingTime, boolean isRunning, boolean isPaused) {
        this.id = id;
        this.label = label;
        this.totalTime = totalTime;
        this.remainingTime = remainingTime;
        this.isRunning = isRunning;
        this.isPaused = isPaused;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public long getTimeInMillis() {
        return remainingTime;
    }
}
