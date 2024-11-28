package com.example.gymmobileapp;

public class AdminScheduleClass {

    String timeID, timeGym, statusTime, dateGym;
    int remainingPax;

    public AdminScheduleClass() {
    }

    public AdminScheduleClass(String timeID, String timeGym, int remainingPax) {
        this.timeID = timeID;
        this.timeGym = timeGym;
        this.remainingPax = remainingPax;
    }

    public String getDateGym() {
        return dateGym;
    }

    public void setDateGym(String dateGym) {
        this.dateGym = dateGym;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getTimeID() {
        return timeID;
    }

    public void setTimeID(String timeID) {
        this.timeID = timeID;
    }

    public String getTimeGym() {
        return timeGym;
    }

    public void setTimeGym(String timeGym) {
        this.timeGym = timeGym;
    }

    public int getRemainingPax() {
        return remainingPax;
    }

    public void setRemainingPax(int remainingPax) {
        this.remainingPax = remainingPax;
    }
}
