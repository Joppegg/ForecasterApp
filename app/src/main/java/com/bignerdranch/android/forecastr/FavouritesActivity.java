package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
        //Executes a searchtask to display all favourites.
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
     *
     * Fetches the forecast information from all the saved favourites to present them in a recyclerview.
     * if it is unable to fetch the requested information it catches these exceptions and sets fetched state to be false.
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
                    fetcher.fetchForecast();
                    locationsToDisplay.add(mLocationToDisplay);
                }
            }catch (IOException ioe){
                mIsDataFetchedOk = false;

            }catch (JSONException joe){
                mIsDataFetchedOk = false;
            }
            return null;
        }


        /**
         * Updates the UI, displaying a recyclerview with favourites if the fetch was successful.
         * if the fetch was not successful, instead shows an errorfragment.
         */
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