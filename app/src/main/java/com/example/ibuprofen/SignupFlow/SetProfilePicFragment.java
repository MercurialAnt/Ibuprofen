package com.example.ibuprofen.SignupFlow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ibuprofen.MainActivity;
import com.example.ibuprofen.PhotoCapture;
import com.example.ibuprofen.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class SetProfilePicFragment extends PhotoCapture {

    private Button btnCreate;
    private ImageView ivProfile;
    private ImageView ivAdd;
    private ImageView ivCamera;
    private ImageView ivGallery;


    // inflate the layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setprofile_fragment, container, false);
    }

    // instantiate views
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        btnCreate = view.findViewById(R.id.btnCreate);
        ivProfile = view.findViewById(R.id.ivProfileImage);
        ivAdd = view.findViewById(R.id.ivAdd);
        ivCamera = view.findViewById(R.id.ivCamera);
        ivGallery = view.findViewById(R.id.ivGallery);


        final ParseUser newUser = new ParseUser();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                newUser.setUsername(bundle.getString("username"));
                newUser.setPassword(bundle.getString("password"));
                newUser.setEmail(bundle.getString("email"));
                newUser.put("name", bundle.getString("name"));

                File file;

                if (getPhotoFile() != null) {
                    file = getPhotoFile();
                    final ParseFile parseFile = new ParseFile(file);
                    parseFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                newUser.put("profilePic", parseFile);
                                add_user(newUser);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    add_user(newUser);
                }





            }
        });


        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivCamera.setVisibility(View.VISIBLE);
                ivGallery.setVisibility(View.VISIBLE);
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 onLaunchCamera(v);
             }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto(v);
            }
        });
    }

    public void add_user(ParseUser newUser) {
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    final Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
