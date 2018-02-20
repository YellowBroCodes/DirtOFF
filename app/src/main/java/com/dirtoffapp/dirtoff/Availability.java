package com.dirtoffapp.dirtoff;

import android.support.annotation.Keep;

public class Availability {

    public String date, endTime, key, startTime;

    @Keep
    public Availability() {
        // Default constructor
    }

    public Availability(String date, String endTime, String key, String startTime) {
        this.date = date;
        this.endTime = endTime;
        this.key = key;
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
