package com.example.ibuprofen.MovieFlow;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ibuprofen.API.MovieAPI;
import com.example.ibuprofen.API.YelpAPI;
import com.example.ibuprofen.Adapters.CategoriesAdapter;
import com.example.ibuprofen.OkSingleton;
import com.example.ibuprofen.R;
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

public class MovieFilterFragment extends Fragment {

    SeekBar sbMovieLength;
    Button btnSubmit;
    TextView tvHours;
    RecyclerView rvGenres;
    RecyclerView rvServices;
    FragmentManager manager;
    Activity mActivity;
    Event event;
    List<Category> genres;
    List<String> chosenGenres;
    List<Category> services;
    List<String> chosenServices;
    CategoriesAdapter genreAdapter;
    CategoriesAdapter servicesAdapter;
    int hours;

    // todo--use when we implement imdb
//    CheckBox cbG;
//    CheckBox cbPG;
//    CheckBox cbPG13;
//    CheckBox cbR;

    // list of movies that fit the criteria
    String options;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        manager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_filter_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)mActivity).getSupportActionBar().hide();
        sbMovieLength = view.findViewById(R.id.sbLength);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        tvHours = view.findViewById(R.id.tvHours);
        options = "";

//        cbG = view.findViewById(R.id.cbG);
//        cbPG = view.findViewById(R.id.cbPG);
//        cbPG13 = view.findViewById(R.id.cbPG13);
//        cbR = view.findViewById(R.id.cbR);

        // genre set up
        rvGenres = view.findViewById(R.id.rvGenres);
        genres = new ArrayList<>();
        chosenGenres = new ArrayList<>();
        fillGenres();
        genreAdapter = new CategoriesAdapter(getContext(), genres, chosenGenres);
        rvGenres.setAdapter(genreAdapter);
        rvGenres.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // services set up
        rvServices = view.findViewById(R.id.rvServices);
        services = new ArrayList<>();
        chosenServices = new ArrayList<>();
        fillServices();
        servicesAdapter = new CategoriesAdapter(getContext(), services, chosenServices);
        rvServices.setAdapter(servicesAdapter);
        rvServices.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        sbMovieLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHours.setText(seekBar.getProgress() + " hours");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours =  sbMovieLength.getProgress();
                if (hours == 0) {
                    hours = 5;
                }

                // event info
                event = new Event();
                event.setCreator(ParseUser.getCurrentUser());
                event.getMembers().add(ParseUser.getCurrentUser());
                event.setName(getArguments().getString("eventName"));

                // query movies/shows
                queryOptions();
            }
        });
    }

    public void fillServices() {
        // todo--confirm API names
        services.add(new Category("HBO", "hbo", "ic_pizza"));
        services.add(new Category("Hulu", "hulu", "ic_pizza"));
        services.add(new Category("Netflix", "netflix", "ic_pizza"));
        services.add(new Category("Amazon Prime", "amazon_prime", "ic_pizza"));
        services.add(new Category("Youtube", "youtube", "ic_pizza"));
    }

    public void fillGenres() {
        // todo--confirm API names, get icons
        genres.add(new Category("Drama", "drama", "ic_pizza"));
        genres.add(new Category("Comedy", "comedy", "ic_pizza"));
    }

    public void queryOptions() {
        MovieAPI test = new MovieAPI(getContext());
        OkHttpClient client = OkSingleton.getInstance();
        //todo--finish the call
    }
}
