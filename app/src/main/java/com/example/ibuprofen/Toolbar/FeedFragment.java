package com.example.ibuprofen.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibuprofen.Adapters.EndlessRecyclerViewScrollListener;
import com.example.ibuprofen.Adapters.RestaurantsAdapter;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
import com.example.ibuprofen.API.YelpAPI;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FeedFragment extends Fragment {
    private RecyclerView rvRestaurants;
    protected RestaurantsAdapter restaurantsAdapter;
    protected List<Restaurant> mRestaurants;
    private SwipeRefreshLayout scFeed;
    protected Activity mActivity;
    private EndlessRecyclerViewScrollListener scrollListener;
    private OkSingleton client;
    private YelpAPI api;
    private Context context;
    private int total_offset;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        client = OkSingleton.getInstance();
        api = new YelpAPI(context);
        this.context = context;
    }

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rvRestaurants = view.findViewById(R.id.rvRestaurants);
        mRestaurants = new ArrayList<>();
        restaurantsAdapter = new RestaurantsAdapter(context, mRestaurants);
        rvRestaurants.setAdapter(restaurantsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvRestaurants.setLayoutManager(linearLayoutManager);
        restaurantsAdapter.notifyDataSetChanged();
        populateFeed(total_offset + "");

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadFromParse();
            }
        };

        rvRestaurants.addOnScrollListener(scrollListener);

        //swipe refresh
        scFeed = view.findViewById(R.id.scFeed);
        scFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restaurantsAdapter.clear();
                total_offset = 0;
                scrollListener.resetState();
                populateFeed(total_offset + "");
            }
        });
        scFeed.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void loadFromParse() {
        populateFeed(total_offset + "");
    }

    private void populateFeed(final String offset) {
        Location gpsLocation = api.getLocationByProvider(LocationManager.GPS_PROVIDER);
        client.newCall(api.getRestaurants(gpsLocation, offset)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Feed", "Did not work");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray array = obj.getJSONArray("businesses");
                        total_offset += array.length();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject store = array.getJSONObject(i);
                            Restaurant restaurant = Restaurant.fromJSON(store);
                            mRestaurants.add(restaurant);
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    restaurantsAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                scFeed.setRefreshing(false);
            }
        });
    }
}