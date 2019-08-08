package com.example.ibuprofen.API;

import android.content.Context;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class MovieAPI extends BaseAPI {
    Context context;

    private final String movie_base_url = "http://api-public.guidebox.com/v2/";
    private static final String GUIDE_BOX_API_KEY = "c816879da73e2d82733d7d52abc1a1a198d9234c";

    public MovieAPI(Context context) {
        this.context = context;
    }

    public Request getGenres() {
        HttpUrl url = HttpUrl.parse(movie_base_url + "genres");
        return getAuthRequest(url, GUIDE_BOX_API_KEY);
    }

//    public Request getFilteredMovies() {
//
//    }
}
