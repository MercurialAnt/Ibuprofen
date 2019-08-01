package com.example.ibuprofen.RestaurantFlow;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
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
    private FragmentManager manager;
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
        manager = getFragmentManager();

        // set up list
        mChoices = new ArrayList<>();

        // initialize vars and adapter
        event = getArguments().getParcelable("event");
        client = OkSingleton.getInstance();
        api = new YelpAPI(context);

        rvChoose = view.findViewById(R.id.rvChoose);
        btnDone = view.findViewById(R.id.btnDone);
        adapter = new ChooseAdapter(context, mChoices, rvChoose, manager, event);
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
                goToResults(event);
            }
        });

    }

    public void goToResults(Event currentEvent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("event", currentEvent);
        fragmentIntent(new ResultsFragment(), bundle, manager, false);
    }

    private void populateChoices() throws JSONException {
        ParseRelation<Restaurant> relation = event.getRelation("stores");
        relation.getQuery().findInBackground(new FindCallback<Restaurant>() {
            @Override
            public void done(List<Restaurant> objects, ParseException e) {
                Log.d("ChooseFragment", objects.size() + " places found with those filters");
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Restaurant restaurant = objects.get(i);
                        if (restaurant.getReviews() != null) {
                            addRestaurant(restaurant);
                        } else {
                            populateReviews(restaurant);
                        }
                    }
                }
            }
        });

    }

    private void populateReviews(final Restaurant restaurant) {
        Request reviewRequest = api.getReview(restaurant.getID());

        client.newCall(reviewRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("ReviewFragment", "getting the reviews failed");
                e.printStackTrace();
                addRestaurant(restaurant);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        restaurant.setReviews(obj.getJSONArray("reviews"));
                        restaurant.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    addRestaurant(restaurant);
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    private void addRestaurant(Restaurant restaurant) {

        mChoices.add(restaurant);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }


}
