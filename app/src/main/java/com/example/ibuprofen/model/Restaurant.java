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
    public static final String KEY_IMAGE = "image_url";

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

    public String getImage() {
        return getString(KEY_IMAGE);
    }

    public void setImage(String url) {
        put(KEY_IMAGE, url);
    }

    public static Restaurant fromJSON(JSONObject store) throws JSONException {
        Restaurant restaurant = new Restaurant();
        restaurant.setRating(store.getLong(KEY_RATING));
        restaurant.setPrice(store.getString(KEY_PRICE));
        restaurant.setID(store.getString(KEY_ID));
        restaurant.setName(store.getString(KEY_NAME));
        restaurant.setDistance(store.getLong(KEY_DISTANCE) / 1609);
        restaurant.setCategories(categoryToString(store.getJSONArray(KEY_CATEGORIES)));
        restaurant.setImage(store.getString(KEY_IMAGE));
            //do reviews
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

}