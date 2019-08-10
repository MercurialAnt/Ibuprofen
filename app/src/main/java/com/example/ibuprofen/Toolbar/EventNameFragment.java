package com.example.ibuprofen.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ibuprofen.MovieFlow.MovieManager;
import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantFlow.RestaurantManager;

public class EventNameFragment extends Fragment {

    private Button btnNext;
    private EditText etEventName;
    ImageView ivRestaurant;
    ImageView ivMovie;
    ImageView ivAttractions;
    String purple = "#794d7e";
    String white = "#ffffff";
    public String value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_name_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnNext = view.findViewById(R.id.btnNext);
        etEventName = view.findViewById(R.id.etEventName);
        ivRestaurant = view.findViewById(R.id.ivRestaurant);
        ivMovie = view.findViewById(R.id.ivMovie);
        ivAttractions = view.findViewById(R.id.ivAttractions);

        final boolean[] restaurant = new boolean[1];
        final boolean[] movie = new boolean[1];
        final boolean[] attraction = new boolean[1];

        ivRestaurant.setOnClickListener(new View.OnClickListener() {
            // onClick bring it to restaurants filter fragment
            @Override
            public void onClick(View v) {
                if (restaurant[0]) {
                    restaurant[0] = false;
                    v.setBackgroundColor(Color.parseColor(white));
                }
                else {
                    // sets restaurant to true
                    restaurant[0] = true;
                    v.setBackground(getResources().getDrawable(R.drawable.event_icon));
                    v.setBackgroundColor(Color.parseColor(purple));
                    value = "restaurant";

                    // sets movie to false
                    movie[0] = false;
                    ivMovie.setBackgroundColor(Color.parseColor(white));

                    // sets attractions to false
                    attraction[0] = false;
                    ivAttractions.setBackgroundColor(Color.parseColor(white));
                }
            }
        });

        ivMovie.setOnClickListener(new View.OnClickListener() {
            // onClick bring it to restaurants filter fragment
            @Override
            public void onClick(View v) {
                if (movie[0]) {
                    movie[0] = false;
                    v.setBackgroundColor(Color.parseColor(white));
                }
                else {
                    // sets movie to true
                    movie[0] = true;
                    v.setBackground(getResources().getDrawable(R.drawable.event_icon));
                    value = "movie";

                    // sets restaurant to false
                    restaurant[0] = false;
                    ivRestaurant.setBackgroundColor(Color.parseColor(white));

                    // sets attractions to false
                    attraction[0] = false;
                    ivAttractions.setBackgroundColor(Color.parseColor(white));
                }
            }
        });

        ivAttractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attraction[0]) {
                    attraction[0] = false;
                    v.setBackgroundColor(Color.parseColor(white));
                }
                else {
                    // sets attraction to true
                    attraction[0] = true;
                    v.setBackgroundColor(Color.parseColor(purple));
                    value = "attraction";

                    // sets restaurant to false
                    restaurant[0] = false;
                    ivRestaurant.setBackgroundColor(Color.parseColor(white));

                    // sets movie to false
                    movie[0] = false;
                    ivMovie.setBackgroundColor(Color.parseColor(white));
                }
            }
        });

        final String[] name = {""};
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = etEventName.getText().toString();

                if (!name[0].equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventName", name[0]);
                    bundle.putString("fragment", "normal");
                    bundle.putString("eventType", value);

                    if (restaurant[0]) {
                        Intent i = new Intent(getContext(), RestaurantManager.class);
                        i.putExtra("bundle", bundle);
                        startActivity(i);
                    } else if (movie[0]) {
                        Intent i = new Intent(getContext(), MovieManager.class);
                        i.putExtra("bundle", bundle);
                        startActivity(i);
                    } else if (attraction[0]) {
                        Intent i = new Intent(getContext(), RestaurantManager.class);
                        i.putExtra("bundle", bundle);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(getContext(), "select event type", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "name event", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
