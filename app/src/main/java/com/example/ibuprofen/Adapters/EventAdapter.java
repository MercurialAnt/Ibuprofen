package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantFlow.RestaurantManager;
import com.example.ibuprofen.model.Event;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

// chooseAdapter to show user's past events
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
        ImageView ivAccept;
        ImageView ivDecline;
        TextView tvFriends;
        ImageView ivEventType;
        TextView tvDate;


        public ViewHolder(@NonNull View view) {
            super(view);

            // initialize vars using findById
            ivRestaurant = view.findViewById(R.id.ivRestaurant);
            tvCreator = view.findViewById(R.id.tvCreator);
            tvRestaurant = view.findViewById(R.id.tvEventName);
            tvFriendNumber = view.findViewById(R.id.tvFriendNumber);
            cvCard = view.findViewById(R.id.cvCard);
            ivAccept = view.findViewById(R.id.ivAccept);
            ivDecline = view.findViewById(R.id.ivDecline);
            tvFriends = view.findViewById(R.id.tvFriends);
            //Todo: set the image using type of event
            ivEventType = view.findViewById(R.id.ivEventType);
            tvDate = view.findViewById(R.id.tvDate);
            tvDate.setVisibility(View.GONE);


            if (pastEvent) {
                ivAccept.setVisibility(View.GONE);
                ivDecline.setVisibility(View.GONE);
                tvDate.setVisibility(View.VISIBLE);
            }

            // sets on click listener for add button if in the AddMembers page
            ivAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, RestaurantManager.class);
                    int position = getAdapterPosition();
                    Event event = events.get(position);
                    bundle.putString("fragment", "ChooseFragment");
                    bundle.putParcelable("event", event);
                    intent.putExtra("bundle", bundle);
                    context.startActivity(intent);
                }
            });

            ivDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        cvCard.setVisibility(View.GONE);
                        Event event = events.get(position);
                        events.remove(event);
                        notifyItemRemoved(position);
                        event.removeMember(user);
                        event.saveInBackground();

                        // checks if there are any events left, and changes notification symbol
                        if (events.size() == 0) {
                            ParseUser.getCurrentUser().put("hasPending", false);
//                            ParseUser.getCurrentUser().saveInBackground();
                        }
                    }
                }
            });

            view.setOnClickListener(this);
        }

        public void bind(Event event) throws ParseException {
            cvCard.setVisibility(View.VISIBLE);

            if (pastEvent) {
                cvCard.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
                cvCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else {
                cvCard.setRadius(40);
                cvCard.setCardElevation(4);
                cvCard.setMaxCardElevation(4);
                if (events.size() <= 1) {
                    cvCard.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
                }
            }

            // set username
            tvCreator.setText("@" + event.getCreator().fetchIfNeeded().getUsername());

            //set date
            tvDate.setText(getRelativeTimeAgo(event.getCreatedAt().toString()));

            // set image (either restaurant of choice or profile picture of organizer)
            final ParseFile creatorImage = (ParseFile) event.getCreator().fetchIfNeeded().get("profilePic");
            if (creatorImage != null) {
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
                        } else {
                            Log.d("test", "Problem load image the data.");
                        }
                    }
                });
            }

            tvRestaurant.setText(event.getName());

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
            Bundle bundle = new Bundle();
            Intent intent = new Intent(context, RestaurantManager.class);
            int position = getAdapterPosition();
            Event event = events.get(position);
            if (!pastEvent) {

            } else {
                bundle.putString("fragment", "ResultsFragment");
                bundle.putParcelable("event", event);
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        }

        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            return relativeDate;
        }
    }
}