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
 * Helper class handles the saving and retrieving of locations from Shared Preference using Gson.
 *
 */
public class SharedPreference {
    public static final String PREFS_NAME = "FORECAST_APP";
    public static final String FAVOURITES = "LOCATION_Favourite";
    private static final String TAG = "SharedPreference";
    public SharedPreference() {
        super();
    }


    /**
     * Converts an arraylist with LocationParser into a JSON-string through Gson and saves it in shared preferences.
     * @param context the context
     * @param favourites the list with locations to be saved.
     */
    public void saveFavourites(Context context, List<LocationParser> favourites){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavourites = gson.toJson(favourites);

        editor.putString(FAVOURITES, jsonFavourites);
        editor.apply();
    }


    /**
     * Returns a list with the current locations saved.
     * if there are no saved preferences, instead return an empty arraylist.
     * @param context the context
     * @return favourites, ArrayList with all the saved locations
     */
    public ArrayList<LocationParser> getFavourites(Context context){
        SharedPreferences settings;
        List<LocationParser> favourites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        //Checks if there are any saved locations.
        if (settings.contains(FAVOURITES)){
            String jsonFavourites = settings.getString(FAVOURITES, null);
            Gson gson = new Gson();
            LocationParser[] favouriteItems = gson.fromJson(jsonFavourites, LocationParser[].class);
            favourites = Arrays.asList(favouriteItems);
            favourites = new ArrayList<LocationParser>(favourites);
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
            editor.apply();
            return (ArrayList<LocationParser>) favourites;
        }



    }

    /**
     *
     *Adds a location.
     * @param context the context
     * @param location the location to be added.
     * Returns true if the location is added, and false otherwise (if it is saved already).
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
            return true;
        }
        return false;

    }

    /**
     * Removes a location from the saved list.
     * @param context the context
     * @param location location to be removed.
     */
    public void removeFavourite(Context context, LocationParser location){
        List<LocationParser> favourites = getFavourites(context);
        for (LocationParser locationParser : new ArrayList<>(favourites)) {

            if (locationParser.getLocationId().equals(location.getLocationId())) {
                favourites.remove(locationParser);
            }
        }
        saveFavourites(context, favourites);
    }


    /**
     * Helper method to see if the location exists (is saved):
     * @param location the location to be checked.
     * @param context the ocntext.
     * @return
     */
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
