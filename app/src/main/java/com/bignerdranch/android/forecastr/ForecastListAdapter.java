package com.bignerdranch.android.forecastr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastListAdapter extends RecyclerView.Adapter {
    private Location mLocation;
    public ForecastListAdapter(Location location){
        mLocation = location;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_forecast, parent, false);
       return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mLocation.getForeCast().size();
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTemperature;

        public ListViewHolder(View itemView){

            super(itemView);
            mTemperature = itemView.findViewById(R.id.temperature_text_forecast);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            mTemperature.setText(mLocation.getForeCast().get(position).getTemperature());
        }

        @Override
        public void onClick(View view) {

        }
    }
}
