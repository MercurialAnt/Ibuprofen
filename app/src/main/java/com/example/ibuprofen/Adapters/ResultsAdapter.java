package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        private RecyclerView rvPeople;
        private List<Pair<String, String>> people;
        private PeopleAdapter peopleAdapter;

        public ViewHolder(@NonNull View itemView) {
           super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvName = itemView.findViewById(R.id.tvName);

            people = new ArrayList<>();
            rvPeople = itemView.findViewById(R.id.rvPeople);
            peopleAdapter = new PeopleAdapter(context, people);
            rvPeople.setAdapter(peopleAdapter);
            rvPeople.setLayoutManager(new GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false));
        }

        public void bind(Restaurant restaurant) {
            peopleAdapter.clear();
            try {
                if (restaurant.getPeople() != null) {
                    JSONArray voters = new JSONArray(restaurant.getPeople());
                    addPeople(voters);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvCount.setText(String.format("%d", restaurant.getCount()));
            tvName.setText(restaurant.getName());
        }

        public void addPeople(JSONArray list) {
            try {
                for (int i = 0; i < list.length(); i++) {
                    JSONObject person = list.getJSONObject(i);
                    String url = person.getString("image");
                    String name = person.getString("name");
                    Pair<String, String> pair = new Pair<>(name, url);
                    people.add(pair);
                }
                peopleAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
