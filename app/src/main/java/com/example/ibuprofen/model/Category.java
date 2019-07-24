package com.example.ibuprofen.model;

public class Category {
    private String name;
    private String api_name;
    private String iconImg;

    public Category(String name, String api_name, String iconImg) {
        this.name = name;
        this.api_name = api_name;
        this.iconImg = iconImg;
    }

    public String getName() {
        return name;
    }

    public String getApi_name() {
        return api_name;
    }

    public String getIconImg() {
        return  iconImg;
    }
}

