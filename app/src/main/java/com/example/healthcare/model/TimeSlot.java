package com.example.healthcare.model;

public class TimeSlot {
    private String startTime;
    private boolean isAvailable;

    private boolean isSelected;


    public TimeSlot(String startTime, boolean isAvailable, boolean isSelected) {
        this.startTime = startTime;
        this.isAvailable = isAvailable;
        this.isSelected = isSelected;
    }

    public TimeSlot(String startTime, boolean isAvailable) {
        this.startTime=startTime;
        this.isAvailable=isAvailable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
