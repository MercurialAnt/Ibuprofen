package com.example.ibuprofen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    List<Restaurant> restaurants;
    ResultsAdapter resultsAdapter;
    RecyclerView result_rv;
    Button done_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        result_rv = findViewById(R.id.results_rv);
        done_btn = findViewById(R.id.done_btn);

        restaurants = new ArrayList<>();
        Intent intent = getIntent();
        JSONArray places;
        String jsonResults = intent.getStringExtra("votedOn");
        try {
            places = new JSONArray(jsonResults);
            for (int i = 0; i < places.length(); i++) {
                restaurants.add(Restaurant.fromJSON(places.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultsAdapter = new ResultsAdapter(this, restaurants);

        result_rv.setAdapter(resultsAdapter);
        result_rv.setLayoutManager(new LinearLayoutManager(this));

       done_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent finishedIntent = new Intent(ResultsActivity.this, MainActivity.class);
               startActivity(finishedIntent);
               finish();
           }
       });
    }
}
