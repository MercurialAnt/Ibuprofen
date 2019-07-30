package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Review;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Review review = reviews.get(position);
        viewHolder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvText;
        private ImageView ivProfile;
        private RatingBar rbReviewRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvText = itemView.findViewById(R.id.tvText);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            rbReviewRating = itemView.findViewById(R.id.rbReviewRating);
        }

        public void bind(Review review) {
            tvText.setText(review.getText());
            rbReviewRating.setRating(review.getRating());
            try {
                JSONObject user = new JSONObject(review.getUser());
                tvUsername.setText(user.optString("name", "Anon"));
                String url = user.optString("image_url");
                if (url != null) {
                    Glide.with(context)
                            .load(url)
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivProfile);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }
}
