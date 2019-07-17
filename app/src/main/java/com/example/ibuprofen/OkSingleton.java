package com.example.ibuprofen;

import okhttp3.OkHttpClient;

/*
 * By following the Singleton pattern for the HTTP client it will reduce latency. Since
 * OkHttp uses thread pools and creating multiple instaces uses more memory and slows the
 * requests.
 */
public class OkSingleton extends OkHttpClient {

    private static class LazyHolder {
        private static final OkSingleton instance = new OkSingleton();
    }

    // Using the initialization on demand holder pattern to lazy load the cliet.
    public static OkSingleton getInstance() {
        return LazyHolder.instance;
    }

}
