package com.example.ibuprofen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ibuprofen.Toolbar.EventNameFragment;
import com.example.ibuprofen.Toolbar.FeedFragment;
import com.example.ibuprofen.Toolbar.FriendsFragment;
import com.example.ibuprofen.Toolbar.ProfileFragment;
import com.example.ibuprofen.model.Event;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // sets up notification for Pending
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);
        itemView.addView(notificationBadge);
        setPendingBoolean();

        // hide support action bar
        getSupportActionBar().hide();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // check if pending exists
                Fragment fragment;
                setPendingBoolean();
                switch (menuItem.getItemId()) {
                    case R.id.action_feed: {
                        getSupportActionBar().hide();
                        fragment = new FeedFragment();
                        break;
                    }
                    case R.id.action_event: {
                        getSupportActionBar().hide();
                        fragment = new EventNameFragment();
                        break;
                    }
                    case R.id.action_profile: {
                        getSupportActionBar().hide();
                        fragment = new ProfileFragment();
                        break;
                    }
                    case R.id.action_friends:
                    default:
                        getSupportActionBar().show();
                        fragment = new FriendsFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flSignupContainer, fragment).commit();
                return true;
            }
        });

        //set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public void addBadgeView(boolean add) {
        if (add) {
            notificationBadge.setVisibility(View.VISIBLE);
        }
        else {
            notificationBadge.setVisibility(View.INVISIBLE);
        }
    }

    // will use triggers later
    public void setPendingBoolean() {
        ParseQuery<Event> postQuery = new ParseQuery<>(Event.class);
        postQuery.include(Event.KEY_USERS);
        postQuery.whereEqualTo("attendees", ParseUser.getCurrentUser());
        postQuery.whereNotEqualTo("hasVoted", ParseUser.getCurrentUser());

        postQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (count != 0) {
                    ParseUser.getCurrentUser().put("hasPending", true);
                    addBadgeView(true);
                }
                else {
                    ParseUser.getCurrentUser().put("hasPending", false);
                    addBadgeView(false);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });
    }
}