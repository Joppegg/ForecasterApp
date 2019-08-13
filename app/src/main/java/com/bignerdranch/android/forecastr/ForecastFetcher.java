package com.bignerdranch.android.forecastr;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ForecastFetcher {
    private Location mLocation;
    private static final String TAG = "ForecastFetcher";
    private static final Uri ENDPOINT = Uri.parse("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/");
    private List<LocationForecast> downloadLocationForecast(String url){
            List<LocationForecast>forecastList = new ArrayList<>();
        return null;
    }

    public ForecastFetcher(Location location){
        mLocation = location;
    }


    public void printArray(){
        try {
            String jsonString = new String (getUrlBytes());
            Log.i(TAG, jsonString);
            parseForecast(jsonString);

        }catch (JSONException joe){
            Log.i(TAG, "sket sig: " + joe);
        }catch (IOException ioe){
            Log.i(TAG, "ioe");
        }

    }

    public byte[] getUrlBytes() throws IOException {
        URL url = new URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/17.158/lat/58.5812/data.json");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        Log.i(TAG, url.toString());

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with");

            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }


    }
    /**
     * This method parses the forecast information from the API for a certain location, creates
     * locationforecast objects and adds them to the locations list of forecast.
     * @throws JSONException
     */
    private void parseForecast(String jsonString)throws JSONException{
        //

        JSONObject jsonBody = new JSONObject(jsonString);
        JSONArray forecastArray = jsonBody.getJSONArray("timeSeries");

        //Loops through the timeseries array.
        for (int i = 0; i <forecastArray.length(); i++){
            LocationForecast locationForecast = new LocationForecast();

            //This is one object with a validtime and an array called parameters.
            JSONObject forecastObject = forecastArray.getJSONObject(i);
            //Sets time.
            locationForecast.setValidTime(forecastObject.getString("validTime"));
            Log.i(TAG, "Time for forecast: " + locationForecast.getValidTime());

            //Loops through all parameters
            JSONArray parametersArray = forecastObject.getJSONArray("parameters");
            Log.i(TAG, "length of param array: " + parametersArray.length());


            for (int j =0; j<parametersArray.length(); j++){
                JSONObject currentParameter = parametersArray.getJSONObject(j);
                    Log.i(TAG, "Parameter name: " + currentParameter.getString("name"));
                    if (currentParameter.getString("name").equals("t")){
                        Log.i(TAG, "Temperature in Celsius: " + currentParameter.getString("values"));
                        locationForecast.setTemperature(currentParameter.getString("values"));
                    }
                    else if (currentParameter.getString("name").equals("Wsymb2")){
                        locationForecast.setWeatherSymbol(currentParameter.getString("values"));
                        Log.i(TAG, "Weathersymbol: : " + currentParameter.getString("values"));
                    }
                    else if (currentParameter.getString("name").equals("ws")){
                        locationForecast.setWindSpeed(currentParameter.getString("values"));
                        Log.i(TAG, "Windspeed: " + currentParameter.getString("values"));
                    }


            }
            mLocation.addForecast(locationForecast);
        }


    }




}
