package com.example.gymmobileapp;

public class UserGymClass {

    String userID, nameUser, unitUser, emailUser, phoneUser, genderUser, roleUser, imgUser;
    String gymID, dateGym, timeGym, remarkGym, statusGym;
    int paxGym;

    public UserGymClass() {
    }

    public UserGymClass(String userID, String nameUser, String unitUser, String emailUser, String phoneUser, String genderUser, String roleUser, String imgUser, String gymID, String dateGym, String timeGym, int paxGym, String remarkGym,  String statusGym) {
        this.userID = userID;
        this.nameUser = nameUser;
        this.unitUser = unitUser;
        this.emailUser = emailUser;
        this.phoneUser = phoneUser;
        this.genderUser = genderUser;
        this.roleUser = roleUser;
        this.imgUser = imgUser;
        this.gymID = gymID;
        this.dateGym = dateGym;
        this.timeGym = timeGym;
        this.paxGym = paxGym;
        this.remarkGym = remarkGym;
        this.statusGym = statusGym;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getUnitUser() {
        return unitUser;
    }

    public void setUnitUser(String unitUser) {
        this.unitUser = unitUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(String phoneUser) {
        this.phoneUser = phoneUser;
    }

    public String getGenderUser() {
        return genderUser;
    }

    public void setGenderUser(String genderUser) {
        this.genderUser = genderUser;
    }

    public String getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
    }

    public String getGymID() {
        return gymID;
    }

    public void setGymID(String gymID) {
        this.gymID = gymID;
    }

    public String getDateGym() {
        return dateGym;
    }

    public void setDateGym(String dateGym) {
        this.dateGym = dateGym;
    }

    public String getTimeGym() {
        return timeGym;
    }

    public void setTimeGym(String timeGym) {
        this.timeGym = timeGym;
    }

    public int getPaxGym() {
        return paxGym;
    }

    public void setPaxGym(int paxGym) {
        this.paxGym = paxGym;
    }

    public String getRemarkGym() {
        return remarkGym;
    }

    public void setRemarkGym(String remarkGym) {
        this.remarkGym = remarkGym;
    }

    public String getStatusGym() {
        return statusGym;
    }

    public void setStatusGym(String statusGym) {
        this.statusGym = statusGym;
    }
}
