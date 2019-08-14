package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

public class SearchActivity  extends AppCompatActivity {
    PlacesClient mPlacesClient;
    final String TAG = "forecastr.SearchActivity";
    final String APIKEY = "AIzaSyCpJt91l68JY93EIxNuDaLZ8a4zgRnH5oU";
    private TextView mTextViewLocationName, mTextViewLocationLatitude, mTextViewTemperature, mTextViewWindSpeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        fragment.getView().setBackgroundColor(Color.GRAY);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), APIKEY);
        }

        mPlacesClient = Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.autocomplete_fragment);

        //Sets the place fields when the user clicks on an autocompleted field. ie, selecting Stockholm sets all fields to sthlm-variables.
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        //Listener for selecting new place.
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                new SearchTask().execute(latLng);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intenthome = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(intenthome);
                        break;

                    case R.id.nav_search:

                        break;

                    case R.id.nav_favorites:
                        Intent intentfavourites = new Intent(SearchActivity.this, FavouritesActivity.class);
                        startActivity(intentfavourites);
                        break;
                }

                return false;
            }


        });


    }


    /**
     * Updates current weather temperature, windspeed etc from the chosen lat/long.
     *
     *
     */
    private class SearchTask extends AsyncTask<LatLng,Void,Void> {
        private LatLng mLatLng;
        private Location mLocationToDisplay;

        @Override
        protected Void doInBackground(LatLng... params) {
            mLocationToDisplay = new Location();
            mLatLng = params[0];
            mLocationToDisplay.setLatitude(mLatLng.latitude);
            mLocationToDisplay.setLongitude(mLatLng.longitude);
            ForecastFetcher fetcher = new ForecastFetcher(mLocationToDisplay);
            fetcher.printArray();

            return null;
        }


        //Updates the ui - TEST. move later
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            SearchSeekbarFragment seekbarFragment = new SearchSeekbarFragment(mLocationToDisplay);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.seekbarLayout, seekbarFragment,seekbarFragment.getTag())
                    .commit();

        }
    }
}
