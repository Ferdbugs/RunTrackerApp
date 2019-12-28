package com.example.runtracker;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Run {
    private int id;
    private float distance;
    private long duration;
    private float speed;
    private String type;
    private double initialLocationLAT;
    private double initialLocationLONG;
    private double finalLocationLAT;
    private double finalLocationLONG;
    private long date;

    //Runner model with declared variables,getters and setters

    public Run(){
    }

    public Run(float distance, long duration,float speed, Location Initial, Location Final,long Date){
        this.distance = distance;
        this.duration = duration;
        this.speed = speed;
        this.initialLocationLAT = Initial.getLatitude();
        this.initialLocationLONG = Initial.getLongitude();
        this.finalLocationLAT = Final.getLatitude();
        this.finalLocationLONG = Final.getLongitude();
        setDate(Date);
    }

    public int getID() {
        return id;
    }

    public float getDistance() {
        return distance;
    }

    public long getDuration() {
        return duration;
    }

    public float getSpeed() {
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

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSpeed(float speed) {
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

    public String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(date));
    }

    public long getLongDate() {
        return date;
    }

    public void setDate(long date) {
        long trim = date % (24*3600*1000);
        this.date = date-trim;
    }
}
