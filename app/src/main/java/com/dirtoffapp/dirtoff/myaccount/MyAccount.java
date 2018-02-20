package com.dirtoffapp.dirtoff.myaccount;

import android.support.annotation.Keep;

public class MyAccount {

    public String bulky, monthlyServicesFee, weeklyServicesFee;

    @Keep
    public MyAccount() {
        // Default constructor
    }

    public MyAccount(String bulky, String monthlyServicesFee, String weeklyServicesFee) {
        this.bulky = bulky;
        this.monthlyServicesFee = monthlyServicesFee;
        this.weeklyServicesFee = weeklyServicesFee;
    }

    public String getBulky() {
        return bulky;
    }

    public void setBulky(String bulky) {
        this.bulky = bulky;
    }

    public String getMonthlyServicesFee() {
        return monthlyServicesFee;
    }

    public void setMonthlyServicesFee(String monthlyServicesFee) {
        this.monthlyServicesFee = monthlyServicesFee;
    }

    public String getWeeklyServicesFee() {
        return weeklyServicesFee;
    }

    public void setWeeklyServicesFee(String weeklyServicesFee) {
        this.weeklyServicesFee = weeklyServicesFee;
    }
}