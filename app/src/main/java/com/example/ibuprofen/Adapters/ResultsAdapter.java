package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
        // pop up vars
        private TextView tvPopName;
        private RecyclerView rvPopPeople;

        public ViewHolder(@NonNull View itemView) {
           super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvName = itemView.findViewById(R.id.tvName);
            llProfiles = itemView.findViewById(R.id.llProfiles);

            people = new ArrayList<>();
            rvPeople = itemView.findViewById(R.id.rvPeople);
            peopleAdapter = new PeopleAdapter(context, people, true);
            rvPeople.setAdapter(peopleAdapter);
            rvPeople.setLayoutManager(new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false));
        }

        public void bind(final Restaurant restaurant) {
            peopleAdapter.clear();
            restaurant.getVoted().getQuery().findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        addPeople(objects);
                        tvCount.setText(String.format("%d", objects.size()));
                        prepare_people(objects, restaurant);
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

        private void prepare_people(final List<ParseUser> users, final Restaurant restaurant) {
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
                                //rvPeople.setVisibility(View.VISIBLE);
                                showPopupWindowClick(v, restaurant);
                                clicked = false;
                            } else {
//                                rvPeople.setVisibility(View.GONE);
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

        public void showPopupWindowClick(View view, Restaurant restaurant) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_results, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            tvPopName = popupWindow.getContentView().findViewById(R.id.tvPopName);
            tvPopName.setText(restaurant.getName());
            rvPopPeople = popupWindow.getContentView().findViewById(R.id.rvPopPeople);
            rvPopPeople.setAdapter(peopleAdapter);
            rvPopPeople.setLayoutManager(new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false));


            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
    }

    public void clear() {
        restaurants.clear();
        notifyDataSetChanged();
    }
}
