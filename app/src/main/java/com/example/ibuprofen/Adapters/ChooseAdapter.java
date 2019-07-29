package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Restaurant;
import com.example.ibuprofen.model.Review;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<Restaurant> choices;
    private RecyclerView rvChoices;
    public int[] counters;

    public ChooseAdapter(Context context, List<Restaurant> choices, RecyclerView rvChoices) {
        this.context = context;
        this.choices = choices;
        this.rvChoices = rvChoices;
        counters = new int[10]; // keeps track of votes
    }

    @NonNull
    @Override
    public ChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_choose, parent, false);
        return new ChooseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAdapter.ViewHolder viewHolder, int position) {
        Restaurant choice = choices.get(position);
        viewHolder.bind(choice);
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        return;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
            counters[position] = direction == ItemTouchHelper.END ? 1 : 0;
        if (position <= counters.length)
            nextChoice(position + 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        private TextView tvName;
        private ImageView ivImage;
        private RatingBar rbRating;
        private TextView tvCuisine;
        public RatingBar rbPrice;
        private TextView tvDistance;
        private RecyclerView rvReviews;
        private List<Review> reviews;
        private ReviewAdapter reviewAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            rbPrice = itemView.findViewById(R.id.rbPrice);
            tvDistance = itemView.findViewById(R.id.tvDistance);

            reviews = new ArrayList<>();
            rvReviews = itemView.findViewById(R.id.rvReviews);
            reviewAdapter = new ReviewAdapter(context, reviews);
            rvReviews.setAdapter(reviewAdapter);
            rvReviews.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }

        public void bind(Restaurant restaurant) {
            reviewAdapter.clear();
            addReviews(restaurant.reviews);

            if (restaurant.getImage() != null) {
                Glide.with(context).load(restaurant.getImage()).into(ivImage);
            }
            tvName.setText(restaurant.getName());
            tvCuisine.setText(restaurant.getCategories());
            tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
            rbRating.setRating(restaurant.getRating());
            rbPrice.setRating(restaurant.getPrice());



        }

        public void addReviews(String list) {
            try {
                JSONArray array = new JSONArray(list);
                for (int i = 0; i < array.length(); i++) {
                    reviews.add(Review.fromJson(array.getJSONObject(i)));
                }
                reviewAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
    public void nextChoice(int count) {
        rvChoices.scrollToPosition(count);
    }

    public void clear() {
        choices.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Restaurant> list) {
        choices.addAll(list);
        notifyDataSetChanged();
    }


}
