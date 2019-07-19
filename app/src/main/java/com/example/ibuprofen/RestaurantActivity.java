package com.example.ibuprofen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.ibuprofen.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    // instance vars
    EditText distance; // in miles
    Button submit_btn;
    // cuisines
    CheckBox chinese_cb;
    CheckBox french_cb;
    CheckBox american_cb;

    // information to send
    Double dist = 5.0; // initially set to 5
    boolean chinese;
    boolean french;
    boolean american;

    // list of restaurants that fit the criteria
    List<Restaurant> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        // initialize vars
        distance = findViewById(R.id.distance_et);
        chinese_cb = findViewById(R.id.chinese_cb);
        french_cb = findViewById(R.id.french_cb);
        american_cb = findViewById(R.id.american_cb);
        submit_btn = findViewById(R.id.submit_btn);
        options = new ArrayList<>();

        // set on click listener for submit
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo--parse user's selection information and base options in next activity on these selections
                // gets distance information
                String enteredDistance = distance.getText().toString();
                if (!enteredDistance.equals("")) {
                    dist = Double.parseDouble(enteredDistance);
                }
                // gets selected cuisine information
                chinese = chinese_cb.isSelected();
                french = french_cb.isSelected();
                american = american_cb.isSelected();

                // todo--query acceptable restaurant (once you have completed API/getter methods);
                queryOptions();

                // intent to go to next fragment once restaurants are queried
                Intent i = new Intent(v.getContext(), ChooseActivity.class);
                // todo--pass list of restaurants (once you have Parcelable restaurant model)
//                i.putParcelableArrayListExtra(options);
                startActivity(i);
            }
        });
    }
// todo--once api is updated with getter methods
    public void queryOptions() {
    }

}
