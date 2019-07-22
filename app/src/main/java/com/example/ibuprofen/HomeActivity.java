package com.example.ibuprofen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    final String TAG = "SignUp";
    final static int REQUEST_LOCATION = 10;
    // Instance variables of the views
    private EditText username_et;
    private EditText password_et;
    private Button login_btn;
    private Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_LOCATION);


        if (ParseUser.getCurrentUser() != null) {
            to_landing_page();
        }

        username_et = findViewById(R.id.etUsername);
        password_et = findViewById(R.id.etPassword);
        login_btn = findViewById(R.id.btnLogin);
        signup_btn = findViewById(R.id.btnSignup);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = username_et.getText().toString();
                final String password = password_et.getText().toString();

                login(username, password);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void to_landing_page() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("Login Activity", "Login successful");
                    final Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Login Activity", "Login error");
                    e.printStackTrace();
                }
            }
        });
    }



}
