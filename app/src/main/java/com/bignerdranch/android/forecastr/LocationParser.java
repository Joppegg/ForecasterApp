package com.bignerdranch.android.forecastr;

/**
 * Helper class for saving Location to SharedPreferences due to jodaktime issues.
 * Saves the identifiers for the location and allows for executing the forecast fetcher at runtime.
 *
 *
 */
public class LocationParser {
    private String mLatitude, mLongitude, mLocationName, mLocationId;

    public LocationParser(Location location){
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mLocationName = location.getLocationName();
        mLocationId = location.getLocationId();
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String locationId) {
        mLocationId = locationId;
    }
}
