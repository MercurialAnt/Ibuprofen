package com.example.ibuprofen.model;

import android.util.Log;

import com.example.ibuprofen.Adapters.SlidingImageAdapter;
import com.example.ibuprofen.DetailsActivity;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.YelpAPI;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    public static final String KEY_ID = "code";
    public static final String KEY_VOTERS = "voted";
    public static final String KEY_REVIEWS = "reviews";
    public static final String KEY_PRICE = "price";
    public static final String KEY_NAME = "name";
    public static final String KEY_RATING = "rating";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_CATEGORIES = "categories";
    public static final String KEY_IMAGE = "image_url";
    public static final String KEY_TIME = "time";

    public Restaurant() {

    }

    // Getters and Setters
    public String getID() {
        return getString(KEY_ID);
    }

    public void setID(String id) {
        put(KEY_ID, id);

    }

    public ParseRelation<ParseUser> getVoted() {
        return getRelation(KEY_VOTERS);
    }

    public JSONArray getReviews() {
        return getJSONArray(KEY_REVIEWS);
    }

    public void setReviews(JSONArray array) {
        put(KEY_REVIEWS, array);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getPrice() {
        return getString(KEY_PRICE);
    }

    public void setPrice(String price) {
        put(KEY_PRICE, price);
    }


    public double getRating() {
        return getDouble(KEY_RATING);
    }

    public void setRating(double rating) {
        put(KEY_RATING, rating);
    }

    public double getDistance() {
        return getDouble(KEY_DISTANCE);
    }

    public void setDistance(double distance) {
        put(KEY_DISTANCE, distance);
    }

    public String getCategories() {
        return getString(KEY_CATEGORIES);
    }

    public void setCategories(String categories) {
        put(KEY_CATEGORIES, categories);
    }

    public String getImage() {
        return getString(KEY_IMAGE);
    }

    public void setImage(String url) {
        put(KEY_IMAGE, url);
    }

    public JSONArray getTime() {
        return getJSONArray(KEY_TIME);
    }

    public void setTime(JSONArray time) {
         put(KEY_TIME, time);
    }

    public static Restaurant fromJSON(JSONObject store) throws JSONException {
        Restaurant restaurant = new Restaurant();
        restaurant.setRating(store.getDouble(KEY_RATING));
        restaurant.setPrice(store.optString(KEY_PRICE, ""));
        restaurant.setID(store.getString("id"));
        restaurant.setName(store.getString(KEY_NAME));
        restaurant.setDistance(store.getDouble(KEY_DISTANCE) / 1609.0);
        restaurant.setCategories(categoryToString(store.getJSONArray(KEY_CATEGORIES)));
        restaurant.setImage(store.getString(KEY_IMAGE));

        return restaurant;
    }

    public static String categoryToString(JSONArray array) throws JSONException {
        String categories = "";
        for (int i = 0; i < array.length(); i++) {
            JSONObject titles = array.getJSONObject(i);
            if (i == array.length() - 1)
                categories += (titles.get("title") + "");
            else
                categories += (titles.get("title") + ", ");
        }
        return categories;
    }

    public static JSONArray hoursToString(JSONArray array) throws JSONException, ParseException {
        JSONArray week = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            JSONObject day = array.getJSONObject(i);
            JSONObject newDay = new JSONObject();
            String time = String.format("%s - %s",
                    militaryToNormal(day.getInt("start")),
                    militaryToNormal(day.getInt("end")));
            newDay.put("time", time);
            week.put(newDay);
        }

        return week;
    }

    public static JSONObject getDetailedInfo(String id, YelpAPI api, OkSingleton client) {
        Request reqDetailed = api.getDetails(id);
        final JSONObject details = new JSONObject();
        client.newCall(reqDetailed).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("DetailsActivity", "couldn't get details");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static String militaryToNormal(int time) throws ParseException {
        Date date = new SimpleDateFormat("hhmm").parse(String.format("%04d", time));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(date);
    }

}