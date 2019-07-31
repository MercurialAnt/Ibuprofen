package com.example.ibuprofen.RestaurantFlow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ibuprofen.Adapters.ResultsAdapter;
import com.example.ibuprofen.MainActivity;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ResultsFragment extends Fragment {

    List<Restaurant> restaurants;
    ResultsAdapter resultsAdapter;
    RecyclerView tvResults;
    Button btnDone;
    Event event;
    ReentrantLock update = new ReentrantLock();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.results_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        update.lock();

        // initializes variables
        tvResults = view.findViewById(R.id.rvResults);
        btnDone = view.findViewById(R.id.btnDone);

        // show results
        restaurants = new ArrayList<>();
        Bundle bundle = getArguments();
        event = bundle.getParcelable("event");
        event.getVoters().add(ParseUser.getCurrentUser());
        int[] my_vote = bundle.getIntArray("votes");
        String [] my_info = bundle.getStringArray("my_info");

        try {
            JSONArray places = new JSONArray(event.getOptions());
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                JSONArray people = place.getJSONArray("people");
                if (my_info.length == 10 && my_info[i] != null) {
                    people.put(new JSONObject(my_info[i]));
                }

                place.put("count", place.getInt("count") + my_vote[i]);
                restaurants.add(Restaurant.fromJSON(place));
            }
            event.setOptions(places.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                update.unlock();
            }
        });


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

