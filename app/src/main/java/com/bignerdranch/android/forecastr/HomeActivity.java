package com.bignerdranch.android.forecastr;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity {
    private static final int REQUEST_ERROR = 0;

    private FusedLocationProviderClient mClient;
    private Button mLocateButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestPermission();

        mClient = LocationServices.getFusedLocationProviderClient(this);


        mLocateButton = findViewById(R.id.update_location_button);
        mLocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mClient.getLastLocation().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!= null){
                            TextView textView = findViewById(R.id.location_text_placeholder);
                            textView.setText(location.toString());

                        }

                    }
                });
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



}