package com.example.ibuprofen;

import android.app.Application;

import com.example.ibuprofen.model.Event;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        // register Event class
        ParseObject.registerSubclass(Event.class);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("ANA")
                .clientKey("erichascrustyelbows")
                .server("http://ibuprofen.herokuapp.com/parse")
                .build();
        Parse.initialize(config);
    }
}
