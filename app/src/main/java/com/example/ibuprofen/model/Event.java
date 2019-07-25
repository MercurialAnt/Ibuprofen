package com.example.ibuprofen.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

// keeps track of events that have occurred and users that participated
@ParseClassName("Event")
public class Event extends ParseObject {

    // instance vars
    public static final String KEY_RESTAURANT = "restaurantId";
    public static final String KEY_USERS = "attendees";
    public static final String KEY_OPTIONS = "options";
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_VOTED = "hasVoted";
    public static final String KEY_EVENTNAME = "name";

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

    public String getEventName() {
        if (KEY_EVENTNAME == null)
            return "New Event";
        else
            return getString(KEY_EVENTNAME);
    }
}