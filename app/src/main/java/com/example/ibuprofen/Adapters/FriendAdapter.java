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
import com.parse.ParseUser;

import java.util.List;

// adapter to show user's past events
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    private Context context;
    private List<ParseUser> users;

    public FriendAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
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

            //set onclick listener for add button (but only in fragment)

        }

        public void bind(ParseUser user) {
            // fix this
            // makes add button disappear if the user being shown is the current user
            if (user.equals(ParseUser.getCurrentUser())) {
                btnAdd.setVisibility(View.GONE);
            }
            // set information
            // profile image (replace anonymous.png later)
            String profileUrl;
            if (user.getParseFile("profilePic") != null) {
                profileUrl = user.getParseFile("profilePic").getUrl();
            }
            else {
                profileUrl = "@drawable/anonymous.png";
            }
            Glide.with(context)
                    .load(profileUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
            // username
            tvUsername.setText(user.getString("username"));
            // name
            tvName.setText(user.getString("name"));
        }
    }
}
