package com.example.ibuprofen.RestaurantFlow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.ibuprofen.R;

public class RestaurantManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_manager);


        final FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment nextFragment = new FilterFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flRestaurant, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
