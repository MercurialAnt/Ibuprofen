package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.R;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    List<Pair<String, String>> people;
    Context context;

    public PeopleAdapter(Context context, List<Pair<String, String>> people) {
        this.context = context;
        this.people = people;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_person, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Pair<String, String> pair = people.get(position);
        viewHolder.bind(pair);
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

        public void bind(Pair<String, String> pair) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.ic_launcher_background)
                    .transform(new CircleCrop());

            Glide.with(context)
                    .load(pair.second)
                    .apply(options)
                    .into(ivProfile);
        }
    }
}
