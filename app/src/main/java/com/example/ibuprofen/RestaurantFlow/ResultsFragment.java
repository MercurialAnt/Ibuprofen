package com.example.ibuprofen.RestaurantFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ibuprofen.Adapters.ResultsAdapter;
import com.example.ibuprofen.MainActivity;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends Fragment {

    List<Restaurant> restaurants;
    ResultsAdapter resultsAdapter;
    RecyclerView tvResults;
    Button btnDone;
    Event event;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.results_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        Bundle bundle = getArguments();
        event = bundle.getParcelable("event");
        String jsonResults = bundle.getString("votedOn");
        event.setOptions(jsonResults);
        event.getVoters().add(ParseUser.getCurrentUser());
        event.saveInBackground();

        // initializes variables
        tvResults = view.findViewById(R.id.rvResults);
        btnDone = view.findViewById(R.id.btnDone);

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

        resultsAdapter = new ResultsAdapter(getContext(), restaurants);

        tvResults.setAdapter(resultsAdapter);
        tvResults.setLayoutManager(new LinearLayoutManager(getContext()));

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finishedIntent = new Intent(getContext(), MainActivity.class);
                startActivity(finishedIntent);
                getActivity().finish();
            }
        });

    }

}

