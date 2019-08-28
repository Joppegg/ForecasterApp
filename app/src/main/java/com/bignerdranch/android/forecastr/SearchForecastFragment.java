package com.bignerdranch.android.forecastr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This fragment handles displaying the coming forecast for a specific location.
 * It uses a specific locations arraylist with saved LocationForecasts and puts these into a recyclerview for display.
 *
 */
public class SearchForecastFragment extends Fragment {
    private static final String TAG = "SearchForecastFragment";
    private Location mLocation;

    public SearchForecastFragment(){

    }

    public SearchForecastFragment(Location location){
        mLocation = location;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!= null) {
            mLocation = savedInstanceState.getParcelable("location");
            mLocation.setForeCastMidDay(savedInstanceState.<LocationForecast>getParcelableArrayList("midday"));
        }

        View view = inflater.inflate(R.layout.fragment_forecast_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.forecastListRecyclerView);

        //Sets adapter and layoutmanager
        ForecastListAdapter listAdapter = new ForecastListAdapter(mLocation);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }


    //Handling onsaveinstance
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("midday", mLocation.getForeCastMidDay());
        outState.putParcelable("location", mLocation);
    }
}
