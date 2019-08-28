package com.bignerdranch.android.forecastr;

/**
 * Helper class to retrieve weather symbols.
 *
 */
public class WeatherSymbol {
    private int [] mDrawableResources;

    /**
     * Initiate the drawables.
     */
    public  WeatherSymbol(){
        mDrawableResources = new int[27];
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

    /**
     * Returns the imageresource.
     */
    public  int getDrawableResources(int i){
        return mDrawableResources[i-1];
    }



}
