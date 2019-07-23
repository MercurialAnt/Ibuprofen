package com.example.ibuprofen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.Adapters.ReviewAdapter;
import com.example.ibuprofen.model.Restaurant;
import com.example.ibuprofen.model.Review;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    Restaurant restaurant;
    List<Review> reviews;
    ReviewAdapter reviewAdapter;

    private TextView tvName;
    private ImageView ivImage;
    private TextView tvHours;
    private RatingBar rbRating;
    private TextView tvCuisine;
    private RatingBar rbPrice;
    private RatingBar rbHealth;
    private TextView tvDistance;
    private RecyclerView rvReviews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvName = findViewById(R.id.tvName);
        ivImage = findViewById(R.id.ivImage);
        tvHours = findViewById(R.id.tvHours);
        rbRating = findViewById(R.id.rbRating);
        tvCuisine = findViewById(R.id.tvCuisine);
        rbPrice = findViewById(R.id.rbPrice);
        rbHealth = findViewById(R.id.rbHealth);
        tvDistance = findViewById(R.id.tvDistance);
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("Detailed"));
        rvReviews = findViewById(R.id.rvReviews);

        reviews = new ArrayList<>();
        YelpAPI api = new YelpAPI(DetailsActivity.this);
        Request reqReviews = api.getReview(restaurant.id);
        OkSingleton client = OkSingleton.getInstance();
        client.newCall(reqReviews).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("DetailsActivity", "couldn't get reviews");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray array = obj.getJSONArray("reviews");
                        for (int i = 0; i < array.length(); i++) {
                            reviews.add(Review.fromJson(array.getJSONObject(i)));
                        }
                        reviewAdapter = new ReviewAdapter(DetailsActivity.this, reviews);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rvReviews.setAdapter(reviewAdapter);
                                rvReviews.setLayoutManager(new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        if (restaurant.getImage() != null) {
            Glide.with(this).load(restaurant.getImage()).into(ivImage);
        }
        tvName.setText(restaurant.getName());
        tvCuisine.setText(restaurant.getCategories());
        tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
//        tvHours.setText(restaurant.getHours());
        rbRating.setRating(restaurant.getRating());
        rbPrice.setRating(restaurant.getPrice());
//        rbHealth.setRating(restaurant.getHealth());


    }
}
