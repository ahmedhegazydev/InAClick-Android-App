package com.example.hp.in_a_click.model;

/**
 * Created by hp on 11/4/2017.
 */

public class InAClickUser {


    String userEmail;
    String userPass;
    String userName;
    String userPhone;


    public InAClickUser() {
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

    public InAClickUser(String userEmail, String userPass, String userName, String userPhone) {

        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userName = userName;
        this.userPhone = userPhone;
    }
}
