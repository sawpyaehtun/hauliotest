package com.example.user.hauliotest;

import java.io.Serializable;

public class JobBean implements Serializable {
    int id,job_id,priority;
    String company,address;
    double lat,longi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public JobBean() {
    }

    public JobBean(int id, int job_id, int priority, String company, String address, double lat, double longi) {
        this.id = id;
        this.job_id = job_id;
        this.priority = priority;
        this.company = company;
        this.address = address;
        this.lat = lat;
        this.longi = longi;
    }
}
