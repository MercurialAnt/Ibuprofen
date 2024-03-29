package com.example.ibuprofen.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

// keeps track of events that have occurred and users that participated
@ParseClassName("Event")
public class Event extends ParseObject {

    // instance vars
    public static final String KEY_RESTAURANT = "restaurantId";
    public static final String KEY_USERS = "attendees";
    public static final String KEY_OPTIONS = "options";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_VOTED = "hasVoted";
    public static final String KEY_NAME = "name";
    public static final String KEY_TYPE = "eventType";

    public Event() {
    }

    public String getRestaurantId() {
        return getString(KEY_RESTAURANT);
    }

    public void setRestaurantId(String id) {
        put(KEY_RESTAURANT, id);
    }

    public ParseRelation<ParseUser> getMembers() {
        return getRelation(KEY_USERS);
    }

    public void setMembers(ParseRelation<ParseUser> members) {
        put(KEY_USERS,members);
    }

    public void removeMember(ParseUser user) {
        getMembers().remove(user);
    }

    public void setOptions(String opt) {
        put(KEY_OPTIONS,opt);
    }

    public String getOptions() {
        return getString(KEY_OPTIONS);
    }

    public ParseUser getCreator() {
        return getParseUser(KEY_CREATOR);
    }

    public void setCreator(ParseUser user) {
        put(KEY_CREATOR, user);
    }

    public ParseRelation<ParseObject> getVoters() {
        return getRelation(KEY_VOTED);
    }

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getEventType() {
        return getString(KEY_TYPE);
    }

    public void setEventType(String type) {
        put(KEY_TYPE, type);
    }
}
