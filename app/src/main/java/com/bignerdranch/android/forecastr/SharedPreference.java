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
    public SharedPreference() {
        super();
    }


    public void saveFavourites(Context context, List<Location> favourites){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavourites = gson.toJson(favourites);

        editor.putString(FAVOURITES, jsonFavourites);
        editor.commit();
    }

    public ArrayList<Location> getFavourites(Context context){
        SharedPreferences settings;
        List<Location> favourites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVOURITES)){
            String jsonFavourites = settings.getString(FAVOURITES, null);
            Gson gson = new Gson();
            Location[] favouriteItems = gson.fromJson(jsonFavourites, Location[].class);
            favourites = Arrays.asList(favouriteItems);
            favourites = new ArrayList<Location>(favourites);

        } else {
            return null;
        }

        for(Location location : favourites){
            Log.i("Shared", location.getLocationName());

        }
        return  (ArrayList<Location>) favourites;

    }

    public void addFavourite(Context context, Location location){
        List<Location> favourites = getFavourites(context);
        if (favourites == null){
            favourites = new ArrayList<Location>();
        }
        favourites.add(location);
        saveFavourites(context, favourites);
    }

    public void removeFavourite(Context context, Location location){
        ArrayList<Location> favourites = getFavourites(context);
        if (favourites != null){
            favourites.remove(location);
            saveFavourites(context, favourites);
        }

    }

}
