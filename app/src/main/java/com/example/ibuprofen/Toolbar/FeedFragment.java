package com.example.ibuprofen.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibuprofen.Adapters.RestaurantsAdapter;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
import com.example.ibuprofen.YelpAPI;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class FeedFragment extends Fragment {
    private RecyclerView rvRestaurants;
    protected RestaurantsAdapter adapter;
    protected List<Restaurant> mRestaurants;
    private SwipeRefreshLayout swipeContainer;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // inserts PendingFragment
        insertNestedFragment();

        rvRestaurants = view.findViewById(R.id.rvRestaurants);
        mRestaurants = new ArrayList<>();
        adapter = new RestaurantsAdapter(getContext(), mRestaurants);
        rvRestaurants.setAdapter(adapter);
        rvRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        populateFeed();

//        //swipe refresh
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                populateFeed();
//            }
//        });
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
    }

    private void insertNestedFragment() {
        Fragment pendingFragment = new PendingFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flReviews, pendingFragment).commit();
    }

    private void populateFeed() {
        YelpAPI test = new YelpAPI(getContext());
        Location gpsLocation = test.getLocationByProvider(LocationManager.GPS_PROVIDER);
        OkHttpClient client = OkSingleton.getInstance();
        client.newCall(test.getRestaurants(gpsLocation)).enqueue(new Callback() {
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
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject store = array.getJSONObject(i);
                            store.accumulate("count", new Integer(0));
                            Restaurant restaurant = Restaurant.fromJSON(store);
                            mRestaurants.add(restaurant);
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //swipeContainer.setRefreshing(false);
            }
        });
    }
}