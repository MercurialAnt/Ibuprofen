package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

// adapter to show user's past events
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    private Context context;
    private List<Event> events;
    private ParseUser user;

    public EventAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        this.user = ParseUser.getCurrentUser();
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
        viewHolder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        }

        public void bind(Event event) {
            // set username
            tvCreator.setText(user.getUsername());

            // set image (either restaurant of choice or profile picture of organizer)

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
    }
}
