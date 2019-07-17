package com.example.ibuprofen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.Toolbar.ProfileFragment;
import com.example.ibuprofen.model.Restaurant;
import com.parse.ParseFile;

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
        private TextView tvName;
        private ImageView ivImage;
        private RatingBar rbRating;
        private TextView tvCuisine;
        public RatingBar rbPrice;
        private TextView tvDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            rbPrice = itemView.findViewById(R.id.rbPrice);
            tvDistance = itemView.findViewById(R.id.tvDistance);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant restaurant = restaurants.get(position);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("Detailed", (restaurant));
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Restaurant restaurant) {
//            ParseFile image = restaurant.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImage);
//            }
//            tvName.setText(restaurant.getName());
//            tvCuisine.setText(restaurant.getCategories());
//            tvDistance.setText(restaurant.getDistance());
//            rbRating.setRating(restaurant.getRating());
//            rbPrice.setRating(restaurant.getPrice());
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
