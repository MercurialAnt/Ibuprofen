package com.example.ibuprofen.RestaurantFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.ibuprofen.Adapters.ChooseAdapter;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseActivity extends AppCompatActivity {

    private RecyclerView rvChoose;
    private Button btnDone;
    protected ChooseAdapter adapter;
    protected List<Restaurant> mChoices;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Only ever call `setContentView` once right at the top
        setContentView(R.layout.activity_choose);

        // set up list
        mChoices = new ArrayList<>();

        // initialize vars and adapter
        event = getIntent().getParcelableExtra("event");

        rvChoose = findViewById(R.id.rvChoose);
        btnDone = findViewById(R.id.btnDone);
        adapter = new ChooseAdapter(this, mChoices, rvChoose);
        rvChoose.setAdapter(adapter);
        rvChoose.setLayoutManager(new LinearLayoutManager(this));

        try {
            populateChoices();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToResultsIntent = new Intent(ChooseActivity.this, ResultsActivity.class);
                String newJson = change_counts(event);

                // saves event in background
                event.saveInBackground();

                // goes to next page
                goToResultsIntent.putExtra("votedOn", newJson);
                goToResultsIntent.putExtra("event", event);
                startActivity(goToResultsIntent);
            }
        });
    }

    public synchronized String change_counts(Event currentEvent) {
        String newJson = null;
        String options = currentEvent.getOptions();
        try {
            JSONArray places = new JSONArray(options);
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                // get current count
                int curr = place.getInt("count");
                place.put("count", curr + adapter.counters[i]);
            }
            newJson = places.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newJson;
    }

    private void populateChoices() throws JSONException {
        JSONArray options = new JSONArray(event.getOptions());
        for (int i = 0; i < options.length(); i++) {
            Restaurant restaurant = Restaurant.fromJSON((JSONObject) options.get(i));
            mChoices.add(restaurant);
            adapter.notifyDataSetChanged();
        }
    }
}