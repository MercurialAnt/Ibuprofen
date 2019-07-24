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


    //deserialize the JSON
    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();
        JSONArray tvCuisine;

        //extract values from JSON
        //name
        if (jsonObject.has("name")) {
            restaurant.tvName = jsonObject.getString("name");
        } else
            restaurant.tvName = "";

        //id
        if (jsonObject.has("id")) {
            restaurant.id = jsonObject.getString("id");
        } else
            restaurant.id = "";

        //rating
        if (jsonObject.has("rating")) {
            restaurant.rbRating = jsonObject.getInt("rating");
        } else
            restaurant.rbRating = 0;

        //price
        if (jsonObject.has("price")) {
            restaurant.rbPrice = jsonObject.getString("price");
        } else
            restaurant.rbPrice = "";

        //image
        if (jsonObject.has("image_url")) {
            restaurant.ivImage = jsonObject.getString("image_url");
        } else
            restaurant.ivImage = "";

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

        if (jsonObject.has("count")) {
            restaurant.count = jsonObject.getInt("count");
        }

        if (jsonObject.has("distance")) {
            double meters = jsonObject.getDouble("distance");
            // convert the meters to miles
            double conversion = 1609.344;
            restaurant.distance = meters / conversion;
        }


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

//    public Number getHealthRating() {
//
//    }

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