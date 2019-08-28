package com.bignerdranch.android.forecastr;


import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * This fragment handles displaying a seekbar for the current locations forecast, ten hours ahead.
 * It uses a specific locations arraylist with saved LocationForecasts and puts these into a recyclerview for display.
 *
 */
public class SearchSeekbarFragment extends Fragment {
    private WeatherSymbol mWeatherSymbol;
    private int [] mDrawableResources;
    private Location mLocation;
    private SeekBar mSeekBar;
    private TextView mTextViewSeekBarTime, mTextViewTemperature, mTextViewWindSpeed;
    private ImageView mImageViewForecast;
    private String TAG = "SearchSeekbarFragment";


    public SearchSeekbarFragment() {
        // Required empty public constructor
    }

    public SearchSeekbarFragment(Location location) {
        mLocation = location;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("midday", mLocation.getForeCastMidDay());
        outState.putParcelable("location", mLocation);
        Log.i(TAG, "saving" + mLocation.getLocationName());
    }

    /**
     * Handle recovering saved instance.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState!= null){
            mLocation = savedInstanceState.getParcelable("location");
            mLocation.setForeCastMidDay(savedInstanceState.<LocationForecast>getParcelableArrayList("midday"));
        }
        //mDrawableResources = new int[27];
        //    initiateImageViews();
        mWeatherSymbol = new WeatherSymbol();
        setSeekBarValues(0);
        super.onActivityCreated(savedInstanceState);


    }


    /**
     *Handling creating and changing values in the seekbar.
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_seekbar, container, false);

        mSeekBar = v.findViewById(R.id.seekBar);
        mTextViewSeekBarTime = v.findViewById(R.id.seekbar_time);
        mTextViewTemperature = v.findViewById(R.id.seekbar_temperature);
        mTextViewWindSpeed = v.findViewById(R.id.seekbar_windspeed);
        mImageViewForecast = v.findViewById(R.id.imageview_weathersymbol);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mTextViewSeekBarTime.setText("" + progress + "");
                setSeekBarValues(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;

    }

    /**
     * This method looks at the current location used and binds the Discrete seekbar to update
     * accordingly.
     *
     */
    private void setSeekBarValues(int i){
        String temperature = mLocation.getForeCast().get(i).getTemperature() + " C°";
        String windSpeed =  "Windspeed: " + mLocation.getForeCast().get(i).getWindSpeed() + " m/s";
        String dateAndTime = mLocation.getForeCast().get(i).getDateTime().dayOfWeek().getAsText() + " " + mLocation.getForeCast().get(i).getDayAndHour();
        mTextViewTemperature.setText(temperature);
        mTextViewWindSpeed.setText(windSpeed);
        mTextViewSeekBarTime.setText(dateAndTime);

        //Set the weather symbol
        int weatherSymbol = Integer.valueOf(mLocation.getForeCast().get(i).getWeatherSymbol());
        mImageViewForecast.setImageResource(mWeatherSymbol.getDrawableResources(weatherSymbol));

    }

}
