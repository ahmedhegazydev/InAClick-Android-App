package com.example.hp.in_a_click.tests;

/**
 * Created by hp on 10/31/2017.
 */

public class User {

    String email, status = "";

    public User(String email, String status) {
        this.email = email;
        this.status = status;
    }


    public User() {
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
