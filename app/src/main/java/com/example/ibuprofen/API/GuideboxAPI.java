package com.example.ibuprofen.API;

import android.content.Context;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class GuideboxAPI extends BaseAPI {
    Context context;
    MoviedbAPI movieAPI;

    private final String movie_base_url = "http://api-public.guidebox.com/v2/";
    private static final String GUIDE_BOX_API_KEY = "c816879da73e2d82733d7d52abc1a1a198d9234c";

    public GuideboxAPI(Context context) {
        this.context = context;
        movieAPI = new MoviedbAPI(context);
    }

    public Request getGenres() {
        HttpUrl url = HttpUrl.parse(movie_base_url + "genres");
        return getAuthRequest(url, GUIDE_BOX_API_KEY);
    }

    public Request getFilteredMovies(int limit, String sources) {
        HttpUrl url = HttpUrl.parse(movie_base_url + "movies")
                .newBuilder()
                .addQueryParameter("limit", limit + "")
                .addQueryParameter("sources", sources)
                .build();
        return null;
    }
}
