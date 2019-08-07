package com.example.ibuprofen.RestaurantFlow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ibuprofen.Adapters.CategoriesAdapter;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
import com.example.ibuprofen.API.YelpAPI;
import com.example.ibuprofen.model.Category;
import com.example.ibuprofen.model.Event;
import com.example.ibuprofen.model.Restaurant;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.graphics.Color.*;

public class FilterFragment extends Fragment {

    SeekBar sbDistance;
    Button btnSubmit;
    ToggleButton btnOne;
    ToggleButton btnTwo;
    ToggleButton btnThree;
    ToggleButton btnFour;
    TextView tvMiles;
    RecyclerView rvCuisine;
    FragmentManager manager;
    Activity mActivity;
    Event event;

    final double meterToMile = 1609;
    AtomicInteger count;

    // filter options that come with set defaults
    int dist = (int) (5 * meterToMile);
    ArrayList<Integer> price;
    List<Category> categories;
    List<String> choosen;
    CategoriesAdapter categoriesAdapter;

    // list of restaurants that fit the criteria
    String options;

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
        ((AppCompatActivity)mActivity).getSupportActionBar().hide();

        sbDistance = view.findViewById(R.id.sbLength);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnOne = view.findViewById(R.id.btnOne);
        btnTwo = view.findViewById(R.id.btnTwo);
        btnThree = view.findViewById(R.id.btnThree);
        btnFour = view.findViewById(R.id.btnFour);
        rvCuisine = view.findViewById(R.id.rvCuisine);
        tvMiles = view.findViewById(R.id.tvMiles);

        options = "";
        price = new ArrayList<>();
        categories = new ArrayList<>();
        choosen = new ArrayList<>();
        fillCategoryList();
        categoriesAdapter = new CategoriesAdapter(getContext(), categories, choosen);

        rvCuisine.setAdapter(categoriesAdapter);
        rvCuisine.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        btnOne.setOnCheckedChangeListener(new MoneyListen(1));
        btnTwo.setOnCheckedChangeListener(new MoneyListen(2));
        btnThree.setOnCheckedChangeListener(new MoneyListen(3));
        btnFour.setOnCheckedChangeListener(new MoneyListen(4));

        // set on click listener for submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets distance information
                String enteredDistance = sbDistance.getProgress() + "";
                if (!enteredDistance.equals("")) {
                    dist = (int) (Double.parseDouble(enteredDistance) * meterToMile); // convert to miles
                }

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
        });

        sbDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMiles.setText(seekBar.getProgress() + " miles");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    // gets options within radius and updates them in database
    public void queryOptions() {
        YelpAPI test = new YelpAPI(getContext());
        Location gpsLocation = test.getLocationByProvider(LocationManager.GPS_PROVIDER);
        OkHttpClient client = OkSingleton.getInstance();
        client.newCall(test.getFilteredRestaurants(dist, getIntXml(getContext(), R.integer.result_limit), gpsLocation, choosen, price)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Feed", "Did not work");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray array = obj.getJSONArray("businesses");
                        final ParseRelation<Restaurant> relation = event.getRelation("stores");
                        count = new AtomicInteger(array.length());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject store = array.getJSONObject(i);
                            final Restaurant restaurant = Restaurant.fromJSON(store);
                            restaurant.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        relation.add(restaurant);
                                        if (count.decrementAndGet() == 0) {
                                            saveEvent();
                                        } else {
                                            event.saveInBackground();
                                        }
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        Log.d("RESTACTIVITY", options);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d("RESTACTIVITY", "fail");
                }
            }
        });
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

    public static void fragmentIntent(Fragment nextFragment, Bundle bundle, FragmentManager manager, boolean back) {
        FragmentTransaction transaction = manager.beginTransaction();
        nextFragment.setArguments(bundle);
        transaction.replace(R.id.flRestaurant, nextFragment);
        if (back)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    public void fillCategoryList() {
        categories.add(new Category("Pizza", "pizza", "ic_pizza"));
        categories.add(new Category("Chinese", "chinese", "ic_ramen"));
        categories.add(new Category("Burgers", "burgers", "ic_burger"));
        categories.add(new Category("Seafood", "seafood", "ic_shrimp"));
        categories.add(new Category("Mexican", "mexican", "ic_taco"));
        categories.add(new Category("Thai", "thai", "ic_ramen2"));
        categories.add(new Category("Italian", "italian", "ic_spat"));
        categories.add(new Category("Steakhouses", "steak", "ic_strak"));
        categories.add(new Category("Korean", "korean", "ic_spat2"));
        categories.add(new Category("Japanese", "japanese", "ic_fan"));
        categories.add(new Category("Sandwiches", "sandwiches", "ic_sandwhich"));
        categories.add(new Category("Breakfast", "breakfast_brunch", "ic_egg"));
        categories.add(new Category("Vietnamese", "vietnamese", "ic_frying_pan"));
        categories.add(new Category("Vegetarian", "vegetarian", "ic_leaf"));
        categories.add(new Category("Sushi Bars", "sushi", "ic_sushi"));
        categories.add(new Category("American", "tradamerican", "ic_bread"));
    }

    class MoneyListen implements CompoundButton.OnCheckedChangeListener {

        int level;
        public MoneyListen(int priceLevel) {
            this.level = priceLevel;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                price.add(level);
                buttonView.setTextColor(WHITE);
            } else {
                price.remove(new Integer(level));
                buttonView.setTextColor(Color.parseColor("#794d7e"));
            }
        }
    }

    public static int getIntXml(Context con, int id) {
        return con.getResources().getInteger(id);
    }
}
