package com.example.ibuprofen.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.Adapters.EventAdapter;
import com.example.ibuprofen.HomeActivity;
import com.example.ibuprofen.R;

import com.example.ibuprofen.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    // instance vars
    private Button btnSignout;
    private TextView tvUsername;
    private TextView tvName;
    private ImageView ivProfile;
    private RecyclerView rvFeed;
    private ParseUser user;
    boolean currentUser; //todo--make this applicable to viewing other profiles
    private List<Event> eventsList;
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insertNestedFragment();

        // initialize vars
        tvUsername = view.findViewById(R.id.tvCreator);
        tvName = view.findViewById(R.id.tvName);
        rvFeed = view.findViewById(R.id.rvPastEvents);
        ivProfile = view.findViewById(R.id.ivProfileImage);
        eventsList = new ArrayList<>();

        // set current user and set username, name, and image
        user = ParseUser.getCurrentUser();
        tvUsername.setText("@" + user.getUsername());
        tvName.setText(user.getString("name"));

        // set up adapter
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(getContext(), eventsList);
        rvFeed.setAdapter(adapter);

        // get past events by user
        queryUserEvents();

        // load profile image
        ParseFile profile = user.getParseFile("profilePic");
        if (profile != null) {
            Glide.with(getContext())
                    .load(profile.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
        }
        else {
            Glide.with(getContext())
                    .load("@drawable/anonymous")
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
        }

        // set on click listener for sign out button
        btnSignout = view.findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logs user out
                ParseUser.logOut();

                // goes back to sign in page
                Intent i = new Intent(getContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }

    private void insertNestedFragment() {
        Fragment pendingFragment = new PendingFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flPending, pendingFragment).commit();
    }

    private void queryUserEvents() {
        ParseQuery<Event> postQuery = new ParseQuery<>(Event.class);
        postQuery.include(Event.KEY_USERS);
        postQuery.whereEqualTo("attendees", ParseUser.getCurrentUser());
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
                eventsList.addAll(events);
                adapter.notifyDataSetChanged();
                Log.d("ProfileFragment", "number of posts: " + eventsList.size());
            }
        });
    }
}