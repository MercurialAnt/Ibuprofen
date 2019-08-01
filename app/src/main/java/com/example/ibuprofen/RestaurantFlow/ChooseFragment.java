package com.example.ibuprofen.RestaurantFlow;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ibuprofen.Adapters.ChooseAdapter;
import com.example.ibuprofen.Controllers.SwipeController;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
import com.example.ibuprofen.YelpAPI;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ibuprofen.RestaurantFlow.FilterFragment.fragmentIntent;

public class ChooseFragment extends Fragment {

    private RecyclerView rvChoose;
    private Button btnDone;
    protected ChooseAdapter adapter;
    protected List<Restaurant> mChoices;
    private Context context;
    private YelpAPI api;
    private OkSingleton client;
    Event event;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        // set up list
        mChoices = new ArrayList<>();

        // initialize vars and adapter
        event = getArguments().getParcelable("event");
        client = OkSingleton.getInstance();
        api = new YelpAPI(context);

        rvChoose = view.findViewById(R.id.rvChoose);
        btnDone = view.findViewById(R.id.btnDone);
        adapter = new ChooseAdapter(context, mChoices, rvChoose);
        rvChoose.setAdapter(adapter);
        rvChoose.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        SwipeController swipeController = new SwipeController(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvChoose);

        try {
            populateChoices();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_counts(event);
            }
        });

    }

    public void change_counts(Event currentEvent) {
        Bundle bundle = new Bundle();
        bundle.putIntArray("votes", adapter.counters);
        String [] people = new String[10];

        ParseUser user = ParseUser.getCurrentUser();
        ParseFile file = (ParseFile)user.get("profilePic");
        for (int i = 0; i < adapter.counters.length; i++) {
            if (adapter.counters[i] == 1) {
                //update people who voted for this restaurant
                JSONObject person = new JSONObject();
                try {
                    person.put("name", user.getUsername());
                    if (file != null) {
                        person.put("image", file.getUrl());
                    } else {
                        person.put("image", R.drawable.ic_launcher_foreground);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                people[i] = person.toString();
            }
        }
        bundle.putString("id", currentEvent.getRestaurantId());
        bundle.putStringArray("my_info", people);
        fragmentIntent(new ResultsFragment(), bundle, getFragmentManager(), false);

    }

    private void populateChoices() throws JSONException {
        JSONArray options = new JSONArray(event.getOptions());
        Log.d("ChooseFragment", options.length() + " places found with those filters");
        for (int i = 0; i < options.length(); i++) {
            JSONObject obj = (JSONObject)options.get(i);
            if (!obj.has("reviews")) {
                populateReviews(obj);
            } else {
                addRestaurant(obj);
            }

        }
        event.setOptions(options.toString());
        event.saveInBackground();
    }

    private void populateReviews(final JSONObject object) {
        Request reviewRequest = api.getReview(object.optString("id"));

        client.newCall(reviewRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("ReviewFragment", "getting the reviews failed");
                e.printStackTrace();
                addRestaurant(object);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        String reviews  = obj.getJSONArray("reviews").toString();
                        object.put("reviews", reviews);
                        addRestaurant(object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void addRestaurant(JSONObject obj) {

        try {
            Restaurant restaurant = Restaurant.fromJSON(obj);
            mChoices.add(restaurant);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
