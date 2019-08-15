package com.bignerdranch.android.forecastr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesListAdapter extends RecyclerView.Adapter {
    SharedPreference mSharedPreference = new SharedPreference();
    private Context mContext;


    public FavouritesListAdapter(Context context){
        mContext = context;
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

        ArrayList<Location> locations = mSharedPreference.getFavourites(mContext);
        public ListViewHolder(View itemView){
            super(itemView);
            mLocationTemperature = itemView.findViewById(R.id.temperature_text_favourites_list);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){

            mLocationTemperature.setText(locations.get(position).getLocationName());

        }

        @Override
        public void onClick(View view) {

        }
    }

}
