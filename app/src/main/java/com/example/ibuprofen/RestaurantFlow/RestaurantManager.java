package com.example.ibuprofen.RestaurantFlow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.ibuprofen.R;

public class RestaurantManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_manager);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        Fragment nextFragment;
        String fragment = bundle.getString("fragment");

        if (fragment.equals("ChooseFragment")) {
            nextFragment = new ChooseFragment();
        } else if(fragment.equals("ResultsFragment")) {
            nextFragment = new ResultsFragment();
        } else {
//            getSupportActionBar().hide();
            nextFragment = new FilterFragment();
        }

        nextFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flRestaurant, nextFragment);
        transaction.commit();
    }
}
