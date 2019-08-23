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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Displays a single location from clicking on it in favourites-view.
 * Hosts two forecast and seekbar fragment.
 * Allows for deletion from sharedpreference.
 *
 *
 */
public class LocationActivity extends AppCompatActivity {
    private Button mDeleteButton;

    public LocationActivity(){

    }

    private Location mLocation;
    private int mPosition;
    private SharedPreference mSharedPreference;
    private static final String TAG = "LocationActivity";
    private LocationParser mLocationParser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mSharedPreference = new SharedPreference();
        mDeleteButton = findViewById(R.id.delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreference.removeFavourite(getApplicationContext(),mLocationParser);
                finish();
            }
        });

        //TODO hämta den location som ska visas bara.p
        Intent i = getIntent();
        mPosition = i.getIntExtra("Position", 0);
        ArrayList<LocationParser> locations = mSharedPreference.getFavourites(getApplicationContext());
        mLocationParser = locations.get(mPosition);



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
        new SearchTask().execute(mLocationParser);



    }


    public static Intent newIntent(Context packageContext, int position){
        Intent intent = new Intent(packageContext, LocationActivity.class);
        intent.putExtra("Position", position);
        return intent;
    }

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

                fetcher.printArray();
            }
            catch (IOException ioe){
                Log.i(TAG, "ioexception");
                mIsDataFetchedOk = false;
            }catch (JSONException joe){
                Log.i(TAG, "joexception");
                mIsDataFetchedOk = false;
            }
            mLocation = mLocationToDisplay;

            return null;
        }


        //Updates the ui - TEST. move later
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
