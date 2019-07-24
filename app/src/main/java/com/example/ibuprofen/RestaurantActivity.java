package com.example.ibuprofen;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibuprofen.Adapters.CategoriesAdapter;
import com.example.ibuprofen.model.Category;
import com.example.ibuprofen.model.Event;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class RestaurantActivity extends AppCompatActivity {

    EditText etDistance;
    Button btnSubmit;
    // The money sign buttons
    Button btnOne;
    Button btnTwo;
    Button btnThree;
    Button btnFour;
    RecyclerView rvCuisine;

    Event event;

    final double meterToMile = 1609.344;

    // filter options that come with set defaults
    int dist = (int) (5 * meterToMile);
    List<Integer> price;
    List<Category> categories;
    List<String> choosen;
    CategoriesAdapter categoriesAdapter;

    // list of restaurants that fit the criteria
    String options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        etDistance = findViewById(R.id.etDistance);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
        rvCuisine = findViewById(R.id.rvCuisine);

        options = "";
        price = new ArrayList<>();
        categories = new ArrayList<>();
        choosen = new ArrayList<>();
        fillCategoryList();
        categoriesAdapter = new CategoriesAdapter(this, categories, choosen);
        rvCuisine.setAdapter(categoriesAdapter);
        rvCuisine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        btnOne.setOnClickListener(new MoneyListen(1));
        btnTwo.setOnClickListener(new MoneyListen(2));
        btnThree.setOnClickListener(new MoneyListen(3));
        btnFour.setOnClickListener(new MoneyListen(4));

        // set on click listener for submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets distance information
                String enteredDistance = etDistance.getText().toString();
                if (!enteredDistance.equals("")) {
                    dist = (int) (Double.parseDouble(enteredDistance) * meterToMile); // convert to miles
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
        client.newCall(test.getDistanceFilteredRestaurants(dist, gpsLocation, choosen, price)).enqueue(new Callback() {
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

    public void fillCategoryList() {
        categories.add(new Category("Pizza", "pizza"));
        categories.add(new Category("Chinese", "chinese"));
        categories.add(new Category("Burgers", "burgers"));
        categories.add(new Category("Seafood", "seafood"));
        categories.add(new Category("Thai", "thai"));
        categories.add(new Category("Italian", "italian"));
        categories.add(new Category("Steakhouses", "steak"));
        categories.add(new Category("Korean", "korean"));
        categories.add(new Category("Japanese", "japanese"));
        categories.add(new Category("Sandwiches", "sandwiches"));
        categories.add(new Category("Breakfast", "breakfast_brunch"));
        categories.add(new Category("Vietnamese", "vietnamese"));
        categories.add(new Category("Vegetarian", "vegetarian"));
        categories.add(new Category("Sushi Bars", "sushi"));
        categories.add(new Category("American", "tradamerican"));
    }

    class MoneyListen implements View.OnClickListener {

        int level;
        public MoneyListen(int priceLevel) {
            this.level = priceLevel;
        }
        @Override
        public void onClick(View v) {
            price.add(level);
            v.setBackgroundColor(Color.parseColor("#F08080"));
        }
    }
}
