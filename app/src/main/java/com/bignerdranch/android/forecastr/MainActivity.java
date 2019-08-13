package com.bignerdranch.android.forecastr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PlacesClient mPlacesClient;
    final String TAG = "forecastr.MainActivity";
    final String APIKEY = "AIzaSyCpJt91l68JY93EIxNuDaLZ8a4zgRnH5oU";
    private TextView mTextViewLocationName, mTextViewLocationLatitude, mTextViewTemperature, mTextViewWindSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewLocationName = findViewById(R.id.location_name);
        mTextViewLocationLatitude= findViewById(R.id.location_latitude);
        mTextViewTemperature = findViewById(R.id.temperature);
        mTextViewWindSpeed=findViewById(R.id.windspeed);
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
                mTextViewLocationName.setText(place.getName());
                mTextViewLocationLatitude.setText(String.valueOf(latLng.latitude));
                Log.i(TAG, "onPlaceSelected: "+ place.getName() + " " + latLng.latitude + "\n" + latLng.longitude);
                new SearchTask().execute(latLng);
            }

            @Override
            public void onError(@NonNull Status status) {

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
            mTextViewTemperature.setText(mLocationToDisplay.getForeCast().get(0).getTemperature());
            mTextViewWindSpeed.setText(mLocationToDisplay.getForeCast().get(0).getWindSpeed());
        }
    }
}
