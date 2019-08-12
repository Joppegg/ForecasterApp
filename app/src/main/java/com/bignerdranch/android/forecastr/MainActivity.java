package com.bignerdranch.android.forecastr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
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
    final String APIKEY = "AIzaSyDt18yLpPPTSCkI26bimMNjl0mGvUcnd6s";
    private TextView mTextViewLocationName, mTextViewLocationLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextViewLocationName = findViewById(R.id.location_name);
        mTextViewLocationLatitude= findViewById(R.id.location_latitude);
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
                ForecastFetcher fetcher = new ForecastFetcher();
                new SearchTask().execute();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });



    }

    private class SearchTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {


                ForecastFetcher fetcher = new ForecastFetcher();
                fetcher.printArray();


            return null;

        }

    }
}
