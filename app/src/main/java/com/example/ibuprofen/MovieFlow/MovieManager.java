package com.example.ibuprofen.MovieFlow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantFlow.ChooseFragment;
import com.example.ibuprofen.RestaurantFlow.FilterFragment;
import com.example.ibuprofen.RestaurantFlow.ResultsFragment;

public class MovieManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_manager);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        Fragment nextFragment;
        String fragment = bundle.getString("fragment");

        // todo--update when the rest of the fragments are made
//        if (fragment.equals("ChooseMovieFragment")) {
//            nextFragment = new ChooseMovieFragment();
//        } else if(fragment.equals("ResultsMovieFragment")) {
//            nextFragment = new ResultsMovieFragment();
//        } else {
//            nextFragment = new MovieFilterFragment();
//        }

        nextFragment = new MovieFilterFragment();

        nextFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flMovie, nextFragment);
        transaction.commit();
    }
}
