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
 * this class handles the implementation of the favourites list
 *
 */
public class FavouritesFragment extends Fragment  implements FavouritesListAdapter.OnLocationListener {

    private ArrayList<Location> mLocations;
    private FavouritesListAdapter mListAdapter;
    private static final String TAG = "FavouritesFragment";
    private SharedPreference mSharedPreference = new SharedPreference();

    public FavouritesFragment(ArrayList<Location> locations){
        mLocations = locations;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recyclerview_favourites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favourites_recyclerview);
         mListAdapter = new FavouritesListAdapter(getActivity(), mLocations, this);
        recyclerView.setAdapter(mListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Adds a simple divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        return view;

    }


    //starts new activity from click.
    @Override
    public void onLocationClicked(int position) {
        Intent intent = LocationActivity.newIntent(getActivity(), position);
        startActivity(intent);


    }
}
