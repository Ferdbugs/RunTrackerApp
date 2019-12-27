package com.example.runtracker;

import android.location.Location;

public class Run {
    int id;
    String distance;
    String duration;
    String speed;
    String type;
    double initialLocationLAT;
    double initialLocationLONG;
    double finalLocationLAT;
    double finalLocationLONG;

    //Runner model with declared variables,getters and setters

    public Run(){
    }

    public Run(float distance, long duration, Location Initial, Location Final){
        this.distance = String.valueOf(distance);
        this.duration = String.valueOf(duration);
        this.initialLocationLAT = Initial.getLatitude();
        this.initialLocationLONG = Initial.getLongitude();
        this.finalLocationLAT = Final.getLatitude();
        this.finalLocationLONG = Final.getLongitude();
    }

    public int getID() {
        return id;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getSpeed() {
        return speed;
    }

    public String getType() {
        return type;
    }

    public double getInitialLocationLAT(){
        return initialLocationLAT;
    }
    public double getInitialLocationLONG(){
        return initialLocationLONG;
    }

    public double getFinalLocationLAT(){
        return finalLocationLAT;
    }

    public double getFinalLocationLONG() {
        return finalLocationLONG;
    }


    public void setID(int id) {
        this.id = id;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setInitialLocationLAT(double initialLocationLAT) {
        this.initialLocationLAT = initialLocationLAT;
    }

    public void setInitialLocationLONG(double initialLocationLONG) {
        this.initialLocationLONG = initialLocationLONG;
    }

    public void setFinalLocationLAT(double finalLocationLAT) {
        this.finalLocationLAT = finalLocationLAT;
    }

    public void setFinalLocationLONG(double finalLocationLONG) {
        this.finalLocationLONG = finalLocationLONG;
    }
}
