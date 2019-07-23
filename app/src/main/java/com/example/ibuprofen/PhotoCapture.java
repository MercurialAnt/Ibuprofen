package com.example.ibuprofen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 * This was made so that fragment could receive images from the camera or the device.
 * Inside your fragment you need to implement your own onActivityResult inorder to set the image
 * view for your layout. A template below shows how it might look.
 */
public class PhotoCapture extends Fragment {

    final String TAG = "PhotoCapture";

    public static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int PICK_PHOTO_CODE = 200;
    public String photoFileName = "photo.jpg";
    private File photoFile;
    Context context;

    public void onLaunchCamera(View view) {
        // Creating an intent to launch camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context = getContext();
        checkFile();

        // If there is a valid file start the activity
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST_CODE);
            }
        }
    }

    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context = getContext();
        checkFile();

        if (photoFile != null && pickPictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(pickPictureIntent, PICK_PHOTO_CODE);
        }
    }

    public File getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(File file) {
        photoFile = file;
    }

    public ParseFile getParseFile() throws ParseException {
        final ParseFile image = new ParseFile(getPhotoFile());
        if (image.isDirty()) {
            image.save();
        }
        return image;
    }

    public void checkFile() {
        // Create file to store image
        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        photoFile = getPhotoFileUri(photoFileName);
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoFileName = image.getAbsolutePath();
        return image;
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs())
            Log.d(TAG, "failed to create directory");

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
