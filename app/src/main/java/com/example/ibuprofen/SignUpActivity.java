package com.example.ibuprofen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    final String TAG = "SignUp";
    // Instantiating the views
    private EditText username_et;
    private EditText password_et_1;
    private EditText password_et_2;
    private Button create_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username_et = findViewById(R.id.username_et);
        password_et_1 = findViewById(R.id.password_et_1);
        password_et_2 = findViewById(R.id.password_et_2);
        create_btn = findViewById(R.id.create_btn);

        final ParseUser newUser = new ParseUser();


        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();
                String password_1 = password_et_1.getText().toString();
                String password_2 = password_et_2.getText().toString();

                if (samePassword(password_1, password_2)) {
                    newUser.setUsername(username);
                    newUser.setPassword(password_1);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(TAG, "Signing up a user was successful");
                                final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.d(TAG, "Couldn't save the new user");
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });


    }


    // -TODO need to discuss how we want to accept username
    private boolean checkUniqueUser(String user) {
        ParseQuery<ParseUser> p = new ParseQuery<ParseUser>(ParseUser.class);
        try {
            if (!p.whereContains("username", user).find().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Username taken already", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean samePassword(String password1, String password2) {

        if (!password1.equals(password2)) {
            Toast.makeText(SignUpActivity.this, "The passwords don't match", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password1.length() <= 0) {
            Toast.makeText(SignUpActivity.this, "The password mustn't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
