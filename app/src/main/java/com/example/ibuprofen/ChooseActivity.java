package com.example.ibuprofen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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

        mChoices = new ArrayList<>();
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
            }
        });
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
