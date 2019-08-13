package com.bignerdranch.android.forecastr;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Location {
    private String mLatitude, mLongitude;
    private String mLocationName;
    private int mId; //Change this to double? goby googles documentation
    private ArrayList<LocationForecast> mForeCast = new ArrayList<>();
 DecimalFormat df = new DecimalFormat("#.000");

    public void addForecast(LocationForecast locationForecast){
        mForeCast.add(locationForecast);
    }

    public String getLatitude() {
        return mLatitude;
    }

    /**
     *
     * Formats to 3 decimals (~ 100 meters) to avoid json errors for too specific coordinates.
     * @param latitude
     */
    public void setLatitude(double latitude) {
        mLatitude = df.format(latitude);

    }

    public String getLongitude() {
        return mLongitude;
    }

    /**
     * Formats to 3 decimals (~ 100 meters) to avoid json errors for too specific coordinates.
     * @param longitude
     */
    public void setLongitude(double longitude) {
        mLongitude = df.format(longitude);

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
