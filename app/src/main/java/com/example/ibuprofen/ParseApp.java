package com.example.ibuprofen;

import android.app.Application;

import com.parse.Parse;


public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();



        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("ANA")
                .clientKey("erichascrustyelbows")
                .server("http://ibuprofen.herokuapp.com/parse")
                .build();
        Parse.initialize(config);
    }
}
