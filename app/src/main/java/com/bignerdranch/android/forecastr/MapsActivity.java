package com.bignerdranch.android.forecastr;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**does not want to
 *
 * Google Maps activity to let user choose a location to display.
 * This class will be called when we can not get the users device location dynamically.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private LatLng mSelectedPosition;
    private Button mSelectLocationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSelectLocationButton = findViewById(R.id.locationSelect);

        //callback to home activity when location is selected.
        mSelectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", mSelectedPosition);
                setResult(FragmentActivity.RESULT_OK, returnIntent);
                finish();

            }
        });


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i(TAG, "Latlng: " + latLng.latitude + " " +  latLng.longitude);

                mSelectedPosition = latLng;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(mSelectedPosition));
                mSelectLocationButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                mSelectLocationButton.setEnabled(true);

            }
        });

        //Set up Southwest and Northeast corners
        LatLng southWest = new LatLng(55, 10);
        LatLng northEast = new LatLng(69,  25);

        //Build bounds
        LatLngBounds bounds = new LatLngBounds.Builder().include(southWest).include(northEast).build();
        //int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);

        //specify dimensions
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        //Animate camera

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(update);

    }

    // If user presses back, thus not selecting a location.
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(MapsActivity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
