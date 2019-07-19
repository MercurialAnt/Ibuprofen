package com.example.ibuprofen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ibuprofen.model.Restaurant;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    Restaurant restaurant;
    private TextView tvName;
    private ImageView ivImage;
    private TextView tvHours;
    private TextView tvDescription;
    private RatingBar rbRating;
    private TextView tvCuisine;
    private RatingBar rbPrice;
    private RatingBar rbHealth;
    private TextView tvDistance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvName = findViewById(R.id.tvName);
        ivImage = findViewById(R.id.ivImage);
        tvHours = findViewById(R.id.tvHours);
        tvDescription = findViewById(R.id.tvDescription);
        rbRating = findViewById(R.id.rbRating);
        tvCuisine = findViewById(R.id.tvCuisine);
        rbPrice = findViewById(R.id.rbPrice);
        rbHealth = findViewById(R.id.rbHealth);
        tvDistance = findViewById(R.id.tvDistance);
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("Detailed"));

//        if (restaurant.getImage() != null) {
//            Glide.with(this).load(restaurant.getImage()).into(ivImage);
//        }
        tvName.setText(restaurant.getName());
        tvCuisine.setText(restaurant.getCategories());
        tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
//        tvHours.setText(restaurant.getHours());
        rbRating.setRating(restaurant.getRating());
        rbPrice.setRating(restaurant.getPrice());
//        rbHealth.setRating(restaurant.getHealth());
    }
}
