package com.example.ibuprofen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static com.parse.Parse.getApplicationContext;

public class YelpAPI {
    private static final String API_KEY = "EE9jyEqL-mfsN9GgQ98zTUsfwCCo8dEMd3oO6Rc5an_uMq3ilJRNYiaarDzJWd5rBvdF25BLcEeyOPLpBxlzzyrIhd2f8-d3q-nk0lTHu16b7KVqzC61LWhYcTQuXXYx";
    private static final String CLIENT_ID = "mUOZCkNGCqMlYAobs2PbjQ";
    private final String base_url = "https://api.yelp.com/v3";
    private final String auth_key_header = "Authorization";
    final String auth_value_header = "Bearer " + API_KEY;
    // /businesses/search search
    // /businesses/{id} details
    // /businesses/{id}/reviews reviews

    Context context;

    public YelpAPI(Context context) {
        this.context = context;
    }

    public Request getRestaurants(Location gpsLocation, String offset) {
        HttpUrl.Builder builder = HttpUrl
                .parse(base_url + "/businesses/search")
                .newBuilder()
                .addQueryParameter("sort_by", "distance")
                .addQueryParameter("limit", "10")
                .addQueryParameter("offset", offset);
        builder = gpsLocation != null
                ? builder.addQueryParameter("latitude",gpsLocation.getLatitude() + "").addQueryParameter("longitude", gpsLocation.getLongitude() + "")
                : builder.addQueryParameter("location", "Seattle");
        return getAuthRequest(builder.build());
    }

    public Request getReview(String restId) {
        HttpUrl url = HttpUrl
                .parse(base_url + "/businesses/" + restId + "/reviews");
        return getAuthRequest(url);
    }

    public Request getDetails(String restId) {
        HttpUrl url = HttpUrl
                .parse(base_url + "/businesses/" + restId);
        return getAuthRequest(url);
    }

    public Request getFilteredRestaurants(int radius, int limit, Location gpsLocation, List<String> choosen, List<Integer> price) {
        HttpUrl.Builder builder =  HttpUrl
                .parse(base_url + "/businesses/search")
                .newBuilder()
                .addQueryParameter("latitude",gpsLocation.getLatitude() + "")
                .addQueryParameter("longitude", gpsLocation.getLongitude() + "")
                .addQueryParameter("limit", limit + "")
                .addQueryParameter("radius", radius + "");

        addCategories(choosen, builder);
        addMoneySign(price, builder);

        return getAuthRequest(builder.build());
    }

    public Request getAuthRequest(HttpUrl url) {
        return new Request.Builder()
                .get()
                .url(url)
                .addHeader(auth_key_header, auth_value_header)
                .build();
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

    public Location getLocationByProvider(String provider) {
        Location location = null;

        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return location;
                }
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return location;
    }
}