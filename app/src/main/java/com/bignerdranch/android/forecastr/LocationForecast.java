package com.bignerdranch.android.forecastr;


import android.os.Parcel;
import android.os.Parcelable;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * This class represents a forecast for a given time and a given location.
 * Handles storing the temperature, weathersymbol, windspeed and time.
 *
 *
 */
public class LocationForecast implements Parcelable {
    private Location mLocation;
    private int mIndexValue;
    private String mValidTime, mTemperature, mWeatherSymbol, mWindSpeed;
    private static final String TAG = "LocationForecast";
    private DateTime mDateTime;
    private  int MIDDAY = 12;
    private boolean isMidDay;
    public LocationForecast(){

    }

    //For serializing.
    protected LocationForecast(Parcel in) {
        mLocation = in.readParcelable(Location.class.getClassLoader());
        mIndexValue = in.readInt();
        mValidTime = in.readString();
        mTemperature = in.readString();
        mWeatherSymbol = in.readString();
        mWindSpeed = in.readString();
        MIDDAY = in.readInt();
        isMidDay = in.readByte() != 0;
    }

    public static final Creator<LocationForecast> CREATOR = new Creator<LocationForecast>() {
        @Override
        public LocationForecast createFromParcel(Parcel in) {
            return new LocationForecast(in);
        }

        @Override
        public LocationForecast[] newArray(int size) {
            return new LocationForecast[size];
        }
    };


    /**
     *
     * Checks if the forecast is mid day(12.00)
     * @return true if middday
     */
    public boolean isMidDay() {
        return isMidDay;
    }

    /**
     *Sets the forecast to be midday
     * @param midDay
     */
    public void setMidDay(boolean midDay) {
        isMidDay = midDay;
    }


    /**
     * Gets the day and hour by formatting the ISO 8601 format to HH:mm for cleaner presentation in UI.
     * @return
     */
    public String getDayAndHour(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        return fmt.print(mDateTime);

    }

    /**
     *
     * Gets the datetime.
     * @return datetime.
     */
    public DateTime getDateTime() {
        return mDateTime;
    }

    /**
     * Workaround for parsing String to time with help of JodaTime.
     * If the hour of the day corresponds to MIDDAY, set the isMidDay boolean to true.
     * Sets the forecast to be midday if the specific forecasts hour is equal to midday (12.00)
     * @param localDateTime
     */
    public void setDateTime(String localDateTime)  {
        DateTime dt = new DateTime(localDateTime);
        mDateTime = dt;
        boolean isMidDay = mDateTime.getHourOfDay() == MIDDAY;
        setMidDay(isMidDay);
    }

    /**
     * Returns the owner location.
     * @return Location mlocation.
     */
    public Location getLocation() {
        return mLocation;
    }

    /**
     * Sets the owner location.
     * @param location
     */
    public void setLocation(Location location) {
        mLocation = location;
    }


    /**
     * Gets the valid time (The time the forecast is valid for)
     * @return
     */
    public String getValidTime() {
        return mValidTime;
    }

    /**
     * Sets the valid time.
     * @param validTime
     */
    public void setValidTime(String validTime) {
        mValidTime = validTime;
    }

    /**
     * Gets the forecast temperature.
     * @return String temperature.
     */
    public String getTemperature() {
        return mTemperature;
    }

    /**
     * Sets the forecast temperature.
     * Trims the brackets from the retrieved json format.
     * @param temperature the temperature.
     */
    public void setTemperature(String temperature) {
        mTemperature  = temperature.substring(1, temperature.length()-1);
    }

    /**
     * Gets the forecast windspeed.
     * @return String windspeed.
     */
    public String getWindSpeed() {
        return mWindSpeed;
    }

    /**
     * Sets the forecast windspeed.
     * @param windSpeed the windspeed.
     */
    public void setWindSpeed(String windSpeed) {
        String trimmedWindSpeed = windSpeed.substring(1, windSpeed.length()-1);
        mWindSpeed = trimmedWindSpeed;
    }

    /**
     * Gets the weathersymbol used to present a visual of the forecasted weather.
     * More info here: https://opendata-download-metfcst.smhi.se/
     * @return String representing an int 0-27 to use for weather symbols.
     */
    public String getWeatherSymbol() {
        return mWeatherSymbol;
    }

    /**
     * Sets the weathersymbol to be used for the forecast, and trims the brackets.
     * @param weatherSymbol the forecasts weather symbol.
     */
    public void setWeatherSymbol(String weatherSymbol) {
        mWeatherSymbol  = weatherSymbol.substring(1, weatherSymbol.length()-1);
    }


    //For serializing
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mLocation, i);
        parcel.writeInt(mIndexValue);
        parcel.writeString(mValidTime);
        parcel.writeString(mTemperature);
        parcel.writeString(mWeatherSymbol);
        parcel.writeString(mWindSpeed);
        parcel.writeInt(MIDDAY);
        parcel.writeByte((byte) (isMidDay ? 1 : 0));

    }
}
