package com.bignerdranch.android.forecastr;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class handles the saving and retrieving of locations in favourites.
 *
 *
 */
public class SharedPreference {
    public static final String PREFS_NAME = "FORECAST_APP";
    public static final String FAVOURITES = "LOCATION_Favourite";
    private static final String TAG = "SharedPreference";
    public SharedPreference() {
        super();
    }


    public void saveFavourites(Context context, List<LocationParser> favourites){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavourites = gson.toJson(favourites);

        editor.putString(FAVOURITES, jsonFavourites);
        editor.commit();
    }




    public ArrayList<LocationParser> getFavourites(Context context){
        SharedPreferences settings;
        List<LocationParser> favourites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVOURITES)){
            String jsonFavourites = settings.getString(FAVOURITES, null);
            Gson gson = new Gson();
            LocationParser[] favouriteItems = gson.fromJson(jsonFavourites, LocationParser[].class);
            favourites = Arrays.asList(favouriteItems);
            favourites = new ArrayList<LocationParser>(favourites);
            //Logs size of array
            Log.i(TAG, "Number of entries: " +  favourites.size());
            return  (ArrayList<LocationParser>) favourites;

        }
        //If there is no saved sharedpreferences, create an empty arraylist and return it.
        else {

            SharedPreferences.Editor editor;

            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            editor = settings.edit();

            favourites = new ArrayList<LocationParser>();
            Gson gson = new Gson();
            String jsonFavourites = gson.toJson(favourites);

            editor.putString(FAVOURITES, jsonFavourites);
            editor.commit();
            return (ArrayList<LocationParser>) favourites;
            //TODO RETURNA ALLTID EN LISTA. ÄVEN OM DEN ÄR TOM.
            // annars blir det error första gången man installerar.
        }



    }

    /**
     *
     *Adds a location.
     * @param context
     * @param location
     * @return
     */
    public boolean addFavourite(Context context, LocationParser location){
        List<LocationParser> favourites = getFavourites(context);
        if (favourites == null){
            favourites = new ArrayList<>();
        }

        if (!doesLocationExist(location, context)){
            favourites.add(location);
            saveFavourites(context, favourites);
            Log.i(TAG, "Saved with id: " + location.getLocationId() + " " + location.getLocationName());
            return true;
        }
        return false;

    }

    public void removeFavourite(Context context, LocationParser location){
        List<LocationParser> favourites = getFavourites(context);
        Log.i(TAG," Before size" + favourites.size());
        Log.i(TAG, "To be removed " + location.getLocationName());


        for (LocationParser locationParser : new ArrayList<>(favourites)) {

            if (locationParser.getLocationId().equals(location.getLocationId())) {
                favourites.remove(locationParser);
            }
        }
        saveFavourites(context, favourites);





        Log.i(TAG, "after " + favourites.size());
        Log.i(TAG, "After real array: " + getFavourites(context).size());





    }

    public boolean doesLocationExist(LocationParser location, Context context){
        ArrayList<LocationParser> locations = getFavourites(context);

        if (location==null){
            return false;
        }
        for (LocationParser locationParser : locations){
            if (locationParser.getLocationId().equals(location.getLocationId())){
                return true;
            }
        }
        return false;
    }

}
