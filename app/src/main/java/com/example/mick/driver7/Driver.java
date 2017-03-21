package com.example.mick.driver7;

/**
 * Created by Mick on 13/02/2017.
 */

public class Driver {
    private String name;
    private Double lat,lon;

    public Driver(String name, Double lon, Double lat) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public Driver() {
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
