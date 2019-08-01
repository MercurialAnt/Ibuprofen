package com.example.ibuprofen.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    public static final String KEY_ID = "id";
    public static final String KEY_VOTERS = "voted";
    public static final String KEY_REVIEWS = "reviews";
    public static final String KEY_PRICE = "price";
    public static final String KEY_NAME = "name";
    public static final String KEY_RATING = "rating";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_CATEGORIES = "categories";

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


    public long getRating() {
        return getLong(KEY_RATING);
    }

    public void setRating(long rating) {
        put(KEY_RATING, rating);
    }

    public long getDistance() {
        return getLong(KEY_DISTANCE);
    }

    public void setDistance(long distance) {
        put(KEY_DISTANCE, distance);
    }

    public String getCategories() {
        return getString(KEY_CATEGORIES);
    }

    public void setCategories(String categories) {
        put(KEY_CATEGORIES, categories);
    }

    public static Restaurant fromJSON(JSONObject store) {
        Restaurant restaurant = new Restaurant();
        try {
            restaurant.setRating(store.getLong("rating"));
            restaurant.setPrice(store.getString("price"));
            restaurant.setID(store.getString("id"));
            restaurant.setName(store.getString("name"));
            restaurant.setDistance();
            //do images
            //do reviews
            //do categories
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}