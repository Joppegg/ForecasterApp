package com.bignerdranch.android.forecastr;

/**
 * This is a helper class for saving Locations to SharedPreferences due to issues serializing
 * Joda datetime objects, which is used in Location.
 * This is a lite-class which contains the latitude, longitude, id and name for a specific location
 * and allows the Activity to execute the forecast fetcher at runtime.

 */
public class LocationParser {
    private String mLatitude, mLongitude, mLocationName, mLocationId;

    /**
     * constructor taking a Location and saving it as a LocationParser.
     * @param location
     */
    public LocationParser(Location location){
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mLocationName = location.getLocationName();
        mLocationId = location.getLocationId();
    }

    /**
     * gets the latitude from the location.
     * @return String with latitude.
     */
    public String getLatitude() {
        return mLatitude;
    }

    /**
     * gets the longitude from the location.
     * @return Strign with longitude.
     */
    public String getLongitude() {
        return mLongitude;
    }


    /**
     * gets the location name.
     * @return String with location name.
     */
    public String getLocationName() {
        return mLocationName;
    }

    /**
     * gets the location id.
     * @return String with location id.
     */
    public String getLocationId() {
        return mLocationId;
    }
}
