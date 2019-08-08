package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    List<ParseUser> people;
    Context context;
    boolean popup;

    public PeopleAdapter(Context context, List<ParseUser> people, Boolean popup) {
        this.context = context;
        this.people = people;
        this.popup = popup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_person, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ParseUser user = people.get(position);
        viewHolder.bind(user);
    }

    public void clear() {
        people.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return people.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(ParseUser user) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.ic_launcher_background)
                    .transform(new CircleCrop());

            ParseFile file = user.getParseFile("profilePic");
            String url = "";
            if (file != null) {
                url = file.getUrl();
            }
            if (popup) {
                ivProfile.requestLayout();
                ivProfile.getLayoutParams().height = 150;
                ivProfile.getLayoutParams().width = 150;
            }
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(ivProfile);
        }
    }
}
