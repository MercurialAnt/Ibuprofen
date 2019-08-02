package com.example.ibuprofen.RestaurantFlow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ibuprofen.Adapters.EndlessRecyclerViewScrollListener;
import com.example.ibuprofen.Adapters.ResultsAdapter;
import com.example.ibuprofen.MainActivity;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class ResultsFragment extends Fragment {
    ResultsAdapter resultsAdapter;
    RecyclerView tvResults;
    Button btnDone;
    private SwipeRefreshLayout scResults;
    private EndlessRecyclerViewScrollListener scrollListener;
    Event event;
    Activity mActivity;
    FragmentManager manager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        manager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.results_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // initializes variables
        tvResults = view.findViewById(R.id.rvResults);
        btnDone = view.findViewById(R.id.btnDone);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // show results
        Bundle bundle = getArguments();
        event = bundle.getParcelable("event");
        event.getVoters().add(ParseUser.getCurrentUser());
        event.saveInBackground();
        fetchResults();

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                fetchResults();
            }
        };

        tvResults.addOnScrollListener(scrollListener);

        //swipe refresh
        scResults = view.findViewById(R.id.scResults);
        scResults.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchResults();
            }
        });
        scResults.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent finishedIntent = new Intent(getContext(), MainActivity.class);
                startActivity(finishedIntent);
                mActivity.finish();
            }
        });
    }

    public void fetchResults() {
        ParseRelation<Restaurant> places = event.getRelation("stores");
        places.getQuery().findInBackground(new FindCallback<Restaurant>() {
            @Override
            public void done(List<Restaurant> objects, ParseException e) {
                if (e == null) {
                    resultsAdapter = new ResultsAdapter(getContext(), objects);

                    tvResults.setAdapter(resultsAdapter);
                    tvResults.setLayoutManager(new LinearLayoutManager(getContext()));
                } else {
                    e.printStackTrace();
                }
                scResults.setRefreshing(false);
            }
        });
    }
}

