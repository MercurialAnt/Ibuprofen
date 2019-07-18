package com.example.ibuprofen.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Restaurant {
    //list out attributes
    public String tvName;
    public String id;
    public int rbRating;
    public String rbPrice;
    public String ivImage;

    //deserialize the JSON
    public static Restaurant fromJSON(JSONObject jsonObject) throws JSONException {
        Restaurant restaurant = new Restaurant();

        //extract values from JSON
        //ToDo- check to see if
        restaurant.tvName = jsonObject.getString("name");
        restaurant.id = jsonObject.getString("id");
        restaurant.rbRating = jsonObject.getInt("rating");
        restaurant.rbPrice = jsonObject.getString("price");
        restaurant.ivImage = jsonObject.getString("image_url");
        return restaurant;
    }

    // getter methods for columns
    public String getName() {
        return tvName;
    }

    public String getImage() {
        return ivImage;
    }

//    public String getCategories() {
//
//    }
//
//    public ParseGeoPoint getLocation() {
//
//    }
//
//    public Number getHealthRating() {
//
//    }
//
    public int getRating() {
        return rbRating;
    }

    public int getPrice() {
        if (rbPrice != null)
            return rbPrice.length();
        else
            return 0;
    }
}
