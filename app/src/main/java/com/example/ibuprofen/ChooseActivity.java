package com.example.ibuprofen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.ibuprofen.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

// this is where they'll choose options
public class ChooseActivity extends AppCompatActivity {
    private RecyclerView rvChoose;
    private ChooseAdapter adapter;
    protected List<Restaurant> mChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        rvChoose = view.findViewById(R.id.rvChoose);
//
//        //create data source
//        mChoices = new ArrayList<>();
//        //create adapter
//        adapter = new ChooseAdapter(this, mChoices);
//        //set the adapter on the recycler view
//        rvChoose.setAdapter(adapter);
//        //set layout manager on recycler view
//        rvChoose.setLayoutManager(new LinearLayoutManager(this));
//        displayChoices();
//    }

    private void displayChoices () {
    }
}
