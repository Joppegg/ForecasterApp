package com.bignerdranch.android.forecastr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intenthome = new Intent(FavouritesActivity.this, HomeActivity.class);
                        startActivity(intenthome);
                        break;

                    case R.id.nav_search:
                        Intent intentsearch = new Intent(FavouritesActivity.this, SearchActivity.class);
                        startActivity(intentsearch);
                        break;

                    case R.id.nav_favorites:
                        break;
                }

                return false;
            }


        });

        FavouritesFragment favouritesFragment = new FavouritesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.favourites_placeholder, favouritesFragment, favouritesFragment.getTag())
                .commit();




    }
}