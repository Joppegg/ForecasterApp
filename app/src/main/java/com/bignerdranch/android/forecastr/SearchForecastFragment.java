package com.bignerdranch.android.forecastr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
            Log.i(TAG, "savedinstancestate is not null");
            Log.i(TAG, "test miday");
            Log.i(TAG, mLocation.getLocationName() + " name");
        }

        View view = inflater.inflate(R.layout.fragment_forecast_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.forecastListRecyclerView);

        ForecastListAdapter listAdapter = new ForecastListAdapter(mLocation);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }

/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState!= null) {
            mLocation = savedInstanceState.getParcelable("location");
            mLocation.setForeCastMidDay(savedInstanceState.<LocationForecast>getParcelableArrayList("midday"));
            Log.i(TAG, "savedinstancestate is not null");
            Log.i(TAG, "test miday");
            Log.i(TAG, mLocation.getLocationName() + " name");
        }



    }*/

    public void updateUI(){


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("midday", mLocation.getForeCastMidDay());
        outState.putParcelable("location", mLocation);
        Log.i(TAG, "saving" + mLocation.getLocationName());
    }
}
