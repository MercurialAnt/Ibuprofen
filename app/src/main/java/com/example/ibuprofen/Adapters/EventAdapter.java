package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantFlow.RestaurantManager;
import com.example.ibuprofen.model.Event;
import com.parse.CountCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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


        public ViewHolder(@NonNull View view) {
            super(view);

            // initialize vars using findById
            ivRestaurant = view.findViewById(R.id.ivRestaurant);
            tvCreator = view.findViewById(R.id.tvCreator);
            tvRestaurant = view.findViewById(R.id.tvEventName);
            tvFriendNumber = view.findViewById(R.id.tvFriendNumber);

            view.setOnClickListener(this);
        }

        public void bind(Event event) throws ParseException {
            // set username
            tvCreator.setText(event.getCreator().fetchIfNeeded().getUsername());
            // set image (either restaurant of choice or profile picture of organizer)
            ParseFile creatorImage = (ParseFile) event.getCreator().fetchIfNeeded().get("profilePic");
            creatorImage.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        // ImageView
                        ivRestaurant.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "Problem load image the data.");
                    }
                }
            });

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
            System.out.println(position);
            Event event = events.get(position);
            if (!pastEvent)
                bundle.putString("fragment", "ChooseFragment");
            else {
                bundle.putString("fragment", "ResultsFragment");
                bundle.putString("votedOn", event.getOptions());
//                intent.putExtra("votedOn", event.getOptions());
            }
            bundle.putParcelable("event", event);
//            intent.putExtra("event", event);
            intent.putExtra("bundle", bundle);
            context.startActivity(intent);
        }
    }
}