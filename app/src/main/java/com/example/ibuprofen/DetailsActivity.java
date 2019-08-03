package com.example.ibuprofen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ibuprofen.model.Restaurant.hoursToArray;

public class DetailsActivity extends AppCompatActivity {
    Restaurant restaurant;
    List<Review> reviews;
    ReviewAdapter reviewAdapter;
    List<String> urls;
    SlidingImageAdapter slidingImageAdapter;

    private TextView tvName;
    private ViewPager vpImages;
    private RatingBar rbRating;
    private TextView tvCuisine;
    private TextView tvPrice;
    private TextView tvDistance;
    private ImageView ivMaps;
    private RecyclerView rvReviews;
    private LinearLayout llDots;
    private TextView tvSundayHours;
    private TextView tvMondayHours;
    private TextView tvTuesdayHours;
    private TextView tvWednesdayHours;
    private TextView tvThursdayHours;
    private TextView tvFridayHours;
    private TextView tvSaturdaydayHours;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // hide action bar
        getSupportActionBar().hide();

        tvName = findViewById(R.id.tvName);
        vpImages = findViewById(R.id.vpImages);
        rbRating = findViewById(R.id.rbRating);
        tvCuisine = findViewById(R.id.tvCuisine);
        tvPrice = findViewById(R.id.tvPrice);
        tvDistance = findViewById(R.id.tvDistance);
        restaurant = Parcels.unwrap(getIntent().getParcelableExtra("Detailed"));
        rvReviews = findViewById(R.id.rvReviews);
        llDots = findViewById(R.id.llDots);
        tvSundayHours = findViewById(R.id.tvSundayHours);
        tvMondayHours = findViewById(R.id.tvMondayHours);
        tvTuesdayHours = findViewById(R.id.tvTuesdayHours);
        tvWednesdayHours = findViewById(R.id.tvWednesdayHours);
        tvThursdayHours = findViewById(R.id.tvThursdayHours);
        tvFridayHours = findViewById(R.id.tvFridayHours);
        tvSaturdaydayHours = findViewById(R.id.tvSaturdayHours);
        ivMaps = findViewById(R.id.ivMaps);

        reviews = new ArrayList<>();
        urls = new ArrayList<>();

        YelpAPI api = new YelpAPI(DetailsActivity.this);
        Request reqReviews = api.getReview(restaurant.getID());
        Request reqDetailed = api.getDetails(restaurant.getID());
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

                        final double lat = obj.getJSONObject("coordinates").getDouble("latitude");
                        final double lon = obj.getJSONObject("coordinates").getDouble("longitude");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callMaps(ivMaps, lat, lon, restaurant.getName());
                            }
                        });

                        if (obj.has("hours")) {
                            JSONArray time = obj.getJSONArray("hours");
                            JSONArray hours = obj.getJSONArray("hours").getJSONObject(0).getJSONArray("open");
                            final JSONArray week = hoursToArray(hours);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        setHours(tvMondayHours, week, 0);
                                        setHours(tvTuesdayHours, week, 1);
                                        setHours(tvWednesdayHours, week, 2);
                                        setHours(tvThursdayHours, week, 3);
                                        setHours(tvFridayHours, week, 4);
                                        setHours(tvSaturdaydayHours, week, 5);
                                        setHours(tvSundayHours, week, 6);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }

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
                    } catch (ParseException e) {
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
        rbRating.setRating((float) restaurant.getRating());
        tvPrice.setText(restaurant.getPrice());
    }

    public static void setHours(TextView tvDay, JSONArray week, int index) throws JSONException {
        if (week.length() > index) {
            JSONObject day = week.getJSONObject(index);
            if (day.getInt("day") == index) {
                tvDay.setText(day.getString("time"));
            }
        }

    }
    public void callMaps(ImageView ivMaps, final double lat, double lon, final String name) {
        ivMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + "lon" + "?q=" + Uri.encode(name));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
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
