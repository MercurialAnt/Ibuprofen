package com.example.ibuprofen.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    int rating;
    String id;
    String user;
    String text;
    String url;
    String createdAt;

    public static Review fromJson(JSONObject jsonObject) throws JSONException {
        Review review = new Review();

        review.id = jsonObject.optString("id", "");
        JSONObject user = jsonObject.optJSONObject("user");
        review.user = user == null ? "" : user.toString();
        review.text = jsonObject.optString("text", "");
        review.url = jsonObject.optString("url", "");
        review.createdAt = jsonObject.optString("time_created", "");
        review.rating = jsonObject.optInt("rating", 0);


        return review;
    }

    public int getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
