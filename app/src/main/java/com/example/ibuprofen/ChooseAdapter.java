package com.example.ibuprofen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONException;

import java.util.List;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> choices;
    private RecyclerView rvChoices;

    public ChooseAdapter(Context context, List<Restaurant> choices, RecyclerView rvChoices) {
        this.context = context;
        this.choices = choices;
        this.rvChoices = rvChoices;
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
        try {
            viewHolder.bind(choice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivImage;
        private RatingBar rbRating;
        private TextView tvCuisine;
        public RatingBar rbPrice;
        private TextView tvDistance;
        private Button btnYes;
        private Button btnNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            rbPrice = itemView.findViewById(R.id.rbPrice);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            btnYes = itemView.findViewById(R.id.btnYes);
            btnNo = itemView.findViewById(R.id.btnNo);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant choice = choices.get(position);
                        choice.incrementCount();
                        nextChoice(position + 1);
                    }
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant choice = choices.get(position);
                        choice.incrementCount();
                        nextChoice(position + 1);
                    }
                }
            });
        }

        public void bind(Restaurant restaurant) throws JSONException {
            if (restaurant.getImage() != null) {
                Glide.with(context).load(restaurant.getImage()).into(ivImage);
            }
            tvName.setText(restaurant.getName());
            tvCuisine.setText(restaurant.getCategories());
            tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
            rbRating.setRating(restaurant.getRating());
            rbPrice.setRating(restaurant.getPrice());
        }
        public void nextChoice(int count) {
            rvChoices.scrollToPosition(count);
        }
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
