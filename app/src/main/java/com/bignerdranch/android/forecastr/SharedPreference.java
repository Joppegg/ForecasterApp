package com.bignerdranch.android.forecastr;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
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

        } else {
            return null;
        }

      /*
        for(Location location : favourites){
            Log.i("Shared", location.getLocationName());

        }
        */


    }

    public void addFavourite(Context context, LocationParser location){
        List<LocationParser> favourites = getFavourites(context);
        if (favourites == null){
            favourites = new ArrayList<LocationParser>();
        }
        favourites.add(location);
        saveFavourites(context, favourites);
        Log.i(TAG, "Saved with id: " + location.getLocationId() + " " + location.getLocationName());
    }

    public void removeFavourite(Context context, LocationParser location){
        ArrayList<LocationParser> favourites = getFavourites(context);
        if (favourites != null){
            favourites.remove(location);
            saveFavourites(context, favourites);
        }

    }

}
