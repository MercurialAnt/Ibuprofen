package com.example.ibuprofen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.ibuprofen.Adapters.ResultsAdapter;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    // instance variables
    List<Restaurant> restaurants;
    ResultsAdapter resultsAdapter;
    RecyclerView tvResults;
    Button btnDone;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // sets up layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // gets intent and uploads results to database
        Intent intent = getIntent();
        event = getIntent().getParcelableExtra("event");
        String jsonResults = intent.getStringExtra("votedOn");
        event.setOptions(jsonResults);
        event.getVoters().add(ParseUser.getCurrentUser());
        event.saveInBackground();

        // initializes variables
        tvResults = findViewById(R.id.rvResults);
        btnDone = findViewById(R.id.btnDone);

        // shows results
        restaurants = new ArrayList<>();
        JSONArray places;
        try {
            places = new JSONArray(jsonResults);
            for (int i = 0; i < places.length(); i++) {
                restaurants.add(Restaurant.fromJSON(places.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        resultsAdapter = new ResultsAdapter(this, restaurants);

        tvResults.setAdapter(resultsAdapter);
        tvResults.setLayoutManager(new LinearLayoutManager(this));

       btnDone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent finishedIntent = new Intent(ResultsActivity.this, MainActivity.class);
               startActivity(finishedIntent);
               finish();
           }
       });
    }
}
