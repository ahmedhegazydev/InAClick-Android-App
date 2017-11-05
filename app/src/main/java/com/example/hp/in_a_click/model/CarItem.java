package com.example.hp.in_a_click.model;

/**
 * Created by hp on 11/4/2017.
 */

public class CarItem {
    String id;
    String lat;
    String lon;
    String name;
    String model;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarItem(String id, String lat, String lon, String name, String model) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.model = model;
    }

    public CarItem(String id, String lat, String lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }


}
