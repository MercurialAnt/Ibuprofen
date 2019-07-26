package com.example.ibuprofen.RestaurantFlow;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ibuprofen.Adapters.ChooseAdapter;
import com.example.ibuprofen.Controllers.SwipeController;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.ibuprofen.RestaurantFlow.FilterFragment.fragmentIntent;

public class ChooseFragment extends Fragment {

    private RecyclerView rvChoose;
    private Button btnDone;
    protected ChooseAdapter adapter;
    protected List<Restaurant> mChoices;
    private Context context;
    Event event;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        // set up list
        mChoices = new ArrayList<>();


        // initialize vars and adapter
        event = getArguments().getParcelable("event");

        rvChoose = view.findViewById(R.id.rvChoose);
        btnDone = view.findViewById(R.id.btnDone);
        adapter = new ChooseAdapter(context, mChoices, rvChoose, getActivity());
        rvChoose.setAdapter(adapter);
        rvChoose.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        SwipeController swipeController = new SwipeController(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(rvChoose);

        try {
            populateChoices();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String newJson = change_counts(event);
                bundle.putParcelable("event", event);
                bundle.putString("votedOn", newJson);
                fragmentIntent(new ResultsFragment(), bundle, getFragmentManager(), true);
            }
        });

    }

    public synchronized String change_counts(Event currentEvent) {
        String newJson = null;
        String options = currentEvent.getOptions();
        try {
            JSONArray places = new JSONArray(options);
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                // get current count
                int curr = place.getInt("count");
                place.put("count", curr + adapter.counters[i]);
            }
            newJson = places.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newJson;
    }

    private void populateChoices() throws JSONException {
        JSONArray options = new JSONArray(event.getOptions());
        for (int i = 0; i < options.length(); i++) {
            Restaurant restaurant = Restaurant.fromJSON((JSONObject) options.get(i));
            mChoices.add(restaurant);
            adapter.notifyDataSetChanged();
        }
    }


}
