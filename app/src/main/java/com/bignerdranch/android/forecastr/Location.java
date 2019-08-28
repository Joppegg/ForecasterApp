package com.bignerdranch.android.forecastr;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class handles a location and the weather forecasts for it.
 *
 */
public class Location implements Parcelable {
    private String mLatitude, mLongitude;
    private String mLocationName;
    private String locationId;
    private ArrayList<LocationForecast> mForeCast = new ArrayList<>();
    private ArrayList<LocationForecast> mForeCastMidDay = new ArrayList<>();

    //Default constructor
    public Location (){

    }

    //For serializing
    protected Location(Parcel in) {
        mLatitude = in.readString();
        mLongitude = in.readString();
        mLocationName = in.readString();
        locationId = in.readString();
        mForeCast = in.createTypedArrayList(LocationForecast.CREATOR);
        mForeCastMidDay = in.createTypedArrayList(LocationForecast.CREATOR);
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    /**
     * sets the locations latitude.
     * @param latitude
     */
    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    /**
     * Sets the locations longitude.
     * @param longitude
     */
    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    /**
     * Gets the locations ID as defined from Google Places.
     * @return locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Sets the locations ID as defined from Google Places.
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * Gets an arraylist with the Mid-day forecast (12.00) for the location.
     * @return
     */
    public ArrayList<LocationForecast> getForeCastMidDay() {
        return mForeCastMidDay;
    }

    /**
     * Sets the arraylist with Mid-day forecast(12.00) for the location.
     */
    public void setForeCastMidDay(ArrayList<LocationForecast> foreCastMidDay) {
        mForeCastMidDay = foreCastMidDay;
    }

    /**
     * Adds a locationforecast to the mid-day forecast list.
     * @param locationForecast
     */
    public void addMidDayForeCast(LocationForecast locationForecast){
        mForeCastMidDay.add(locationForecast);

    }

    /**
     * Adds a locationforecast to the forecast list.
     * @param locationForecast
     */
    public void addForecast(LocationForecast locationForecast){
        mForeCast.add(locationForecast);
    }

    /**
     * Gets the locations latitude.
     * @return String latitude
     */
    public String getLatitude() {
        return mLatitude;
    }

    /**
     *Sets the locations latitude.
     * Formats the latitude to 3 decimals (~ 100 meters) to avoid json errors for too specific coordinates.
     * @param latitude
     */
    public void setLatitude(double latitude) {
        mLatitude = String.format(Locale.ENGLISH, "%.3f", latitude);

    }

    /**
     * Gets the location longitude.
     * @return String longitude
     */
    public String getLongitude() {
        return mLongitude;
    }

    /**
     * Sets the locations longitude.
     * Formats the longitude to 3 decimals (~ 100 meters) to avoid json errors for too specific coordinates.
     * @param longitude
     */
    public void setLongitude(double longitude) {
        mLongitude = String.format(Locale.ENGLISH, "%.3f", longitude);
    }

    /**
     * Gets the locations name.
     * @return String location name.
     */
    public String getLocationName() {
        return mLocationName;
    }

    /**
     * Sets the location name.
     * @param locationName
     */
    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    /**
     * Gets the forecast list for the location.
     * @return forecast list.
     */
    public ArrayList<LocationForecast> getForeCast() {
        return mForeCast;
    }

    //for serializing
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mLatitude);
        parcel.writeString(mLongitude);
        parcel.writeString(mLocationName);
        parcel.writeString(locationId);
        parcel.writeTypedList(mForeCast);
        parcel.writeTypedList(mForeCastMidDay);
    }
}
