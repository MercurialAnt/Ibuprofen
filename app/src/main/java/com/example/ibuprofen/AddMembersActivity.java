package com.example.ibuprofen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ibuprofen.Adapters.FriendAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddMembersActivity extends AppCompatActivity {
    // instance vars
    RecyclerView rvUsers;
    FriendAdapter adapter;
    List<ParseUser> users;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        // initialize vars
        rvUsers = findViewById(R.id.rvUsers);
        btnNext = findViewById(R.id.btnNext);
        users = new ArrayList<>();

        //create adapter
        adapter = new FriendAdapter(this, users);
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
                // intent to move on to chooseActivity
                Intent i = new Intent(AddMembersActivity.this, ChooseActivity.class);
                startActivity(i);
            }
        });
    }

    private void queryUsers() {
        // get all users in the database
        ParseQuery query = ParseUser.getQuery();
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
    }
}
