package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

// adapter to show user's past events
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    private Context context;
    private List<ParseUser> users;
    Event event; // to access attendees list and edit it
    boolean addMembers; // true if this is the AddMembersActivity, false if its the friendFragment
    public ParseRelation<ParseUser> members;
    public boolean saved; // keeps track of whether or not something is currently saving

    public FriendAdapter(Context context, List<ParseUser> users, Event event, boolean addMembers) {
        this.context = context;
        this.users = users;
        this.event = event;
        this.addMembers = addMembers;
        if (addMembers) {
            this.members = event.getMembers();
            this.saved = true;
        }
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
        TextView tvAdded;

        public ViewHolder(@NonNull View view) {
            super(view);

            // initialize vars using findById
            ivProfile = view.findViewById(R.id.ivProfile);
            tvUsername = view.findViewById(R.id.tvUser);
            tvName = view.findViewById(R.id.tvN);
            btnAdd = view.findViewById(R.id.btnAddd);
            tvAdded = view.findViewById(R.id.tvAdded);

            tvAdded.setVisibility(View.GONE);

            // sets on click listener for add button if in the AddMembers page
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // replaces buttons
                    btnAdd.setVisibility(View.GONE);
                    tvAdded.setVisibility(View.VISIBLE);

                    // does additional actions depending on view
                    if (addMembers) { // if this is the addnewmembers fragment
                        // sets saved to false
                        saved = false;
                        // adds added user to attendees in event
                        // find number of event attendees (list all of them in details page)
                        members.add(users.get(getAdapterPosition()));
                        event.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                saved = true;
                            }
                        });
                        Log.d("FRIENDADD", users.get(getAdapterPosition()).getUsername());
                    }
                    else { // if this is the friends fragment
                        ParseUser friend = users.get(getAdapterPosition());

                        // adds friend to current user's friend list
                        addFriends(friend);

                        // adds current user to friend's list todo--decide if we want friend requests
//                        friend.getRelation("friends").add(ParseUser.getCurrentUser());
//                        friend.saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                Log.d("FriendAdapter","saved");
//                            }
//                        });
                    }
                }
            });
        }

        public void addFriends(ParseUser friend) {
            // adds friend to current user and vice versa
            ParseUser current = ParseUser.getCurrentUser();
            current.getRelation("friends").add(friend);

            // saves users in background
            current.saveInBackground();
        }

        public boolean isCurrentFriend(ParseUser friend) throws ParseException {
            // get current users friends
            ParseUser current = ParseUser.getCurrentUser();
            ParseQuery<ParseObject> postQuery = current.getRelation("friends").getQuery();
            postQuery.whereEqualTo("username", friend.getUsername());
            int results = postQuery.find().size();
            Log.d("results", results + friend.getUsername());

            // returns true if friends
            if (results != 0) {
                return true;
            }
            return false;
        }

        public void bind(ParseUser user) throws ParseException {
            // makes add button disappear if the user being shown is the current user
            if (user.hasSameId(ParseUser.getCurrentUser())) {
                btnAdd.setVisibility(View.GONE);
                tvAdded.setVisibility((View.GONE));
            }

            // replaces Add button for current friends
            if (!addMembers) {
                if (isCurrentFriend(user)) {
                    // replaces buttons
                    btnAdd.setVisibility(View.GONE);
                    tvAdded.setVisibility(View.VISIBLE);
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
            tvUsername.setText(user.getString("username"));
            // name
            tvName.setText(user.getString("name"));
        }
    }
}
