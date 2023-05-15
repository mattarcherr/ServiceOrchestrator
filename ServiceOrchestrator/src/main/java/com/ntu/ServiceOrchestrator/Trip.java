package com.ntu.ServiceOrchestrator;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;

public class Trip {

    public String userID;
    public String tripID;
    public Double longitude;
    public Double latitude;
    public LocalDate date;
    public String title;
    public String location;
        public forecastData[] weather;
    public int interest;

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String UserID) {
        userID = UserID;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setWeather(forecastData[] weather) {
        this.weather = weather;
    }

    public forecastData[] getWeather() {
        return weather;
    }
}
