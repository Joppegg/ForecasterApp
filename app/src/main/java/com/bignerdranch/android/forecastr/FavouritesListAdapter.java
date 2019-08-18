package com.bignerdranch.android.forecastr;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesListAdapter extends RecyclerView.Adapter {
    SharedPreference mSharedPreference = new SharedPreference();
    private Context mContext;
    private static final String TAG = "FavouritesListAdapter";
    private ArrayList<Location> mLocations;

    public FavouritesListAdapter(Context context, ArrayList<Location> locations){
        mContext = context;
        mLocations = locations;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favourites, parent, false);
        return  new ListViewHolder(view);
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
        private ImageView mWeatherSymbol;


        ArrayList<LocationParser> locations = mSharedPreference.getFavourites(mContext);
        public ListViewHolder(View itemView){
            super(itemView);
            mLocationTemperature = itemView.findViewById(R.id.temperature_text_favourites_list);
            mLocationName = itemView.findViewById(R.id.location_name_favourites_list);
            mWeatherSymbol = itemView.findViewById(R.id.imageview_favourites_weathersymbol);
            itemView.setOnClickListener(this);
        }

        //TODO:
        // if list is null app crashes.
        public void bindView(int position){
            Log.i(TAG, "Size: " + locations.size());
            Log.i(TAG, "Position: " + position);
            mLocationTemperature.setText(mLocations.get(position).getForeCast().get(0).getTemperature());
            mLocationName.setText(locations.get(position).getLocationName());

        }

        @Override
        public void onClick(View view) {

        }
    }

}
