package com.example.ibuprofen.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.ChooseActivity;
import com.example.ibuprofen.DetailsActivity;
import com.example.ibuprofen.R;
import com.example.ibuprofen.ResultsActivity;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

// adapter to show user's past events
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    private Context context;
    private List<Event> events;
    private ParseUser user;
    public boolean pastEvent;

    public EventAdapter(Context context, List<Event> events, boolean pastEvent) {
        this.context = context;
        this.events = events;
        this.user = ParseUser.getCurrentUser();
        this.pastEvent = pastEvent;
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Event event = events.get(position);
        try {
            viewHolder.bind(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // instance vars
        ImageView ivRestaurant;
        TextView tvCreator;
        TextView tvRestaurant;
        TextView tvFriendNumber;
        CardView cvCard;


        public ViewHolder(@NonNull View view) {
            super(view);

            // initialize vars using findById
            ivRestaurant = view.findViewById(R.id.ivRestaurant);
            tvCreator = view.findViewById(R.id.tvCreator);
            tvRestaurant = view.findViewById(R.id.tvEventName);
            tvFriendNumber = view.findViewById(R.id.tvFriendNumber);
            cvCard = view.findViewById(R.id.cvCard);

            view.setOnClickListener(this);
        }

        public void bind(Event event) throws ParseException {
            if (!pastEvent) {
                cvCard.setCardBackgroundColor(Color.parseColor("#FFD8D9"));
                cvCard.setRadius(40);
                cvCard.setCardElevation(4);
                cvCard.setMaxCardElevation(4);
                if (events.size() > 1) {
                    cvCard.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.MATCH_PARENT));
//                    cvCard.setContentPadding(30, 30, 30, 0);
                }
            }

            // set username
            tvCreator.setText(event.getCreator().fetchIfNeeded().getUsername());
            // set image (either restaurant of choice or profile picture of organizer)
            final ParseFile creatorImage = (ParseFile) event.getCreator().fetchIfNeeded().get("profilePic");
            creatorImage.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        if (!pastEvent) {
                            Glide.with(context)
                                    .load(creatorImage.getUrl())
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(ivRestaurant);
                        }
                        else {
                            Glide.with(context).load(creatorImage.getUrl()).into(ivRestaurant);
                        }
//                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                        // ImageView
//                        ivRestaurant.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "Problem load image the data.");
                    }
                }
            });

            // find number of event attendees (list all of them in details page)
            ParseRelation<ParseUser> members = event.getMembers();
            members.getQuery().countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    int num = count - 1;
                    tvFriendNumber.setText("" + num);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            int position = getAdapterPosition();
            System.out.println(position);
            Event event = events.get(position);
            if (!pastEvent)
                intent = new Intent(context, ChooseActivity.class);
            else {
                intent = new Intent(context, ResultsActivity.class);
                intent.putExtra("votedOn", event.getOptions());
            }
            intent.putExtra("event", event);
            context.startActivity(intent);
        }
    }
}