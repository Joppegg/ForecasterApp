package com.bignerdranch.android.forecastr;

public class LocationForecast {
    private Location mLocation;
    private int mIndexValue;
    private String mValidTime, mTemperature, mWeatherSymbol, mWindSpeed;

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



    public String getTemperature() {

        return mTemperature;
    }

    public void setTemperature(String temperature) {
        String trimmedTemperature = temperature.substring(1, temperature.length()-1);
        mTemperature = trimmedTemperature;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        String trimmedWindSpeed = windSpeed.substring(1, windSpeed.length()-1);
        mWindSpeed = trimmedWindSpeed;
    }


    public String getWeatherSymbol() {
        return mWeatherSymbol;
    }

    public void setWeatherSymbol(String weatherSymbol) {
        String trimmedSymbol = weatherSymbol.substring(1, weatherSymbol.length()-1);
        mWeatherSymbol = trimmedSymbol;
    }
}
