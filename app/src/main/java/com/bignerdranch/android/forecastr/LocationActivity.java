package com.bignerdranch.android.forecastr;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Displays a single location from clicking on it in favourites-view.
 * Hosts a forecast and seekbar fragment.
 * Allows for deletion of Location from sharedpreference.
 */
public class LocationActivity extends AppCompatActivity {
    public LocationActivity(){

    }
    private Location mLocation;
    private SharedPreference mSharedPreference;
    private static final String TAG = "LocationActivity";
    private LocationParser mLocationParser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mSharedPreference = new SharedPreference();

        //On pressing delete, removes the selected location from the shared preference list where favourite locations are stored and destroys activity.
        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreference.removeFavourite(getApplicationContext(),mLocationParser);
                finish();
            }
        });

        //Gets the location that was clicked on in the Forecast Activity and displays the forecast for it.
        Intent i = getIntent();
        int position = i.getIntExtra("Position", 0);
        ArrayList<LocationParser> locations = mSharedPreference.getFavourites(getApplicationContext());
        mLocationParser = locations.get(position);


        //Bottom navigation view.
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intenthome = new Intent(LocationActivity.this, HomeActivity.class);
                        startActivity(intenthome);
                        break;

                    case R.id.nav_search:
                        Intent intentsearch = new Intent(LocationActivity.this, SearchActivity.class);
                        startActivity(intentsearch);
                        break;

                    case R.id.nav_favorites:
                        break;
                }

                return false;
            }


        });
        //Displays the selected location.
        new SearchTask().execute(mLocationParser);

    }


    /**
     * Handles getting the position to use for fetching the location.
     * @param packageContext the context.
     * @param position the position in the Forecast view.
     * @return an intent to be started.
     */
    public static Intent newIntent(Context packageContext, int position){
        Intent intent = new Intent(packageContext, LocationActivity.class);
        intent.putExtra("Position", position);
        return intent;
    }

    /**
     * Fetches the forecast information from the selected location
     * if it is unable to fetch the requested information it catches these exceptions and sets fetched state to be false.
     *
     */
    private class SearchTask extends AsyncTask<LocationParser,Void,Void> {
        private Location mLocationToDisplay;
        private LocationParser mLocationParser;
        private boolean mIsDataFetchedOk;

        @Override
        protected Void doInBackground(LocationParser... params) {
            mIsDataFetchedOk = true;
            mLocationToDisplay = new Location();
            //set location id.
            mLocationParser = params[0];
            mLocationToDisplay.setLatitude(mLocationParser.getLatitude());
            mLocationToDisplay.setLongitude(mLocationParser.getLongitude());
            mLocationToDisplay.setLocationId(mLocationParser.getLocationId());
            mLocationToDisplay.setLocationName(mLocationParser.getLocationName());
            ForecastFetcher fetcher = new ForecastFetcher(mLocationToDisplay);
            try {
                fetcher.fetchForecast();
            }
            catch (IOException ioe){
                mIsDataFetchedOk = false;
            }catch (JSONException joe){
                mIsDataFetchedOk = false;
            }
            mLocation = mLocationToDisplay;
            return null;
        }


        /**
         * Updates the UI, displaying a recyclerview with favourites if the fetch was successful.
         * if the fetch was not successful, instead shows an errorfragment.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mIsDataFetchedOk) {
                SearchSeekbarFragment seekbarFragment = new SearchSeekbarFragment(mLocationToDisplay);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.location_seekbarLayout, seekbarFragment, seekbarFragment.getTag())
                        .commit();

                SearchForecastFragment searchForecastFragment = new SearchForecastFragment(mLocationToDisplay);
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.location_forecastPlaceholder, searchForecastFragment, searchForecastFragment.getTag())
                        .commit();
            }
            else{
                ErrorFragment errorFragment = new ErrorFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.location_seekbarLayout, errorFragment, errorFragment.getTag())
                        .commitAllowingStateLoss();

            }






        }
    }
}
