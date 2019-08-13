package com.bignerdranch.android.forecastr;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private double mLatitude, mLongitude;
    private String mLocationName;
    private int mId; //Change this to double? goby googles documentation
    private ArrayList<LocationForecast> mForeCast = new ArrayList<>();

    public void addForecast(LocationForecast locationForecast){
        mForeCast.add(locationForecast);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public ArrayList<LocationForecast> getForeCast() {
        return mForeCast;
    }

    public void setForeCast(ArrayList<LocationForecast> foreCast) {
        mForeCast = foreCast;
    }
}
