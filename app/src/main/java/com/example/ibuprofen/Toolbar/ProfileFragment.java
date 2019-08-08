package com.example.ibuprofen.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.Adapters.EventAdapter;
import com.example.ibuprofen.HomeActivity;
import com.example.ibuprofen.PhotoCapture;
import com.example.ibuprofen.R;
import com.example.ibuprofen.model.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends PhotoCapture {
    // instance vars
    private Button btnSignout;
    private TextView tvUsername;
    private TextView tvName;
    private ImageView ivProfile;
    private ImageView ivAdd;
    private ImageView ivCamera;
    private ImageView ivGallery;
    private ImageView ivRefresh;
    private ConstraintLayout clRefresh;
    private RecyclerView rvFeed;
    private ParseUser user;
    boolean currentUser; //todo--make this applicable to viewing other profiles
    private List<Event> eventsList;
    private EventAdapter eventAdapter;
    private boolean show;
    TextView tvPendingLabel;
    FrameLayout flReviews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        insertNestedFragment();

        // initialize vars
        tvUsername = view.findViewById(R.id.tvCreator);
        tvName = view.findViewById(R.id.tvName);
        rvFeed = view.findViewById(R.id.rvPastEvents);
        ivProfile = view.findViewById(R.id.ivProfileImage);
        ivGallery = view.findViewById(R.id.ivGallery);
        ivRefresh = view.findViewById(R.id.ivRefresh);
        clRefresh = view.findViewById(R.id.clRefresh);
        ivCamera = view.findViewById(R.id.ivCamera);
        ivAdd = view.findViewById(R.id.ivAdd);
        eventsList = new ArrayList<>();
        tvPendingLabel = view.findViewById(R.id.tvPendingLabel);
        flReviews = view.findViewById(R.id.flReviews);

        show = true;

        // set current user and set username, name, and image
        user = ParseUser.getCurrentUser();
        tvUsername.setText("@" + user.getUsername());
        tvName.setText(user.getString("name"));

        // set up chooseAdapter
        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(getContext(), eventsList, true);
        rvFeed.setAdapter(eventAdapter);

        // get past events by user
        queryUserEvents();

        if (!ParseUser.getCurrentUser().getBoolean("hasPending")) {
            tvPendingLabel.setVisibility(view.GONE);
//            flReviews.setVisibility(view.GONE);
            ivRefresh.setVisibility(view.GONE);
        }
        else {
            tvPendingLabel.setVisibility(view.VISIBLE);
//            flReviews.setVisibility(view.VISIBLE);
            ivRefresh.setVisibility(view.VISIBLE);
        }

        // load profile image
        ParseFile profile = user.getParseFile("profilePic");
        if (profile != null) {
            Glide.with(getContext())
                    .load(profile.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
        }
        else {
            Glide.with(getContext())
                    .load("@drawable/anonymous")
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivProfile);
        }

        // set on click listener for sign out button
        btnSignout = view.findViewById(R.id.btnSignout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logs user out
                ParseUser.logOut();

                // goes back to sign in page
                Intent i = new Intent(getContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNestedFragment();
            }
        });

        clRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNestedFragment();
            }
        });

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show) {
                    showIcons();
                    show = false;
                } else {
                    removeIcons();
                    show = true;
                }
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
                removeIcons();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto(v);
                removeIcons();
            }
        });
    }

    public void showIcons() {
        ivCamera.setVisibility(View.VISIBLE);
        ivGallery.setVisibility(View.VISIBLE);
    }

    public void removeIcons() {
        ivCamera.setVisibility(View.GONE);
        ivGallery.setVisibility(View.GONE);
    }

    public void saveImage() {
        if (getPhotoFile() != null) {
            File file = getPhotoFile();
            final ParseFile parseFile = new ParseFile(file);
            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        user.put("profilePic", parseFile);
                        user.saveInBackground();
                    } else {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(getPhotoFile().getAbsolutePath());
                // Load the taken image into a preview
                Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 150, 100, true);

                Glide.with(getContext())
                        .asBitmap()
                        .load(bMapScaled)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfile);
                saveImage();

            }
        }
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getData();

                // Do something with the photo based on Uri
                Bitmap takenImage = null;
                try {
                    takenImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
                    File file = new File(getRealPathFromURI(photoUri));
                    setPhotoFile(file);

                    // Load the taken image into a preview
                    Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 150, 100, true);


                    Glide.with(getContext())
                            .asBitmap()
                            .load(bMapScaled)
                            .apply(RequestOptions.circleCropTransform())
                            .into(ivProfile);
                    saveImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void insertNestedFragment() {
        Fragment pendingFragment = new PendingFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flReviews, pendingFragment).commit();
    }

    private void queryUserEvents() {
        ParseQuery<Event> postQuery = new ParseQuery<>(Event.class);
        postQuery.include(Event.KEY_USERS);
        postQuery.whereEqualTo("attendees", ParseUser.getCurrentUser());
        postQuery.whereEqualTo("hasVoted", ParseUser.getCurrentUser());
        // since it's an expensive operation you want to do this in a background thread not in the
        // same thread as the UI
        postQuery.orderByDescending("createdAt");
        postQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null) {
                    Log.e("ComposeActivity", "query failed");
                    e.printStackTrace();
                    return;
                }
                eventsList.addAll(events);
                eventAdapter.notifyDataSetChanged();
                Log.d("ProfileFragment", "number of posts: " + eventsList.size());
            }
        });
    }

//    private int getNumberPending() {
//        ParseQuery<Event> postQuery = new ParseQuery<>(Event.class);
//        postQuery.include(Event.KEY_USERS);
//        postQuery.whereEqualTo("attendees", ParseUser.getCurrentUser());
//        postQuery.whereNotEqualTo("hasVoted", ParseUser.getCurrentUser());
//        final int[] numPending = {0};
//
//        postQuery.countInBackground(new CountCallback() {
//            @Override
//            public void done(int count, ParseException e) {
//                if (count != 0) {
//                    ParseUser.getCurrentUser().put("hasPending", true);
//                    numPending[0] = count;
//                }
//                else {
//                    ParseUser.getCurrentUser().put("hasPending", false);
//                }
//                ParseUser.getCurrentUser().saveInBackground();
//            }
//        });
//        return numPending[0];
//    }
}