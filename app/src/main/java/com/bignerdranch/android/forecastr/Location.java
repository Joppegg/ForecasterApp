package com.bignerdranch.android.forecastr;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Location {
    private String mLatitude, mLongitude;
    private String mLocationName;
    private String locationId;

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    private int mId; //Change this to double? goby googles documentation
    private ArrayList<LocationForecast> mForeCast = new ArrayList<>();
    private ArrayList<LocationForecast> mForeCastMidDay = new ArrayList<>();

    public ArrayList<LocationForecast> getForeCastMidDay() {
        return mForeCastMidDay;
    }

    public void setForeCastMidDay(ArrayList<LocationForecast> foreCastMidDay) {
        mForeCastMidDay = foreCastMidDay;
    }

    /**
     * Adds a locationforecast to the mid-day forecast.
     * @param locationForecast
     */
    public void addMidDayForeCast(LocationForecast locationForecast){
        mForeCastMidDay.add(locationForecast);

    }

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
        mLatitude = String.format(Locale.ENGLISH, "%.3f", latitude);

    }


    public String getLongitude() {
        return mLongitude;
    }

    /**
     * Formats to 3 decimals (~ 100 meters) to avoid json errors for too specific coordinates.
     * @param longitude
     */
    public void setLongitude(double longitude) {
        mLongitude = String.format(Locale.ENGLISH, "%.3f", longitude);
    //    mLongitude = df.format(longitude);

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
