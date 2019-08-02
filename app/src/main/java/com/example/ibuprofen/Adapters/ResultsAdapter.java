package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Restaurant;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    List<Restaurant> restaurants;
    Context context;

    public ResultsAdapter(Context context, List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, viewGroup, false);
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

        private TextView tvCount;
        private TextView tvName;
        private LinearLayout llProfiles;
        private RecyclerView rvPeople;
        private List<ParseUser> people;
        private PeopleAdapter peopleAdapter;

        public ViewHolder(@NonNull View itemView) {
           super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvName = itemView.findViewById(R.id.tvName);
            llProfiles = itemView.findViewById(R.id.llProfiles);

            people = new ArrayList<>();
            rvPeople = itemView.findViewById(R.id.rvPeople);
            peopleAdapter = new PeopleAdapter(context, people);
            rvPeople.setAdapter(peopleAdapter);
            rvPeople.setLayoutManager(new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false));
        }

        public void bind(Restaurant restaurant) {
            peopleAdapter.clear();
            restaurant.getVoted().getQuery().findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        addPeople(objects);
                        tvCount.setText(String.format("%d", objects.size()));
                        prepare_people(objects);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            tvName.setText(restaurant.getName());

        }

        public void addPeople(List<ParseUser> list) {
            for (int i = 0; i < list.size(); i++) {
                people.add(list.get(i));
            }
            peopleAdapter.notifyDataSetChanged();
        }

        private void prepare_people(final List<ParseUser> users) {
            int size = users.size();
            if (size == 0) {
                return;
            }
            int cutoff = Math.min(size, 3);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 80);
            layoutParams.setMargins(2, 2, 2, 0);
            ImageView[] imageViews = new ImageView[cutoff];

            for (int i = 0; i < cutoff; i++) {
                if (size > cutoff && i == 2) {
                    ImageView extra = new ImageView(context);
                    extra.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.plusoptions));
                    extra.setOnClickListener(new View.OnClickListener() {
                        boolean clicked = true;
                        @Override
                        public void onClick(View v) {
                            if (clicked) {
                                rvPeople.setVisibility(View.VISIBLE);
                                clicked = false;
                            } else {
                                rvPeople.setVisibility(View.GONE);
                                clicked = true;
                            }
                        }
                    });
                    llProfiles.addView(extra, 2, layoutParams);
                } else {
                    imageViews[i] = new ImageView(context);
                    loadPic(imageViews[i], users.get(i));
                    llProfiles.addView(imageViews[i], i, layoutParams);
                }

            }

        }

        public void loadPic(ImageView iv, ParseUser user) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.ic_launcher_background)
                    .transform(new CircleCrop());

            ParseFile file = user.getParseFile("profilePic");
            String url = "";
            if (file != null) {
                url = file.getUrl();
            }
            Glide.with(context)
                    .load(url)
                    .apply(options)
                    .into(iv);
        }
    }
}
