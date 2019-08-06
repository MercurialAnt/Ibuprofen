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

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        addBadgeView();

        //get feed fragment
//        ProfileFragment fragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.action_profile);
//        mPending = fragment.getPendingList();
//        System.out.println(mPending);

        // hide support action bar
        getSupportActionBar().hide();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
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
        if (getIntent() != null && getIntent().hasExtra("results")) {
            bottomNavigationView.setSelectedItemId(R.id.action_profile);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.action_feed);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public void addBadgeView() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(3);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification_badge, menuView, false);
        itemView.addView(notificationBadge);

        //check if list of pending events is empty
    }
}