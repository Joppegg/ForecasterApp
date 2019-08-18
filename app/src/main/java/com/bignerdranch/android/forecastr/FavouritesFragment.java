package com.bignerdranch.android.forecastr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 *
 * this class handles the implementation of the favourites list
 *
 */
public class FavouritesFragment extends Fragment {

    private ArrayList<Location> mLocations;

    public FavouritesFragment(ArrayList<Location> locations){
        mLocations = locations;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recyclerview_favourites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favourites_recyclerview);
        FavouritesListAdapter listAdapter = new FavouritesListAdapter(getActivity(), mLocations);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }
}
