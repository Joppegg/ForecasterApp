package com.bignerdranch.android.forecastr;

import android.content.Context;
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

/**
 * This class handles the listadapter for the favourite fragment.
 *
 */
public class FavouritesListAdapter extends RecyclerView.Adapter {
    private SharedPreference mSharedPreference = new SharedPreference();
    private Context mContext;
    private static final String TAG = "FavouritesListAdapter";
    private ArrayList<Location> mLocations;
    private OnLocationListener mOnLocationListener;

    //constructor taking the locations to be displayed., the onlocationlistener and the context.
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


        public void bindView(int position){
            Log.i(TAG, "Size: " + locations.size());
            Log.i(TAG, "Position: " + position);
            //helper class to get the correct weather symbol.
            mWeatherSymbol = new WeatherSymbol();
            mLocationTemperature.setText(mLocations.get(position).getForeCast().get(0).getTemperature());
            mLocationName.setText(locations.get(position).getLocationName());
            //parses the string value in locations to an int.
            int weatherSymbol = Integer.valueOf(mLocations.get(position).getForeCast().get(0).getWeatherSymbol());
            //sets the correct image resource.
            mWeatherSymbolImageView.setImageResource(mWeatherSymbol.getDrawableResources(weatherSymbol));
        }

        @Override
        public void onClick(View view) {
            //onclick-listener to open the correct location view.
            mOnLocationListener.onLocationClicked(getAdapterPosition());
        }
    }

    //interface in order to handle callbacks to the favourites fragment so it can launch a new activity based on what location was clicked on.
    public interface OnLocationListener {
        void onLocationClicked(int position);
    }

}
