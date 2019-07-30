package com.example.ibuprofen.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Restaurant {
    //list out attributes
    public String tvName;
    public String id;
    public int count;
    public int rbRating;
    public String rbPrice;
    public String ivImage;
    public String categories;
    public double distance;
    public String reviews;
    public String people;


    //deserialize the JSON
    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();
        JSONArray tvCuisine;

        //extract values from JSON
        //name
        restaurant.tvName = jsonObject.optString("name", "");

        //id
        restaurant.id = jsonObject.optString("id", "");


        //rating
        restaurant.rbRating = jsonObject.optInt("rating", 0);


        //price
        restaurant.rbPrice = jsonObject.optString("price", "");


        //image
        restaurant.ivImage = jsonObject.optString("image_url", "");

        //reviews
        restaurant.reviews = jsonObject.optString("reviews", "");

        //people
        JSONArray voters = jsonObject.optJSONArray("people");
        if (voters != null) {
            restaurant.people = voters.toString();
        }


        //categories
        restaurant.categories = "";
        if (jsonObject.has("categories")) {
            tvCuisine = jsonObject.getJSONArray("categories");
            for (int i = 0; i < tvCuisine.length(); i++) {
                JSONObject as = tvCuisine.getJSONObject(i);
                if (i == tvCuisine.length() - 1)
                    restaurant.categories += (as.get("title") + "");
                else
                    restaurant.categories += (as.get("title") + ", ");
            }
        }

        restaurant.count = jsonObject.optInt("count", 0);


        double conversion = 1609.344;
        restaurant.distance = jsonObject.optDouble("distance", 0) / conversion;


        return restaurant;
    }


    // getter methods for columns
    public String getName() {
        return tvName;
    }

    public String getImage() {
        return ivImage;
    }

    public String getCategories() {
        return categories;
    }

    public String getReviews() {
        return reviews;
    }

    public String getPeople() {
        return people;
    }

    public int getRating() {
        return rbRating;
    }

    public int getPrice() {
        return rbPrice.length();
    }

    public int getCount() {
        return this.count;
    }

    public double getDistance() {
        return  this.distance;
    }

}