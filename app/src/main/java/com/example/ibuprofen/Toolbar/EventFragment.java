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
    ImageView ivRestaurant;
    ImageView ivCreateYourOwn;

    //onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // initialize vars
        ivRestaurant = view.findViewById(R.id.ivRestaurant);

        // set onclick listener for restaurant button
        ivRestaurant.setOnClickListener(new View.OnClickListener() {

            // onClick bring it to restaurants filter fragment
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), RestaurantActivity.class);
                startActivity(i);
            }
        });
    }
}
