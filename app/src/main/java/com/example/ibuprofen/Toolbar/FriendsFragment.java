package com.example.ibuprofen.Toolbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    boolean searchUsed = false;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflates menu
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // sets up search view listener
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.equals("")) {
                    queryUsers();
                    searchUsed = true;
                    return true;
                }
                querySearchedUsers(query, true);
                searchUsed = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("") && searchUsed) {
                    queryUsers();
                    return false;
                }
                else if (!newText.equals("")) {
                    querySearchedUsers(newText, false);
                    searchUsed = true;
                    return false;
                }
                return false;
            }
        });
    }

    private void queryUsers() {
        // remove everything from users list
        users.removeAll(users);
        adapter.notifyDataSetChanged();

        // get all users in the database except for current user, sort by alphabetical username
        ParseQuery query = ParseUser.getQuery();
        query.orderByAscending("name");
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

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
            }
        });
    }

    private void querySearchedUsers(String search, boolean submitClicked) {
        if (search.equals("") && submitClicked) {
            // means that the search got canceled
            users.removeAll(users);
            adapter.notifyDataSetChanged();
            queryUsers();
        }
        else if (!search.equals("")){
            List<ParseUser> temp = new ArrayList<>(); // keeps track of users that match search results
            List<String> tempIds = new ArrayList<>(); // keeps track of ids
            for (ParseUser i : users) {
                if ((i.getUsername().contains(search)) || (i.getString("name").contains(search))) {
                    if (!tempIds.contains(i.getObjectId())) {
                        temp.add(i);
                        tempIds.add(i.getObjectId());
                    }
                }
            }
            users.removeAll(users);
            adapter.notifyDataSetChanged();
            users.addAll(temp);
            adapter.notifyDataSetChanged();
        }
    }


}

