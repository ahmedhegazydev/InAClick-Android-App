package com.example.hp.in_a_click.tests;

/**
 * Created by hp on 11/1/2017.
 */

public class Tracking {


    String userEmail = "", UserUID = "", userLocationLat = "", userLocationLon = "";

    public Tracking(String userEmail, String userUID, String userLocationLat, String userLocationLon) {

        this.userEmail = userEmail;
        UserUID = userUID;
        this.userLocationLat = userLocationLat;
        this.userLocationLon = userLocationLon;
    }

    public Tracking() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getUserLocationLat() {
        return userLocationLat;
    }

    public void setUserLocationLat(String userLocationLat) {
        this.userLocationLat = userLocationLat;
    }

    public String getUserLocationLon() {
        return userLocationLon;
    }

    public void setUserLocationLon(String userLocationLon) {
        this.userLocationLon = userLocationLon;
    }


}
