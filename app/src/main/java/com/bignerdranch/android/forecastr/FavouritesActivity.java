package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {
    private static final String TAG = "FavouritesActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intenthome = new Intent(FavouritesActivity.this, HomeActivity.class);
                        startActivity(intenthome);
                        break;

                    case R.id.nav_search:
                        Intent intentsearch = new Intent(FavouritesActivity.this, SearchActivity.class);
                        startActivity(intentsearch);
                        break;

                    case R.id.nav_favorites:
                        break;
                }

                return false;
            }


        });
        new SearchTask().execute();
    }


    /**
     *
     * When location is deleted, updates the list with new items.
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        new SearchTask().execute();


    }

    /**
     * Updates current weather temperature, windspeed etc from the chosen lat/long.
     *
     *
     */
    private class SearchTask extends AsyncTask<Void,Void,Void> {
        private boolean mIsDataFetchedOk;
        private SharedPreference mSharedPreference = new SharedPreference();
        ArrayList<LocationParser> locationsToBeParsed = mSharedPreference.getFavourites(getApplicationContext());
        ArrayList<Location> locationsToDisplay = new ArrayList<Location>();
        @Override
        protected Void doInBackground(Void... voids) {

            mIsDataFetchedOk = true;
            try {
                // Loops all Locations to be parsed into the favouriteslist.
                for (LocationParser location : locationsToBeParsed) {
                    Location mLocationToDisplay = new Location();
                    mLocationToDisplay.setLatitude(location.getLatitude());
                    mLocationToDisplay.setLongitude(location.getLongitude());
                    mLocationToDisplay.setLocationId(location.getLocationId());
                    mLocationToDisplay.setLocationName(location.getLocationName());
                    ForecastFetcher fetcher = new ForecastFetcher(mLocationToDisplay);
                    fetcher.printArray();
                    locationsToDisplay.add(mLocationToDisplay);

                }
            }catch (IOException ioe){
                Log.i(TAG, "ioexception");
                mIsDataFetchedOk = false;

            }catch (JSONException joe){
                Log.i(TAG, "joexception");
                mIsDataFetchedOk = false;
            }


            return null;
        }


        //Updates the ui
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Checks for network/json errors.
            if (mIsDataFetchedOk) {
                FavouritesFragment favouritesFragment = new FavouritesFragment(locationsToDisplay);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.favourites_placeholder, favouritesFragment, favouritesFragment.getTag())
                        .commitAllowingStateLoss();
            }

            //commits error fragment instead.
            else{
                ErrorFragment errorFragment = new ErrorFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.favourites_placeholder, errorFragment, errorFragment.getTag())
                        .commitAllowingStateLoss();

            }


        }
    }
}