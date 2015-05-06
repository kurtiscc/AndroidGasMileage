package com.example.kurtiscc.gasmileage;

/**
 * Created by kurtiscc on 2/2/2015.
 */
public class Gas {
    private String date, location;
    private int id, odometer;
    private double gallons;
    private double price;
    private String lat;
    private String lng;



    public Gas(){
        this.id = 0;
        this.date = "";
        this.odometer = 0;
        this.gallons = 0;
        this.price = 0;
        this.location = "";
        this.lat = "";
        this.lng = "";
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOdometer() {
        return this.odometer;
    }

    public void setOdometer( int odometer) {
        this.odometer = odometer;
    }

    public double getGallons() {
        return this.gallons;
    }

    public void setGallons(double gallons) {
        this.gallons = gallons;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice (double price) {
        this.price = price;
    }

    public String getLat() {
        return this.lat;
    }

    public String getLng() {
        return this.lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
