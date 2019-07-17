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
import com.parse.SignUpCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class SetProfilePicFragment extends PhotoCapture {

    private Button create_btn;
    private ImageView profile_iv;
    private ImageView add_iv;
    private ImageView camera_iv;
    private ImageView gallery_iv;
    private byte[] array;

    // inflate the layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setprofile_fragment, container, false);
    }

    // instantiate views
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        create_btn = view.findViewById(R.id.create_btn);
        profile_iv = view.findViewById(R.id.profile_iv);
        add_iv = view.findViewById(R.id.add_iv);
        camera_iv = view.findViewById(R.id.camera_iv);
        gallery_iv = view.findViewById(R.id.gallery_iv);


        final ParseUser newUser = new ParseUser();

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                newUser.setUsername(bundle.getString("username"));
                newUser.setPassword(bundle.getString("password"));
                newUser.setEmail(bundle.getString("email"));
                newUser.put("name", bundle.getString("name"));

                // -TODO fix this becuase images are still being broken
                if (getPhotoFile() != null) {
                    File file = getPhotoFile();
                    ParseFile parseFile = new ParseFile(file);
                    newUser.put("profilePic", parseFile);
                }
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
        });

        add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_iv.setVisibility(View.VISIBLE);
                gallery_iv.setVisibility(View.VISIBLE);
            }
        });

        camera_iv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 //onLaunchCamera(v);
             }
        });

        gallery_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onPickPhoto(view);
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

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bMapScaled.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                array = stream.toByteArray();

                Glide.with(getContext())
                        .asBitmap()
                        .load(bMapScaled)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_iv);

            }
        }
        if (requestCode == PICK_PHOTO_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getData();

                // Do something with the photo based on Uri
                Bitmap takenImage = null;
                try {
                    takenImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
//                    photoFile = new File(getRealPathFromURI(photoUri));
                    File file = new File(getRealPathFromURI(photoUri));
                    setPhotoFile(file);

                    // Load the taken image into a preview
                    Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 150, 100, true);

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bMapScaled.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    array = stream.toByteArray();

                    Glide.with(getContext())
                            .asBitmap()
                            .load(bMapScaled)
                            .apply(RequestOptions.circleCropTransform())
                            .into(profile_iv);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
