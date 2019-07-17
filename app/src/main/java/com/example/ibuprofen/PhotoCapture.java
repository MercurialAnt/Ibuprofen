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
    public String photoFileName;
    private File photoFile;
    Context context;


    public PhotoCapture() {
        this.context = getContext();
    }


    public void onLaunchCamera(View view) {
        // Creating an intent to launch camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        checkFile();

        // If there is a valid file start the activity
        if (photoFile != null && takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.example.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        checkFile();

        if (photoFile != null && pickPictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(pickPictureIntent, PICK_PHOTO_CODE);
        }
    }

    public void checkFile() {
        // Create file to store image
        photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e(TAG, "Error creating the file for the image");
        }
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // by this point we have the camera photo on disk
//                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//                // Load the taken image into a preview
//                Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 150, 100, true);
//
//                ivPostImage.setImageBitmap(bMapScaled);
//                ivPostImage.setVisibility(View.VISIBLE);
//            } else { // Result was a failure
//                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (requestCode == PICK_PHOTO_CODE) {
//            if (resultCode == RESULT_OK && data != null) {
//                Uri photoUri = data.getData();
//
//                // Do something with the photo based on Uri≈
//
//                Bitmap takenImage = null;
//                try {
//                    takenImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoUri);
//                    photoFile = new File(getRealPathFromURI(context, photoUri));
//
//                    // Load the taken image into a preview
//                    Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 150, 100, true);
//                    ivPostImage.setImageBitmap(bMapScaled);
//                    ivPostImage.setVisibility(View.VISIBLE);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }
//    }


}
