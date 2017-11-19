package com.example.hp.in_a_click.model;

/**
 * Created by hp on 11/15/2017.
 */

public class UserHomeOwner {


    String userEmail;
    String userPass;
    String userName;
    String userPhone;
    String city = "";

    public UserHomeOwner(String userEmail, String userPass, String userName, String userPhone, String city, boolean driverStatus) {
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userName = userName;
        this.userPhone = userPhone;
        this.city = city;
        this.driverStatus = driverStatus;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    boolean driverStatus = false;//by default



    public UserHomeOwner() {
    }


    public boolean isDriverStatus() {

        return driverStatus;
    }

    public void setDriverStatus(boolean driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
