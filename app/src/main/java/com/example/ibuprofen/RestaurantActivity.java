package com.example.ibuprofen;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibuprofen.model.Event;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class RestaurantActivity extends AppCompatActivity {

    // instance vars
    EditText distance; // in miles
    Button btnSubmit;

    // new event
    Event event;

    // information to send
    int dist = 5 * 1609; // initially set to 5 miles

    // list of restaurants that fit the criteria
    String options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // initialize vars
        distance = findViewById(R.id.etDistance);
        btnSubmit = findViewById(R.id.btnSubmit);
        options = "";


        // set on click listener for submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets distance information
                String enteredDistance = distance.getText().toString();
                if (!enteredDistance.equals("")) {
                    dist = Integer.parseInt(enteredDistance) * 1609; // convert to miles
                }

                // create a new event
                event = new Event();

                // set creator for event
                event.setCreator(ParseUser.getCurrentUser());

                // add creator to attendee list
                event.getMembers().add(ParseUser.getCurrentUser());

                // query acceptable restaurants
                queryOptions();
            }
        });
    }
    // gets options within radius and updates them in database
    public void queryOptions() {
        YelpAPI test = new YelpAPI(this);
        Location gpsLocation = test.getLocationByProvider(LocationManager.GPS_PROVIDER);
        OkHttpClient client = OkSingleton.getInstance();
        client.newCall(test.getDistanceFilteredRestaurants(dist, gpsLocation)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Feed", "Did not work");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray array = obj.getJSONArray("businesses");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject store = array.getJSONObject(i);
                            store.accumulate("count", new Integer(0));
                        }
                        options = array.toString();
                        event.setOptions(options);
                        saveEvent();
                        Log.d("RESTACTIVITY", options);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d("RESTACTIVITY", "fail");
                }
            }
        });
    }

    public void saveEvent() {
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("RESTACTIVITY", "save failure");
                    e.printStackTrace();
                    return;
                }
                // intent to go to next activity once restaurants are queried
                Intent i = new Intent(RestaurantActivity.this, AddMembersActivity.class);
                i.putExtra("event", event);
                startActivity(i);
                Log.d("RESTACTIVITY", "success!");
                Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
