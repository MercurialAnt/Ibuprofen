package com.example.ibuprofen.AttractionsFlow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ibuprofen.Adapters.CategoriesAdapter;
import com.example.ibuprofen.R;
import com.example.ibuprofen.RestaurantFlow.AddMembersFragment;
import com.example.ibuprofen.model.Category;
import com.example.ibuprofen.model.Event;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import static com.example.ibuprofen.RestaurantFlow.FilterFragment.fragmentIntent;

public class AttractionFilterFragment extends Fragment {

    List<Category> categories;
    CategoriesAdapter categoriesAdapter;
    private Activity mActivity;
    private FragmentManager manager;
    Event event;

    Button btnSubmit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        manager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.filter_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnSubmit = view.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            boolean clicked = true;
            @Override
            public void onClick(View v) {
                if (clicked) {
                    clicked = false;

                    // create a new event
                    event = new Event();

                    // set creator for event
                    event.setCreator(ParseUser.getCurrentUser());

                    // add creator to attendee list
                    event.getMembers().add(ParseUser.getCurrentUser());

                    event.setName(getArguments().getString("eventName"));
                    // query acceptable restaurants
                    queryOptions();
                }
            }
        });
    }

    public void queryOptions() {

    }

    public void saveEvent() {
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("RESTACTIVITY", "save failure");
                    e.printStackTrace();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable("event", event);

                fragmentIntent(new AddMembersFragment(), bundle, manager, false);
            }
        });
    }

    public void fillCategories() {
        categories.add(new Category("Amusement Park", "amusementparks", "ic_pizza"));
        categories.add(new Category("Aquarium", "aquariums", "ic_ramen"));
        categories.add(new Category("Beach", "beaches", "ic_burger"));
        categories.add(new Category("Fitness", "fitness", "ic_shrimp"));
        categories.add(new Category("Movie Theather", "movietheaters", "ic_taco"));
        categories.add(new Category("Gallery", "galleries", "ic_ramen2"));
        categories.add(new Category("Museums", "museums", "ic_spat"));
        categories.add(new Category("Parks", "parks", "ic_spat"));
        categories.add(new Category("Lakes", "lakes", "ic_spat"));
    }


}
