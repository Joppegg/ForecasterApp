package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileOutputStream;
import java.util.Arrays;

public class SearchActivity  extends AppCompatActivity {
    private Location mLocationToDisplay;
    PlacesClient mPlacesClient;
    final String TAG = "forecastr.SearchActivity";
    final String APIKEY = "AIzaSyCpJt91l68JY93EIxNuDaLZ8a4zgRnH5oU";
    private Button mSaveButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final SharedPreference mSharedPreference = new SharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSaveButton = findViewById(R.id.save_button);

        //Parses to a locationparser to facilitate serializing.
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

        //Sets the place fields when the user clicks on an autocompleted field. ie, selecting Stockholm sets all fields to sthlm-variables.
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.LAT_LNG,Place.Field.NAME));

        //Listener for selecting new place.
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();
                new SearchTask().execute(place);
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
     * Saves the location to the internal storage, to be used in "Favourites
     * param Location to be saved
     * @return true if location is saved, false if location already exists.
     */
    public boolean saveLocation(Location locationToSave){

        Location location = locationToSave;
      //  openFileOutput()

        return true;

    }

    /**
     * Updates current weather temperature, windspeed etc from the chosen lat/long.
     *
     *
     */
    private class SearchTask extends AsyncTask<Place,Void,Void> {
        private LatLng mLatLng;
        private Place mPlace;

        @Override
        protected Void doInBackground(Place... params) {
            mLocationToDisplay = new Location();
            //set location id.
            mPlace = params[0];


            mLatLng = mPlace.getLatLng();
            mLocationToDisplay.setLatitude(mLatLng.latitude);
            mLocationToDisplay.setLongitude(mLatLng.longitude);
            mLocationToDisplay.setLocationId(mPlace.getId());
            mLocationToDisplay.setLocationName(mPlace.getName());
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

            SearchForecastFragment searchForecastFragment = new SearchForecastFragment(mLocationToDisplay);
            FragmentManager fragmentManager2 = getSupportFragmentManager();
            fragmentManager2.beginTransaction()
                    .replace(R.id.forecastPlaceholder, searchForecastFragment, searchForecastFragment.getTag())
                    .commit();





        }
    }
}
