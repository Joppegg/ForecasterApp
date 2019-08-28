package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 *
 * This class handles the implementation of the favourites list.
 *
 */
public class FavouritesFragment extends Fragment  implements FavouritesListAdapter.OnLocationListener {

    private ArrayList<Location> mLocations;
    private FavouritesListAdapter mListAdapter;

    public FavouritesFragment(){

    }

    //On saving instance state, puts the arraylist with locations to be displayed in the bundle.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("locations", mLocations);

    }

    //Constructor taking an arraylist with the chosen locations.
    public FavouritesFragment(ArrayList<Location> locations){
        mLocations = locations;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //if there is a saved instance state, fetches the locations from it.
        if (savedInstanceState!=null) {
            mLocations = savedInstanceState.getParcelableArrayList("locations"); }

        View view =  inflater.inflate(R.layout.recyclerview_favourites, container, false);

        //Creates a recyclerview to display the locations.
        RecyclerView recyclerView = view.findViewById(R.id.favourites_recyclerview);
        mListAdapter = new FavouritesListAdapter(getActivity(), mLocations, this);
        recyclerView.setAdapter(mListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Adds a simple divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    //starts new activity from click.
    @Override
    public void onLocationClicked(int position) {
        Intent intent = LocationActivity.newIntent(getActivity(), position);
        startActivity(intent);
    }
}
