package com.example.ibuprofen.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ibuprofen.MainActivity;
import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantFlow.RestaurantManager;

public class EventNameFragment extends Fragment {

    private Button btnNext;
    private EditText etEventName;
    ImageView ivRestaurant;
    ImageView ivMovie;


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

        final boolean[] restaurant = new boolean[1];
        final boolean[] movie = new boolean[1];

        ivRestaurant.setOnClickListener(new View.OnClickListener() {
            // onClick bring it to restaurants filter fragment
            @Override
            public void onClick(View v) {
                if (restaurant[0]) {
                    restaurant[0] = false;
                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                else {
                    restaurant[0] = true;
                    v.setBackgroundColor(Color.parseColor("#3B299895"));
                }
            }
        });

        ivMovie.setOnClickListener(new View.OnClickListener() {
            // onClick bring it to restaurants filter fragment
            @Override
            public void onClick(View v) {
                if (movie[0]) {
                    movie[0] = false;
                    v.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                else {
                    movie[0] = true;
                    v.setBackgroundColor(Color.parseColor("#3B299895"));
                }
            }
        });

        final String[] name = {""};
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = etEventName.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("eventName", name[0]);
                bundle.putString("fragment", "normal");

                if (restaurant[0]) {
                    Intent i = new Intent(getContext(), RestaurantManager.class);
                    i.putExtra("bundle", bundle);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getContext(), "select event type", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
