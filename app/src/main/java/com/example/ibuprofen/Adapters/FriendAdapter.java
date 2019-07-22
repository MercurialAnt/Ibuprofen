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
        ImageView profile_iv;
        TextView username_tv;
        TextView name_tv;
        Button add_btn;

        public ViewHolder(@NonNull View view) {
            super(view);

            // initialize vars using findById
            profile_iv = view.findViewById(R.id.ivProfile);
            username_tv = view.findViewById(R.id.tvUsername);
            name_tv = view.findViewById(R.id.tvName);
            add_btn = view.findViewById(R.id.btnAdd);

            //todo set onclick listener for add button (but only in fragment)

        }

        public void bind(ParseUser user) {
            // set information
            // profile image
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
                    .into(profile_iv);
            // username
            username_tv.setText(user.getString("username"));
            // name
            name_tv.setText(user.getString("name"));
        }
    }
}
