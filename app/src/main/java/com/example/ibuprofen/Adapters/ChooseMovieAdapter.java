//package com.example.ibuprofen.Adapters;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Movie;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.helper.ItemTouchHelper;
//import android.text.method.ScrollingMovementMethod;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.example.ibuprofen.R;
//import com.example.ibuprofen.RestaurantFlow.ResultsFragment;
//import com.example.ibuprofen.model.Event;
//import com.example.ibuprofen.model.Movie;
//import com.parse.ParseException;
//import com.parse.ParseRelation;
//import com.parse.ParseUser;
//import com.parse.SaveCallback;
//
//import org.json.JSONException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.example.ibuprofen.DetailsActivity.setHours;
//import static com.example.ibuprofen.RestaurantFlow.FilterFragment.fragmentIntent;
//import static com.example.ibuprofen.RestaurantFlow.FilterFragment.getIntXml;
//
//public class ChooseMovieAdapter extends RecyclerView.Adapter<ChooseMovieAdapter.ViewHolder> implements ItemTouchHelperAdapter {
//    private Context context;
//    private FragmentManager manager;
//    private List<Movie> choices;
//    private RecyclerView rvChoices;
//    private Event event;
//
//    public ChooseMovieAdapter(Context context, List<Movie> choices, RecyclerView rvChoices, FragmentManager manager, Event event) {
//            this.context = context;
//            this.choices = choices;
//            this.rvChoices = rvChoices;
//            this.manager = manager;
//            this.event = event;
//    }
//
//    @NonNull
//    @Override
//    public ChooseMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        View view;
//        view = LayoutInflater.from(context).inflate(R.layout.item_movie_choose, parent, false);
//        return new ChooseMovieAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ChooseMovieAdapter.ViewHolder viewHolder, int position) {
//        viewHolder.itemView.setBackgroundColor(Color.WHITE);
//        Movie choice = choices.get(position);
//        try {
//            viewHolder.bind(choice);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return choices.size();
//    }
//
//    @Override
//    public void onItemMove(int fromPosition, int toPosition) {
//        return;
//    }
//
//    @Override
//    public void onItemDismiss(final int position, int direction) {
//        if (direction == ItemTouchHelper.END) {
//            Movie movie = choices.get(position);
//            ParseRelation<ParseUser> relation = movie.getRelation("voted");
//            relation.add(ParseUser.getCurrentUser());
//            movie.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e == null) {
//                        nextChoice(position);
//                    } else {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
//            nextChoice(position);
//        }
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
//        private TextView tvName;
//        private ImageView ivImage;
//        private RatingBar rbRating;
//        private TextView tvGenre;
//        public TextView tvPrice;
//        private TextView tvLength;
//        private TextView tvServices;
//        private TextView tvSynopsis;
//
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            tvName = itemView.findViewById(R.id.tvName);
//            ivImage = itemView.findViewById(R.id.ivImage);
//            rbRating = itemView.findViewById(R.id.rbRating);
//            tvGenre = itemView.findViewById(R.id.tvGenre);
//            tvPrice = itemView.findViewById(R.id.tvPrice);
//            tvLength = itemView.findViewById(R.id.tvLength);
//            tvServices = itemView.findViewById(R.id.tvServices);
//            tvSynopsis = itemView.findViewById(R.id.tvSynopsis);
//        }
//
//        public void bind(Movie movie) throws JSONException {
//            if (movie.getImage() != null) {
//                Glide.with(context).load(movie.getImage()).into(ivImage);
//            }
//            tvName.setText(movie.getName());
//            tvGenre.setText(movie.getGenres());
//            rbRating.setRating((float) movie.getRating());
//            tvPrice.setText(movie.getPrice());
//            tvLength.setText(movie.getLength());
//            tvServices.setText(movie.getServices());
//            tvSynopsis.setText(movie.getSynopsis());
//            tvSynopsis.setMovementMethod(new ScrollingMovementMethod());
//        }
//
//        @Override
//        public void onItemSelected() {
//        }
//
//        @Override
//        public void onItemClear() {
//        }
//    }
//
//    public void nextChoice(int position) {
//        if (position + 1 != getIntXml(context, R.integer.result_limit)) {
//            rvChoices.scrollToPosition(position + 1);
//        } else {
//            goToResults(event);
//        }
//    }
//
//    public void clear() {
//        choices.clear();
//        notifyDataSetChanged();
//    }
//
//    // Add a list of items -- change to type used
//    public void addAll(List<Movie> list) {
//        choices.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    public void goToResults(Event currentEvent) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("event", currentEvent);
//        fragmentIntent(new ResultsFragment(), bundle, manager, false);
//    }
//}
