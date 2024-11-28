package com.example.gymmobileapp;

public class UserGymTimeClass {

    String timeID, timeGym, statusTime;
    int remainingPax;



    public UserGymTimeClass() {
    }

    public UserGymTimeClass(String timeID, String timeGym , int remainingPax) {
        this.timeID = timeID;
        this.timeGym = timeGym;
        this.remainingPax = remainingPax;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public int getRemainingPax() {
        return remainingPax;
    }

    public void setRemainingPax(int remainingPax) {
        this.remainingPax = remainingPax;
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
}
