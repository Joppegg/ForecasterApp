package com.bignerdranch.android.forecastr;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Location implements Parcelable {
    private String mLatitude, mLongitude;
    private String mLocationName;
    private String locationId;

    public Location (){

    }

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



    public ArrayList<LocationForecast> getForeCast() {
        return mForeCast;
    }

    public void setForeCast(ArrayList<LocationForecast> foreCast) {
        mForeCast = foreCast;
    }

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
