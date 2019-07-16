package com.example.ibuprofen.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {
    // access keys
    public static final String KEY_NAME = "name";
    public static final String KEY_CUISINE = "cuisine";
    public static final String KEY_AGE = "ageLimit";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_HEALTH = "healthRating";
    public static final String KEY_RATING = "rating";
    public static final String KEY_PRICE = "price";

    // getter methods for columns
    public String getName() {
        return getString(KEY_NAME);
    }

    public String getCuisine() {
        return getString(KEY_CUISINE);
    }

    public boolean getAgeLimit() {
        // if true it means attendees have to be over 21
        return getBoolean(KEY_AGE);
    }

    public ParseGeoPoint getLocation() {
        return super.getParseGeoPoint(KEY_LOCATION);
    }

    public Number getHealthRating() {
        return getNumber(KEY_HEALTH);
    }

    public Number getRating() {
        return getNumber(KEY_RATING);
    }

    public Number getPrice() {
        return getNumber(KEY_PRICE);
    }
}
