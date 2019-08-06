package com.example.ibuprofen.Toolbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibuprofen.Adapters.EventAdapter;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PendingFragment extends Fragment {
    private RecyclerView rvPending;
    protected EventAdapter adapter;
    public List<Event> mPending;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pending_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPending = view.findViewById(R.id.rvPending);
        mPending = new ArrayList<>();
        adapter = new EventAdapter(getContext(), mPending, false);
        rvPending.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPending.setLayoutManager(layoutManager);

        queryPendingEvents();
    }

    private void queryPendingEvents() {
        ParseQuery<Event> postQuery = new ParseQuery<>(Event.class);
        postQuery.include(Event.KEY_USERS);
        postQuery.whereEqualTo("attendees", ParseUser.getCurrentUser());
        postQuery.whereNotEqualTo("hasVoted", ParseUser.getCurrentUser());
        // since it's an expensive operation you want to do this in a background thread not in the
        // same thread as the UI
        postQuery.orderByDescending("createdAt");
        postQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null) {
                    Log.e("ComposeActivity", "query failed");
                    e.printStackTrace();
                    return;
                }
                mPending.addAll(events);
                adapter.notifyDataSetChanged();

                // updates hasPending
                if (mPending.size() != 0) {
                    ParseUser.getCurrentUser().put("hasPending", true);
                }
                else {
                    ParseUser.getCurrentUser().put("hasPending", false);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });
    }
}