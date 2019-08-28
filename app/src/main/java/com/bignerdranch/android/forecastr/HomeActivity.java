package com.bignerdranch.android.forecastr;

import android.app.Activity;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * This class handles the Home activity.
 * It asks for permission to access the users location and updates the forecast after being granted it.
 * It also allows the user to launch a maps activity, where the user can select a location from a map and be shown
 * a forecast for it.
 *
 */
public class HomeActivity extends AppCompatActivity {
    private FusedLocationProviderClient mClient;
    private static final String TAG = "HomeActivity";
    private Location mLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button grantPermissionButton = findViewById(R.id.allow_location_button);
        TextView textView = findViewById(R.id.location_text_placeholder);

        //Asks for location permission on startup.
        requestPermission();
        mClient = LocationServices.getFusedLocationProviderClient(this);

        //Checks if permission has been given
        //if the permission is granted on startup, automatically update the forecast for the location.
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Task<Location> task = mClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        new SearchTask().execute(location);
                    }
                }
            });
        }


        //asks for permission on clicking update location button.
        grantPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                if  (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                //if the location is granted, display the forecast for the users location.
                mClient.getLastLocation().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!= null){
                            new SearchTask().execute(location);
                        }
                    }
                });
            }
        });

        //if the users presses the select location button, starts the Maps Activity in order to show a selected location.
        Button locateButton = findViewById(R.id.update_location_button);
        locateButton.setOnClickListener(new View.OnClickListener() {
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
    //Method for requesting permission.
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    /**
     * Fetches the forecast information from the saved location and displays it.
     * if it is unable to fetch the requested information it catches these exceptions and sets fetched state to be false.
     *
     */
    private class SearchTask extends AsyncTask<Location,Void,Void> {
        private boolean mIsDataFetchedOk;
        private Location mLocation;
        private com.bignerdranch.android.forecastr.Location mLocationToDisplay;
        private String mLocationName;

        //Fetches the location to be displayed and sets the latitude and longitude to be used.
        @Override
        protected Void doInBackground(Location... params) {
            mIsDataFetchedOk = true;
            mLocation = params[0];
            mLocationToDisplay = new com.bignerdranch.android.forecastr.Location();
            mLocationToDisplay.setLatitude(mLocation.getLatitude());
            mLocationToDisplay.setLongitude(mLocation.getLongitude());

            //Fetches the location.
            ForecastFetcher fetcher = new ForecastFetcher(mLocationToDisplay);

            try {
                fetcher.fetchForecast();
                //Gets the location name, postal code and country using geocoder.
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> locations = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                mLocationName = locations.get(0).getAddressLine(0);
            }
            catch (IOException ioe){
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

                ErrorFragment errorFragment2 = new ErrorFragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.forecastPlaceholder, errorFragment2, errorFragment2.getTag())
                        .commitAllowingStateLoss();
            }



        }
    }

    /**
     * Handles the callback from MapsActivity, setting the parameters from a selected location in google maps
     * and fetching the forecast for it.
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //Get the result from extras.
                LatLng result=data.getParcelableExtra("result");
                Location location = new Location("");
                location.setLatitude(result.latitude);
                location.setLongitude(result.longitude);
                new SearchTask().execute(location);

                //This updates the location name with the help of geocoder.
                try {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> locations = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    TextView textView = findViewById(R.id.location_text_placeholder);
                    textView.setText(locations.get(0).getAddressLine(0));
                }
                catch (IOException ioe){
                    Log.i(TAG, "Failed to parse data from geocoder.");
                }
            }
            //if the users presses the back button.

            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "result canceled.");
            }
        }
    }
}