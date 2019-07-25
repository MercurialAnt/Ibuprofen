package com.example.ibuprofen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ibuprofen.Adapters.FriendAdapter;
import com.example.ibuprofen.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AddMembersActivity extends AppCompatActivity {
    // instance vars
    RecyclerView rvUsers;
    FriendAdapter adapter;
    List<ParseUser> users;
    Button btnNext;
    Event event; // to pass onto choose activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        // initialize vars
        rvUsers = findViewById(R.id.rvUsers);
        btnNext = findViewById(R.id.btnNext);
        users = new ArrayList<>();
        event = getIntent().getParcelableExtra("event");

        //create adapter
        adapter = new FriendAdapter(this, users, event, true);
        //set the adapter on the recycler view
        rvUsers.setAdapter(adapter);
        //set layout manager on recycler view
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        // fill list with possible attendees
        queryUsers();

        // set onClick listener for next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.saved) {
                    Intent i = new Intent(AddMembersActivity.this, ChooseActivity.class);
                    i.putExtra("event", event);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Loading, please try again in a second.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void queryUsers() {
        // get all users in the database
        ParseQuery query = ParseUser.getQuery();
        // expensive operation so done in a background thread not in same thread as the UI
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
    }
}
