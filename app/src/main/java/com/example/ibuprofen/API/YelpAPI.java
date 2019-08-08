package com.example.ibuprofen.API;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class YelpAPI extends BaseAPI {
    private static final String YELP_API_KEY = "EE9jyEqL-mfsN9GgQ98zTUsfwCCo8dEMd3oO6Rc5an_uMq3ilJRNYiaarDzJWd5rBvdF25BLcEeyOPLpBxlzzyrIhd2f8-d3q-nk0lTHu16b7KVqzC61LWhYcTQuXXYx";
    private static final String CLIENT_ID = "mUOZCkNGCqMlYAobs2PbjQ";
    private final String yelp_base_url = "https://api.yelp.com/v3";
    final String auth_value_header = "Bearer " + YELP_API_KEY;

    Context context;

    public YelpAPI(Context context) {
        this.context = context;
    }

    public Request getRestaurants(Location gpsLocation, String offset) {
        HttpUrl.Builder builder = HttpUrl
                .parse(yelp_base_url + "/businesses/search")
                .newBuilder()
                .addQueryParameter("sort_by", "distance")
                .addQueryParameter("limit", "10")
                .addQueryParameter("offset", offset);
        builder = gpsLocation != null
                ? builder.addQueryParameter("latitude",gpsLocation.getLatitude() + "").addQueryParameter("longitude", gpsLocation.getLongitude() + "")
                : builder.addQueryParameter("location", "Seattle");
        return getAuthRequest(builder.build(), auth_value_header);
    }

    public Request getReview(String restId) {
        HttpUrl url = HttpUrl
                .parse(yelp_base_url + "/businesses/" + restId + "/reviews");
        return getAuthRequest(url, auth_value_header);
    }

    public Request getDetails(String restId) {
        HttpUrl url = HttpUrl
                .parse(yelp_base_url + "/businesses/" + restId);
        return getAuthRequest(url, auth_value_header);
    }

    public Request getFilteredRestaurants(int radius, int limit, Location gpsLocation, List<String> choosen, List<Integer> price) {
        HttpUrl.Builder builder =  HttpUrl
                .parse(yelp_base_url + "/businesses/search")
                .newBuilder()
                .addQueryParameter("latitude",gpsLocation.getLatitude() + "")
                .addQueryParameter("longitude", gpsLocation.getLongitude() + "")
                .addQueryParameter("limit", limit + "")
                .addQueryParameter("radius", radius + "");

        addCategories(choosen, builder);
        addMoneySign(price, builder);

        return getAuthRequest(builder.build(), auth_value_header);
    }

    public void addCategories(List<String> choices, HttpUrl.Builder builder) {
        if (choices.isEmpty())
            return;

        String categories = "";
        for (String choice : choices) {
            categories += choice + ",";
        }
        builder.addQueryParameter("categories", categories.substring(0, categories.length() - 1));
    }

    public void addMoneySign(List<Integer> price, HttpUrl.Builder builder) {
        if (price.isEmpty())
            return;
        String money = "";
        for (int sign : price) {
            money += sign + ", ";
        }
        money = money.substring(0, money.length() - 2);
        Log.d("Price", money);
        builder.addQueryParameter("price", money);
    }
}