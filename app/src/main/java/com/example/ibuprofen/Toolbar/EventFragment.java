package com.example.ibuprofen.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantActivity;

public class EventFragment extends Fragment {
    // instance vars
    ImageView restaurant_iv;
    ImageView createYourOwn_iv;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // initialize vars
        restaurant_iv = view.findViewById(R.id.ivRestaurant);
        createYourOwn_iv = view.findViewById(R.id.ivCreateYourOwn);

        // set onclick listener for restaurant button
        restaurant_iv.setOnClickListener(new View.OnClickListener() {

            // onClick bring it to restaurants filter fragment
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), RestaurantActivity.class);
                startActivity(i);
            }
        });
    }
}
