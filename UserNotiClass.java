package com.example.gymmobileapp;

public class UserNotiClass {

    String userID, gymID, notiID, status, selectedMaintenance, statusGym;

    public UserNotiClass() {
    }

    public UserNotiClass(String userID, String gymID, String notiID) {
        this.userID = userID;
        this.gymID = gymID;
        this.notiID = notiID;
    }

    public String getStatusGym() {
        return statusGym;
    }

    public void setStatusGym(String statusGym) {
        this.statusGym = statusGym;
    }

    public String getSelectedMaintenance() {
        return selectedMaintenance;
    }

    public void setSelectedMaintenance(String selectedMaintenance) {
        this.selectedMaintenance = selectedMaintenance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getGymID() {
        return gymID;
    }

    public void setGymID(String gymID) {
        this.gymID = gymID;
    }

    public String getNotiID() {
        return notiID;
    }

    public void setNotiID(String notiID) {
        this.notiID = notiID;
    }
}
