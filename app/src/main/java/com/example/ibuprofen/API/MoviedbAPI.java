package com.example.ibuprofen.API;

import android.content.Context;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class MoviedbAPI {
    public static final String MOVIE_DATABASE_API_KEY = "a8d8058aff9f19d6a83469b7fad5fda2";
    private final String movie_database_base_url = "https://api.themoviedb.org/3/";
    private String API_KEY_PARAM = "api_key";

    Context context;

    public MoviedbAPI(Context context) {
        this.context = context;
    }

    public Request getMovieDetails(String id) {
        HttpUrl url = HttpUrl.parse(movie_database_base_url + "movie/" + id)
                .newBuilder()
                .addQueryParameter(API_KEY_PARAM, MOVIE_DATABASE_API_KEY)
                .build();

        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }
}


