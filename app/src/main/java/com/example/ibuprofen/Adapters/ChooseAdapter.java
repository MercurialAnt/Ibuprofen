package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
import com.example.ibuprofen.YelpAPI;
import com.example.ibuprofen.model.Restaurant;
import com.example.ibuprofen.model.Review;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private FragmentActivity activity;
    private List<Restaurant> choices;
    private RecyclerView rvChoices;
    public int[] counters;
    YelpAPI api;
    OkSingleton client;

    public ChooseAdapter(Context context, List<Restaurant> choices, RecyclerView rvChoices, FragmentActivity activity) {
        this.context = context;
        this.choices = choices;
        this.rvChoices = rvChoices;
        this.activity = activity;
        api = new YelpAPI(context);
        client = OkSingleton.getInstance();
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
        private ReviewAdapter reviewAdapter;
        private RecyclerView rvReviews;
        private List<Review> mReviews;
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
            rvReviews = itemView.findViewById(R.id.rvReviews);

            mReviews = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(context, mReviews);
            rvReviews.setAdapter(reviewAdapter);
            rvReviews.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


//            btnYes = itemView.findViewById(R.id.btnYes);
//            btnNo = itemView.findViewById(R.id.btnNo);
//
//            btnYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        counters[position]++;
//                        nextChoice(position + 1);
//                    }
//                }
//            });
//
//            btnNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        counters[position] = 0;
//                        nextChoice(position + 1);
//                    }
//                }
//            });
        }

        public void bind(Restaurant restaurant) {
            if (restaurant.getImage() != null) {
                Glide.with(context).load(restaurant.getImage()).into(ivImage);
            }
            tvName.setText(restaurant.getName());
            tvCuisine.setText(restaurant.getCategories());
            tvDistance.setText(String.format("%.2f miles", restaurant.getDistance()));
            rbRating.setRating(restaurant.getRating());
            rbPrice.setRating(restaurant.getPrice());

//            populateReviews(restaurant.id);

        }

        private void populateReviews(String id) {
            Request reviewRequest = api.getReview(id);

            client.newCall(reviewRequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("ChooseAdapter", "getting the reviews failed");
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject obj = new JSONObject(response.body().string());
                            JSONArray array = obj.getJSONArray("reviews");
                            for (int i = 0; i < array.length(); i++) {
                                mReviews.add(Review.fromJson(array.getJSONObject(i)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                }
            });
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
