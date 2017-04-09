package com.example.mick.driver7;

/**
 * Created by Mick on 13/02/2017.
 */

public class Driver {
    private String name;
    private Double lat,lon;
    String job;
    String jobStatus;
    Long jobStarted;
    Long jobFinished;
//    int id;

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

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Long getJobStarted() {
        return jobStarted;
    }

    public void setJobStarted(Long jobStarted) {
        this.jobStarted = jobStarted;
    }

    public Long getJobFinished() {
        return jobFinished;
    }

    public void setJobFinished(Long jobFinished) {
        this.jobFinished = jobFinished;
    }

    @Override
    public String toString() {
        return name;
    }
}
