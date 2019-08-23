package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class SearchActivity  extends AppCompatActivity {
    private Location mLocationToDisplay;
    PlacesClient mPlacesClient;
    final String TAG = "SearchActivity";
    final String APIKEY = "AIzaSyCpJt91l68JY93EIxNuDaLZ8a4zgRnH5oU";
    private Button mSaveButton;
    private Place mPlace;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final SharedPreference mSharedPreference = new SharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        mSaveButton = findViewById(R.id.save_button);

        //Parses to a locationparser to facilitate serializing to gson.
        mSaveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               LocationParser locationParserToSave = new LocationParser(mLocationToDisplay);
               if (mSharedPreference.addFavourite(getApplicationContext(), locationParserToSave)){
                   Toast.makeText(getApplicationContext(), mLocationToDisplay.getLocationName(), Toast.LENGTH_SHORT).show();

               }
               else{
                   Toast.makeText(getApplicationContext(), "Already saved.", Toast.LENGTH_SHORT).show();
               }

           }
       });


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        fragment.getView().setBackgroundColor(Color.GRAY);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), APIKEY);
        }

        mPlacesClient = Places.createClient(this);
        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setCountry("SE");
        //Sets the place fields when the user clicks on an autocompleted field. ie, selecting Stockholm sets all fields to sthlm-variables.
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));


        //Listener for selecting new place.
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                new SearchTask().execute(place);
              mPlace = place;
                mSaveButton.setEnabled(true);
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

        if (savedInstanceState != null){
            Log.i(TAG, " not null");
            mPlace = savedInstanceState.getParcelable("place");
            mLocationToDisplay = savedInstanceState.getParcelable("location");

            if (mPlace != null) {
                Log.i(TAG, mPlace.getName());
             //   new SearchTask().execute(mPlace);
            }
        }


    }


    /**
     * Updates current weather temperature, windspeed etc from the chosen lat/long.
     *
     *
     */
    private class SearchTask extends AsyncTask<Place,Void,Void> {
        private LatLng mLatLng;
        private Place mPlace;
        private boolean mIsDataFetchedOk;


        @Override
        protected Void doInBackground(Place... params) {
            mIsDataFetchedOk = true;
            mLocationToDisplay = new Location();
            //set location id.
            mPlace = params[0];

            mLatLng = mPlace.getLatLng();
            mLocationToDisplay.setLatitude(mLatLng.latitude);
            mLocationToDisplay.setLongitude(mLatLng.longitude);
            mLocationToDisplay.setLocationId(mPlace.getId());
            mLocationToDisplay.setLocationName(mPlace.getName());

            ForecastFetcher fetcher = new ForecastFetcher(mLocationToDisplay);

            try {
                fetcher.printArray();
            }
            catch (IOException ioe){
                Log.i(TAG, "ioexception");
                mIsDataFetchedOk = false;

            }
            catch (JSONException joe){
                Log.i(TAG, "jsonexception");
                mIsDataFetchedOk = false;
            }

            return null;
        }


        //Updates the ui
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mIsDataFetchedOk) {
                Log.i(TAG, mLocationToDisplay.getForeCast().get(0).getValidTime());

                SearchSeekbarFragment seekbarFragment = new SearchSeekbarFragment(mLocationToDisplay);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.seekbarLayout, seekbarFragment, seekbarFragment.getTag())
                        .commit();

                SearchForecastFragment searchForecastFragment = new SearchForecastFragment(mLocationToDisplay);
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.forecastPlaceholder, searchForecastFragment, searchForecastFragment.getTag())
                        .commit();

            }
            else{
                ErrorFragment errorFragment = new ErrorFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.seekbarLayout, errorFragment, errorFragment.getTag())
                        .commitAllowingStateLoss();

            }


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("place",mPlace);
        savedInstanceState.putParcelable("location", mLocationToDisplay);
    }
}
