package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

// chooseAdapter to show user's past events
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    private Context context;
    private List<ParseUser> users;
    Event event; // to access attendees list and edit it
    boolean addNewMembersFragment; // true if this is the AddMembersActivity, false if its the friendFragment
    public ParseRelation<ParseUser> members;
    public boolean saved; // keeps track of whether or not something is currently saving
    ArrayList<String> addedToEvent; // arrayList of usernames of users that have been added to current event
    ParseUser current;
    String PurpleColor = "#794d7e";

    public FriendAdapter(Context context, List<ParseUser> users, Event event, boolean addNewMembersFragment) {
        this.context = context;
        this.users = users;
        this.event = event;
        this.addNewMembersFragment = addNewMembersFragment;
        if (addNewMembersFragment) {
            this.members = event.getMembers();
            this.saved = true;
            this.addedToEvent = new ArrayList<>();
        }
        this.current = ParseUser.getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ParseUser user = users.get(position);
        try {
            viewHolder.bind(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // instance vars
        ImageView ivProfile;
        TextView tvUsername;
        TextView tvName;
        Button btnAdd;


        public ViewHolder(@NonNull View view) {
            super(view);

            // initialize vars using findById
            ivProfile = view.findViewById(R.id.ivProfile);
            tvUsername = view.findViewById(R.id.tvUser);
            tvName = view.findViewById(R.id.tvN);
            btnAdd = view.findViewById(R.id.btnAddd);

            // sets on click listener for add button if in the AddMembers page
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnAdd.setVisibility(View.VISIBLE);
                    ParseUser clicked = users.get(getAdapterPosition());

                    saved = false;

                    // does additional actions depending on view
                    if (addNewMembersFragment) { // if this is the addNewMembers fragment
                        if (addedToEvent.contains(clicked.getUsername())) {
                            btnAdd.setText("ADD");
                            btnAdd.setBackgroundResource(R.drawable.friends_toggle_on);
                            removeAttendee(clicked);
                        }
                        else {
                            addedToEvent.add(clicked.getUsername());
                            members.add(clicked);
                            event.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    saved = true;
                                }
                            });
                            btnAdd.setText("REMOVE");
                            btnAdd.setBackgroundResource(R.drawable.friends_toggle_off);
                        }
                    }
                    else { // if this is the friends fragment
                        // replaces button
                        try {
                            if (isCurrentFriend(clicked)) {
                                btnAdd.setText("ADD");
                                btnAdd.setBackgroundResource(R.drawable.friends_toggle_on);
                                removeFriend(clicked);
                            }
                            else {
                                btnAdd.setText("REMOVE");
                                btnAdd.setBackgroundResource(R.drawable.friends_toggle_off);

                                // adds friend to current user's friend list
                                addFriends(clicked);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        public void bind(ParseUser user) throws ParseException {
            btnAdd.setVisibility(View.VISIBLE);
            btnAdd.setText("ADD");
//            btnAdd.setBackground(Drawable.createFromPath("friends_toggle_on"));
            btnAdd.setBackgroundResource(R.drawable.friends_toggle_on);

            // replaces Add button for current friends
            if (!addNewMembersFragment) {
                if (isCurrentFriend(user)) {
                    // replaces buttons
                    btnAdd.setVisibility(View.VISIBLE);
                    btnAdd.setText("REMOVE");
                    btnAdd.setBackgroundResource(R.drawable.friends_toggle_off);
                }
            }
            else { // replaces Add button
                if(addedToEvent.contains(user.getUsername())) { // checks if user has already been added to event
                    btnAdd.setVisibility(View.VISIBLE);
                    btnAdd.setText("REMOVE");
                    btnAdd.setBackgroundResource(R.drawable.friends_toggle_off);
                }
            }

            // set information
            // profile image
            String profileUrl;
            if (user.getParseFile("profilePic") != null) {
                profileUrl = user.getParseFile("profilePic").getUrl();
                Glide.with(context)
                        .load(profileUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfile);
            }
            // username
            tvUsername.setText("@" + user.getString("username"));
            // name
            tvName.setText(user.getString("name"));
        }

        public boolean isCurrentFriend(ParseUser friend) throws ParseException {
            // get current users friends
            ParseUser current = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> postQuery = current.getRelation("friends").getQuery();
            postQuery.whereEqualTo("username", friend.getUsername());
            int results = postQuery.find().size();

            // returns true if friends
            if (results != 0) {
                return true;
            }
            return false;
        }

        public void addFriends(ParseUser friend) {
            ParseUser current = ParseUser.getCurrentUser();
            current.getRelation("friends").add(friend);

            current.saveInBackground();
        }

        private void removeFriend(ParseUser user) {
            ParseUser current = ParseUser.getCurrentUser();
            current.getRelation("friends").remove(user);
            // saves users in background
            current.saveInBackground();
        }

        private void removeAttendee(ParseUser user) {
            addedToEvent.remove(user.getUsername());
            members.remove(user);
            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    saved = true;
                }
            });
        }
    }
}
