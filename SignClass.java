package com.example.gymmobileapp;

public class SignClass {

    String userID, nameUser, unitUser, emailUser, pwdUser, roleUser;
    String phoneUser, genderUser;
    String imgUser;

    public SignClass() {
    }

    public SignClass(String userID, String nameUser, String unitUser, String phoneUser, String emailUser, String pwdUser, String roleUser) {
        this.userID = userID;
        this.nameUser = nameUser;
        this.unitUser = unitUser;
        this.phoneUser = phoneUser;
        this.emailUser = emailUser;
        this.pwdUser = pwdUser;
        this.roleUser = roleUser;
    }

    public String getUnitUser() {
        return unitUser;
    }

    public void setUnitUser(String unitUser) {
        this.unitUser = unitUser;
    }

    public String getImgUser() {
        return imgUser;
    }

    public void setImgUser(String imgUser) {
        this.imgUser = imgUser;
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

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPwdUser() {
        return pwdUser;
    }

    public void setPwdUser(String pwdUser) {
        this.pwdUser = pwdUser;
    }

    public String getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }
}