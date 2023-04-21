package com.example.healthcare.model;

public class TimeSlot {
    private String startTime;
    private boolean isAvailable;

    public TimeSlot(String startTime, boolean isAvailable) {
        this.startTime = startTime;
        this.isAvailable = isAvailable;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
