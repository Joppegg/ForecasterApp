package com.bignerdranch.android.forecastr;

public class LocationForecast {
    private Location mLocation;
    private double mTemperature, mWindSpeed;
    private int mWeatherSymbol;
    private int mIndexValue;
    private String mValidTime;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getValidTime() {
        return mValidTime;
    }

    public void setValidTime(String validTime) {
        mValidTime = validTime;
    }

    public int getIndexValue() {
        return mIndexValue;
    }

    public void setIndexValue(int indexValue) {
        mIndexValue = indexValue;
    }



    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        mWindSpeed = windSpeed;
    }


    public int getWeatherSymbol() {
        return mWeatherSymbol;
    }

    public void setWeatherSymbol(int weatherSymbol) {
        mWeatherSymbol = weatherSymbol;
    }
}
