package com.bignerdranch.android.forecastr;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_ERROR = 0;

    private FusedLocationProviderClient mClient;
    private Button mLocateButton, mGrantPermissionButton;
    private TextView mTextView;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mGrantPermissionButton = findViewById(R.id.allow_location_button);
        mTextView = findViewById(R.id.location_text_placeholder);
        requestPermission();


        mClient = LocationServices.getFusedLocationProviderClient(this);



        if (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            mTextView.setText("Grant permission to update automatically.");
        }
        //TODO move these two following methods into another method.
        else{
            Task<Location> task = mClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        Log.i(TAG, "Latitude: " + location.getLatitude());
                        Log.i(TAG, "Longitude: " + location.getLongitude());
                        new SearchTask().execute(location);
                    }
                }
            });
        }

        mGrantPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                if  (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;

                }
                mClient.getLastLocation().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!= null){
                            Log.i(TAG, "Latitude: " + location.getLatitude());
                            Log.i(TAG, "Longitude: " + location.getLongitude());
                            new SearchTask().execute(location);

                        }

                    }
                });


            }
        });

        mLocateButton = findViewById(R.id.update_location_button);
        mLocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeActivity.this, MapsActivity.class);
                startActivityForResult(i, 1);
            }
        });



        BottomNavigationView navView = findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        break;

                    case R.id.nav_search:
                        Intent intentsearch = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intentsearch);
                        break;

                    case R.id.nav_favorites:
                        Intent intentfavourites = new Intent(HomeActivity.this, FavouritesActivity.class);
                        startActivity(intentfavourites);
                        break;
                }

                return false;
            }


        });



    }


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    private class SearchTask extends AsyncTask<Location,Void,Void> {
        private boolean mIsDataFetchedOk;
        private Location mLocation;
        private com.bignerdranch.android.forecastr.Location mLocationToDisplay;
        private String mLocationName;

        @Override
        protected Void doInBackground(Location... params) {
            mIsDataFetchedOk = true;
            mLocation = params[0];

            mLocationToDisplay = new com.bignerdranch.android.forecastr.Location();

            mLocationToDisplay.setLatitude(mLocation.getLatitude());
            mLocationToDisplay.setLongitude(mLocation.getLongitude());
            ForecastFetcher fetcher = new ForecastFetcher(mLocationToDisplay);

            try {
                fetcher.printArray();
                //gets name
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> locations = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                mLocationName = locations.get(0).getAddressLine(0);
                Log.i(TAG, "cityname: " +  locations.get(0).getAddressLine(0));
            }
            catch (IOException ioe){
                mIsDataFetchedOk = false;
                Log.i(TAG, "ioexception" + ioe.getMessage());
            }catch (JSONException joe){
                mIsDataFetchedOk = false;
                Log.i(TAG, "joexception" + joe.getMessage());
            }

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
                        .replace(R.id.seekbarPlaceHolder, seekbarFragment, seekbarFragment.getTag())
                        .commit();

                SearchForecastFragment searchForecastFragment = new SearchForecastFragment(mLocationToDisplay);
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.forecastPlaceholder, searchForecastFragment, searchForecastFragment.getTag())
                        .commit();

                TextView textView = findViewById(R.id.location_text_placeholder);
                textView.setText(mLocationName);
            }
            else{
                ErrorFragment errorFragment = new ErrorFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.seekbarPlaceHolder, errorFragment, errorFragment.getTag())
                        .commitAllowingStateLoss();
            }



        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                LatLng result=data.getParcelableExtra("result");
                Location location = new Location("");
                location.setLatitude(result.latitude);
                location.setLongitude(result.longitude);
                new SearchTask().execute(location);

                Log.i(TAG, result.longitude + "it works.");
                //Get the selected locations name from the lat and long

                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> locations = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Log.i(TAG, "cityname: " +  locations.get(0).getAddressLine(0));
                    TextView textView = findViewById(R.id.location_text_placeholder);
                    textView.setText(locations.get(0).getAddressLine(0));

                }
                catch (IOException ioe){
                    Log.i(TAG, "Failed to parse data from geocoder.");
                }




            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "result canceled.");
            }
        }
    }//onActivityResult
}