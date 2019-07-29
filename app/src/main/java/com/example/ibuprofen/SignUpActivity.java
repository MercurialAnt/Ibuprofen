package com.example.ibuprofen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.ibuprofen.SignupFlow.WelcomeFragment;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        final FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment nextFragment = new WelcomeFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flSignupContainer, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
