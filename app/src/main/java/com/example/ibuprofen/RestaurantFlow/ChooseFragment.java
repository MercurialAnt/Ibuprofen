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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    public synchronized void change_counts(Event currentEvent) {
        String newJson = null;
        String options = currentEvent.getOptions();
        try {
            JSONArray places = new JSONArray(options);
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                // get current count
                int curr = place.getInt("count");
                int my_vote = adapter.counters[i];
                place.put("count", curr + my_vote);

                if (my_vote == 1) {
                    //update people who voted for this restaurant
                    ParseUser user = ParseUser.getCurrentUser();
                    JSONObject person = new JSONObject();
                    person.put("name", user.getUsername());
                    ParseFile file = (ParseFile)user.get("profilePic");
                    if (file != null) {
                        person.put("image", file.getUrl());
                    } else {
                        person.put("image", R.drawable.ic_launcher_foreground);
                    }

                    if (place.has("people")) {
                        JSONArray people = place.getJSONArray("people");
                        people.put(person);
                        place.put("people", people);
                    } else {
                        JSONArray people = new JSONArray();
                        people.put(person);
                        place.put("people", people);
                    }
                }

            }
            newJson = places.toString();
            currentEvent.setOptions(newJson);
            final String finalNewJson = newJson;
            currentEvent.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("event", event);
                    bundle.putString("votedOn", finalNewJson);
                    fragmentIntent(new ResultsFragment(), bundle, getFragmentManager(), true);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
