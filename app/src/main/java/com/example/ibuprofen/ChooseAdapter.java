package com.example.ibuprofen;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {
    private Context context;
    private List<Restaurant> choices;

    public ChooseAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.choices = choices;
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
        private RecyclerView rvChoose;
        int count = 0;

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
            rvChoose = itemView.findViewById(R.id.rvChoose);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count += 1;
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant choice = choices.get(position);
                        //choice.count;
                    }
                    nextChoice(rvChoose, count);
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count += 1;
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Restaurant choice = choices.get(position);
                        //choice.count;
                    }
                    nextChoice(rvChoose, count);
                }
            });
        }

        public void bind(Restaurant restaurant) throws JSONException {
            if (restaurant.getImage() != null) {
                Glide.with(context).load(restaurant.getImage()).into(ivImage);
            }
            tvName.setText(restaurant.getName());
            tvCuisine.setText(restaurant.getCategories());
//            tvDistance.setText(restaurant.getDistance());
            rbRating.setRating(restaurant.getRating());
            rbPrice.setRating(restaurant.getPrice());
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

    public void nextChoice(RecyclerView rvChoose, int count) {
        rvChoose.scrollToPosition(count + 1);
    }
}
