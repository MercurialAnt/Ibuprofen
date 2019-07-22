package com.example.ibuprofen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.ibuprofen.Toolbar.EventFragment;
import com.example.ibuprofen.Toolbar.FeedFragment;
import com.example.ibuprofen.Toolbar.FriendsFragment;
import com.example.ibuprofen.Toolbar.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_feed:
                        fragment = new FeedFragment();
                        break;
                    case R.id.action_event:
                        fragment = new EventFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_friends:
                    default:
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
}