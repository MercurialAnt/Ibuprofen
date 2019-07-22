package com.example.ibuprofen.model;

public class Category {
    private String name;
    private String api_name;
    private String iconImg;

    public Category(String name, String api_name) {
        this.name = name;
        this.api_name = api_name;
    }

    public String getName() {
        return name;
    }

    public String getApi_name() {
        return api_name;
    }
}
