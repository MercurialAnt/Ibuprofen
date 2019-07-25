package com.example.ibuprofen.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ibuprofen.R;

import java.util.List;

public class SlidingImageAdapter extends PagerAdapter {

    private List<String> urls;
    private Context context;
    private LayoutInflater layoutInflater;

    public SlidingImageAdapter(Context context, List<String> urls) {
        this.urls = urls;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = layoutInflater.inflate(R.layout.sliding_image, container, false);
        ImageView imageView = imageLayout.findViewById(R.id.ivViewSlider);

        Glide.with(context)
                .load(urls.get(position))
                .into(imageView);
        container.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
