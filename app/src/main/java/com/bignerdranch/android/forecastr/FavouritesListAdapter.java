package com.bignerdranch.android.forecastr;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesListAdapter extends RecyclerView.Adapter {
    SharedPreference mSharedPreference = new SharedPreference();
    private Context mContext;
    private static final String TAG = "FavouritesListAdapter";
    private ArrayList<Location> mLocations;
    private OnLocationListener mOnLocationListener;

    public FavouritesListAdapter(Context context, ArrayList<Location> locations, OnLocationListener onLocationListener){
        mContext = context;
        mLocations = locations;
        mOnLocationListener = onLocationListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourites, parent, false);
        return  new ListViewHolder(view, mOnLocationListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return mSharedPreference.getFavourites(mContext).size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mLocationName, mLocationTemperature;
        private ImageView mWeatherSymbolImageView;
        private WeatherSymbol mWeatherSymbol;
        OnLocationListener mOnLocationListener;


        ArrayList<LocationParser> locations = mSharedPreference.getFavourites(mContext);
        public ListViewHolder(View itemView, OnLocationListener onLocationListener){
            super(itemView);
            mLocationTemperature = itemView.findViewById(R.id.temperature_text_favourites_list);
            mLocationName = itemView.findViewById(R.id.location_name_favourites_list);
            mWeatherSymbolImageView = itemView.findViewById(R.id.imageview_favourites_weathersymbol);
            mOnLocationListener = onLocationListener;
            itemView.setOnClickListener(this);
        }

        //TODO:
        // if list is null app crashes.
        public void bindView(int position){
            Log.i(TAG, "Size: " + locations.size());
            Log.i(TAG, "Position: " + position);
            mWeatherSymbol = new WeatherSymbol();
            mLocationTemperature.setText(mLocations.get(position).getForeCast().get(0).getTemperature());
            mLocationName.setText(locations.get(position).getLocationName());
            int weatherSymbol = Integer.valueOf(mLocations.get(position).getForeCast().get(0).getWeatherSymbol());

            mWeatherSymbolImageView.setImageResource(mWeatherSymbol.getDrawableResources(weatherSymbol));

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
            mOnLocationListener.onLocationClicked(getAdapterPosition());
            Log.i(TAG , "Position: " + getAdapterPosition());

        }
    }


    public interface OnLocationListener {

        void onLocationClicked(int position);
    }

}
