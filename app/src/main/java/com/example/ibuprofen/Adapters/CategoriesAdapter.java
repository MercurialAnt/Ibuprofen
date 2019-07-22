package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    Context context;
    List<Category> icons;
    List<String> choosen;

    public CategoriesAdapter(Context context, List<Category> icons, List<String> choosen) {
        this.context = context;
        this.icons = icons;
        this.choosen = choosen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuisine, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(icons.get(position));

    }
    @Override
    public int getItemCount() {
        return icons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIcon;
        private TextView tvFoodType;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvFoodType = itemView.findViewById(R.id.tvFoodType);
            ivIcon = itemView.findViewById(R.id.ivIcon);

            ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setBackgroundColor(Color.parseColor("#F08080"));
                    choosen.add(tvFoodType.getHint().toString());
                    Log.d("hello", "clicked");
                }
            });

        }

        public void bind(Category icon) {
            tvFoodType.setText(icon.getName());
            tvFoodType.setHint(icon.getApi_name());
        }
    }
}
