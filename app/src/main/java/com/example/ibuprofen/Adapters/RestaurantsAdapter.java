package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.DetailsActivity;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Restaurant;

import org.parceler.Parcels;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;

    public RestaurantsAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Restaurant restaurant = restaurants.get(position);
        viewHolder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView ivImage;
        public RatingBar rbRating;
        public TextView tvCuisine;
        public TextView tvPrice;
        public TextView tvDistance;
        public ConstraintLayout clRestaurant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            clRestaurant = itemView.findViewById(R.id.clRestaurant);

            clRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant restaurant = restaurants.get(position);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("Detailed", Parcels.wrap(restaurant));
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Restaurant restaurant) {
            if (restaurant.getImage() != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.image_placeholder);
                requestOptions.error(R.drawable.image_placeholder);
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load(restaurant.getImage())
                        .into(ivImage);
            }
            tvName.setText(restaurant.getName());
            tvCuisine.setText(restaurant.getCategories());
            tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
            rbRating.setRating(restaurant.getRating());
            tvPrice.setText(restaurant.getPrice());
        }
    }

    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Restaurant> list) {
        restaurants.addAll(list);
        notifyDataSetChanged();
    }
}