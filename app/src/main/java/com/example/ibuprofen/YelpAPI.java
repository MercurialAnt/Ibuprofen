package com.example.ibuprofen;

import android.content.Context;
import android.location.Location;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class YelpAPI {
    private static final String API_KEY = "2j_lGyWbgvroFWV_ok2MKy9na46ybGYQMhsdocpcQSBkWqDLKGwZbDXwN08cRDQnwdV6KQt84sOegMs0MTdOpTwPFHmi7B17nvlGK3t0U8dIzowV5j3yR3ug9KguXXYx";
    private static final String CLIENT_ID = "Syqog6jHymMjfL2jLkaMgw";
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


    // To use it do client.call(request).enque(new Callback)
    public Request getRestaurants(Location gpsLocation) {

        // -TODO add location from phone and then talk about how to deal with the options
        HttpUrl url;
        if (gpsLocation != null) {
            url = HttpUrl
                    .parse(base_url + "/businesses/search")
                    .newBuilder()
                    .addQueryParameter("latitude",gpsLocation.getLatitude() + "")
                    .addQueryParameter("longitude", gpsLocation.getLongitude() + "")
                    .addQueryParameter("limit", "30")
                    .build();
        }
        else {
            url = HttpUrl
                    .parse(base_url + "/businesses/search")
                    .newBuilder()
                    .addQueryParameter("location","Seattle")
                    .addQueryParameter("limit", "30")
                    .build();
        }
        return new Request.Builder()
                .get()
                .url(url)
                .addHeader(auth_key_header, auth_value_header)
                .build();
    }
}