package com.bignerdranch.android.forecastr;


import android.os.Bundle;

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
 * A simple {@link Fragment} subclass.
 */
public class SearchSeekbarFragment extends Fragment {

    private int [] mDrawableResources;
    private Location mLocation;
    private LocationForecast mLocationForecast;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDrawableResources = new int[27];
        initiateImageViews();
        super.onCreate(savedInstanceState);
        //Do all data here for the Seekbar

    }

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

        //setSeekBarValues(0);


        return v;

    }


    /**
     * This method looks at the current location used and binds the Discrete seekbar to update
     * accordingly.
     *
     */
    private void setSeekBarValues(int i){

        mTextViewTemperature.setText(mLocation.getForeCast().get(i).getTemperature() + " C°");
        mTextViewWindSpeed.setText("Windspeed: " + mLocation.getForeCast().get(i).getWindSpeed() + "m/s");
        mTextViewSeekBarTime.setText(mLocation.getForeCast().get(i).getValidTime());
        int weatherSymbol = Integer.valueOf(mLocation.getForeCast().get(i).getWeatherSymbol());
        weatherSymbol--;
        mImageViewForecast.setImageDrawable(getResources().getDrawable(mDrawableResources[weatherSymbol]));

    }

    public void initiateImageViews(){
        mDrawableResources[0] = R.drawable.sunny_weather1;
        mDrawableResources[1] = R.drawable.sun_cloudy_2;
        mDrawableResources[2] = R.drawable.sun_cloudy3_4;
        mDrawableResources[3] = R.drawable.sun_cloudy3_4;
        mDrawableResources[4] = R.drawable.clouds_5_6;
        mDrawableResources[5] = R.drawable.clouds_5_6;
        mDrawableResources[6] = R.drawable.fog_7;
        mDrawableResources[7] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[8] = R.drawable.heavyrain9_10_19_20_;
        mDrawableResources[9] = R.drawable.heavyrain9_10_19_20_;
        mDrawableResources[10] = R.drawable.thunder_11_21;
        mDrawableResources[11] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[12] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[13] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[14] = R.drawable.snow_15_17_25_27;
        mDrawableResources[15] = R.drawable.snow_15_17_25_27;
        mDrawableResources[16] = R.drawable.snow_15_17_25_27;
        mDrawableResources[17] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[18] = R.drawable.heavyrain9_10_19_20_;
        mDrawableResources[19] = R.drawable.heavyrain9_10_19_20_;
        mDrawableResources[20] = R.drawable.thunder_11_21;
        mDrawableResources[21] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[22] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[23] = R.drawable.lightrain_8_12_13_14_18_22_24;
        mDrawableResources[24] = R.drawable.snow_15_17_25_27;
        mDrawableResources[25] = R.drawable.snow_15_17_25_27;
        mDrawableResources[26] = R.drawable.snow_15_17_25_27;

    }

}
