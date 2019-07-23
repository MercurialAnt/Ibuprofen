package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Restaurant;

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

        public ViewHolder(@NonNull View itemView) {
           super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvName = itemView.findViewById(R.id.tvName);
        }

        public void bind(Restaurant restaurant) {
            tvCount.setText(String.format("%d", restaurant.getCount()));
            tvName.setText(restaurant.getName());
        }
    }
}
