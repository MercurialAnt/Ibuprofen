package com.example.ibuprofen.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantsAdapter;
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

import static com.parse.Parse.getApplicationContext;

public class FeedFragment extends Fragment {
    private YelpAPI client;
    private RecyclerView rvRestaurants;
    protected RestaurantsAdapter adapter;
    protected List<Restaurant> mRestaurants;
    private SwipeRefreshLayout swipeContainer;


    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvRestaurants = view.findViewById(R.id.rvRestaurants);

        //create data source
        mRestaurants = new ArrayList<>();
        //create adapter
        adapter = new RestaurantsAdapter(getContext(), mRestaurants);
        //set the adapter on the recycler view
        rvRestaurants.setAdapter(adapter);
        //set layout manager on recycler view
        rvRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        Location gpsLocation = getLocationByProvider(LocationManager.GPS_PROVIDER);
        populateFeed(gpsLocation);


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void populateFeed(Location gpsLocation) {
        YelpAPI test = new YelpAPI(getContext());
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
                            Restaurant restaurant = Restaurant.fromJSON(array.getJSONObject(i));
                            mRestaurants.add(restaurant);
                            getActivity().runOnUiThread(new Runnable() {
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
            }
        });
    }

    private Location getLocationByProvider(String provider) {
        Location location = null;

        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return location;
                }
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return location;
    }


}