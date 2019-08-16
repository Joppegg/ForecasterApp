package com.bignerdranch.android.forecastr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastListAdapter extends RecyclerView.Adapter {
    private Location mLocation;
    private WeatherSymbol mWeatherSymbol;
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
        return mLocation.getForeCastMidDay().size();
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public WeatherSymbol mWeatherSymbol;
        private TextView mTemperature;
        private TextView mWeekday;
        private ImageView mImageView;

        public ListViewHolder(View itemView){

            super(itemView);
            mTemperature = itemView.findViewById(R.id.temperature_text_forecast);
            mWeekday = itemView.findViewById(R.id.weekday_text_forecast);
            mImageView = itemView.findViewById(R.id.thumbnail_image);
            itemView.setOnClickListener(this);
        }

        //TODO
        // CLEAN UP HERE
        public void bindView(int position){
            mWeatherSymbol = new WeatherSymbol();
            String temperature = mLocation.getForeCastMidDay().get(position).getTemperature() + "C°";
            mTemperature.setText(temperature);
            mWeekday.setText(mLocation.getForeCastMidDay().get(position).getDateTime().dayOfWeek().getAsShortText());

            int weatherSymbol = Integer.valueOf(mLocation.getForeCastMidDay().get(position).getWeatherSymbol());
            mImageView.setImageResource(mWeatherSymbol.getDrawableResources(weatherSymbol));

        }

        @Override
        public void onClick(View view) {

        }
    }
}
