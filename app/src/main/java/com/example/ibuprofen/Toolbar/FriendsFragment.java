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

import com.example.ibuprofen.Adapters.FriendAdapter;
import com.example.ibuprofen.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    // instance vars
    RecyclerView rvUsers;
    FriendAdapter adapter;
    List<ParseUser> users;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // initialize vars
        rvUsers = view.findViewById(R.id.rvUsers);
        users = new ArrayList<>();

        //create adapter
        adapter = new FriendAdapter(getContext(), users, null, false);
        //set the adapter on the recycler view
        rvUsers.setAdapter(adapter);
        //set layout manager on recycler view
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        queryUsers();
    }

    private void queryUsers() {
        // get all users in the database except for current user, sort by alphabetical username
        ParseQuery query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByAscending("username");

        // since it's an expensive operation you want to do this in a background thread not in the
        // same thread as the UI
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    Log.e("FriendsFragment", "query failed");
                    e.printStackTrace();
                    return;
                }
                users.addAll(objects);
                adapter.notifyDataSetChanged();
                Log.d("FriendsFragment", "number of users: " + users.size());
            }
        });
    }

}

