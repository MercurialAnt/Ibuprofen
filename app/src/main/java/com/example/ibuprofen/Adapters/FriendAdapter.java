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
import com.parse.ParseException;
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
        viewHolder.bind(user);
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
            tvUsername = view.findViewById(R.id.tvUsername);
            tvName = view.findViewById(R.id.tvName);
            btnAdd = view.findViewById(R.id.btnAdd);


            // sets on click listener for add button if in the AddMembers page
            if (addMembers) {
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

                        Log.d("FRIENDADD",users.get(getAdapterPosition()).getUsername());
                    }
                });
            }
            else {
                // makes add button go away if this is not the addMembersActivity
                btnAdd.setVisibility(View.GONE);
            }
        }

        public void bind(ParseUser user) {
            // makes add button disappear if the user being shown is the current user
            if (user.hasSameId(ParseUser.getCurrentUser())) {
                btnAdd.setVisibility(View.GONE);
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
