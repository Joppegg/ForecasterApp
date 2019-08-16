package com.bignerdranch.android.forecastr;

import android.nfc.Tag;
import android.os.Build;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public class LocationForecast {
    private Location mLocation;
    private int mIndexValue;
    private String mValidTime, mTemperature, mWeatherSymbol, mWindSpeed;
    private static final String TAG = "LocationForecast";
    private DateTime mDateTime;
    private final int MIDDAY = 12;



    public boolean isMidDay() {
        return isMidDay;
    }

    public void setMidDay(boolean midDay) {
        isMidDay = midDay;
    }

    private boolean isMidDay;


    public String getDayAndHour(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        return fmt.print(mDateTime);

    }
    public DateTime getDateTime() {
        return mDateTime;
    }

    /**
     * Workaround for parsing String to time with help of JodaTime.
     * If the hour of the day corresponds to MIDDAY, set the isMidDay boolean to true.
     * @param localDateTime
     */
    public void setDateTime(String localDateTime)  {
        DateTime dt = new DateTime(localDateTime );
        mDateTime = dt;

        Boolean isMidDay = mDateTime.getHourOfDay() == MIDDAY;
        setMidDay(isMidDay);


    }


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
