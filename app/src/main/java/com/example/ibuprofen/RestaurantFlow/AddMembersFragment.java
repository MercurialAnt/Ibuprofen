package com.example.ibuprofen.RestaurantFlow;

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
import android.widget.Button;
import android.widget.Toast;

import com.example.ibuprofen.Adapters.FriendAdapter;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.ibuprofen.RestaurantFlow.FilterFragment.fragmentIntent;

public class AddMembersFragment extends Fragment {

    RecyclerView rvUsers;
    FriendAdapter adapter;
    List<ParseUser> users;
    Button btnNext;
    Event event;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_members_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvUsers = view.findViewById(R.id.rvUsers);
        btnNext = view.findViewById(R.id.btnNext);
        users = new ArrayList<>();
        event = getArguments().getParcelable("event");

        //create adapter
        adapter = new FriendAdapter(getContext(), users, event, true);
        //set the adapter on the recycler view
        rvUsers.setAdapter(adapter);
        //set layout manager on recycler view
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        // fill list with possible attendees
        queryUsers();

        // set onClick listener for next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.saved) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("event", event);

                    fragmentIntent(new ChooseFragment(), bundle, getFragmentManager(), false);
                }
                else {
                    Toast.makeText(getContext(), "Loading, please try again in a second.", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                    Log.e("AddMembersActivity", "query failed");
                    e.printStackTrace();
                    return;
                }
                users.addAll(objects);
                adapter.notifyDataSetChanged();
                Log.d("AddMembersActivity", "number of users: " + users.size());
            }
        });
        // get all friends of the current user todo--finalize this
//        ParseUser current = ParseUser.getCurrentUser();
//        ParseQuery<ParseObject> postQuery = current.getRelation("friends").getQuery();
//        postQuery.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                users.addAll((Collection<? extends ParseUser>) objects);
//            }
//        });
    }
}
