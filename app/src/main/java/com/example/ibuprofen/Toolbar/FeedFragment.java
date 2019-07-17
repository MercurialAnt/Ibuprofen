package com.example.ibuprofen.Toolbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantsAdapter;
import com.example.ibuprofen.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
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
        //loadTopPosts();

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
}
