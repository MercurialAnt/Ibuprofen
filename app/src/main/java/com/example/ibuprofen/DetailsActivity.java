package com.example.ibuprofen;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ibuprofen.Adapters.ReviewAdapter;
import com.example.ibuprofen.Adapters.SlidingImageAdapter;
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
    List<String> urls;
    SlidingImageAdapter slidingImageAdapter;

    private TextView tvName;
    private ViewPager vpImages;
    private TextView tvHours;
    private RatingBar rbRating;
    private TextView tvCuisine;
    private TextView tvPrice;
    private TextView tvDistance;
    private RecyclerView rvReviews;
    private LinearLayout llDots;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // hide action bar
        getSupportActionBar().hide();

        tvName = findViewById(R.id.tvName);
        vpImages = findViewById(R.id.vpImages);
        tvHours = findViewById(R.id.tvHours);
        rbRating = findViewById(R.id.rbRating);
        tvCuisine = findViewById(R.id.tvCuisine);
        tvPrice = findViewById(R.id.tvPrice);
        tvDistance = findViewById(R.id.tvDistance);
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("Detailed"));
        rvReviews = findViewById(R.id.rvReviews);
        llDots = findViewById(R.id.llDots);

        reviews = new ArrayList<>();
        urls = new ArrayList<>();

        YelpAPI api = new YelpAPI(DetailsActivity.this);
        Request reqReviews = api.getReview(restaurant.id);
        Request reqDetailed = api.getDetails(restaurant.id);
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


        client.newCall(reqDetailed).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("DetailsActivity", "couldn't get details");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray array = obj.getJSONArray("photos");
                        for (int i = 0; i < array.length(); i++) {
                            urls.add(array.getString(i));
                        }
                        slidingImageAdapter = new SlidingImageAdapter(DetailsActivity.this, urls);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vpImages.setAdapter(slidingImageAdapter);
                                prepareDots(0, urls.size());
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        vpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                prepareDots(i, urls.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        tvName.setText(restaurant.getName());
        tvCuisine.setText(restaurant.getCategories());
        tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
//        tvHours.setText(restaurant.getHours());
        rbRating.setRating(restaurant.getRating());
        String price = "";
        for(int i = 0; i < restaurant.getPrice(); i++) {
            price += "$";
        }
        tvPrice.setText(price);
    }

    private void prepareDots(int currentSlide, int size) {
        if (llDots.getChildCount() > 0) {
            llDots.removeAllViews();
        }

        ImageView [] dots = new ImageView[size];

        for (int i = 0; i < size; i++) {
            dots[i] = new ImageView(this);
            if (i == currentSlide) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 0, 4, 0);
            llDots.addView(dots[i], layoutParams);
        }
    }
}
