package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import com.example.ibuprofen.RestaurantFlow.ResultsFragment;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.example.ibuprofen.model.Review;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.ibuprofen.RestaurantFlow.FilterFragment.fragmentIntent;
import static com.example.ibuprofen.RestaurantFlow.FilterFragment.getIntXml;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private FragmentManager manager;
    private List<Restaurant> choices;
    private RecyclerView rvChoices;
    private Event event;

    public ChooseAdapter(Context context, List<Restaurant> choices, RecyclerView rvChoices, FragmentManager manager, Event event) {
        this.context = context;
        this.choices = choices;
        this.rvChoices = rvChoices;
        this.manager = manager;
        this.event = event;
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
    public void onItemDismiss(final int position, int direction) {
        if (direction == ItemTouchHelper.END) {
            Restaurant restaurant = choices.get(position);
            ParseRelation<ParseUser> relation = restaurant.getRelation("voted");
            relation.add(ParseUser.getCurrentUser());
            restaurant.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        if (position < getIntXml(context, R.integer.result_limit)) {
                            nextChoice(position + 1);
                        } else {
                            goToResults(event);
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        private TextView tvName;
        private ImageView ivImage;
        private RatingBar rbRating;
        private TextView tvCuisine;
        public TextView tvPrice;
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
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDistance = itemView.findViewById(R.id.tvDistance);

            reviews = new ArrayList<>();
            rvReviews = itemView.findViewById(R.id.rvReviews);
            reviewAdapter = new ReviewAdapter(context, reviews);
            rvReviews.setAdapter(reviewAdapter);
            rvReviews.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        }

        public void bind(Restaurant restaurant) {
            reviewAdapter.clear();
            addReviews(restaurant.getReviews());

            if (restaurant.getImage() != null) {
                Glide.with(context).load(restaurant.getImage()).into(ivImage);
            }
            tvName.setText(restaurant.getName());
            tvCuisine.setText(restaurant.getCategories());
            tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
            rbRating.setRating(restaurant.getRating());
            tvPrice.setText(restaurant.getPrice());
        }

        public void addReviews(JSONArray list) {
            try {
                for (int i = 0; i < list.length(); i++) {
                    reviews.add(Review.fromJson(list.getJSONObject(i)));
                }
                reviewAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
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

    public void goToResults(Event currentEvent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("event", currentEvent);
        fragmentIntent(new ResultsFragment(), bundle, manager, false);
    }


}
