package com.example.ibuprofen.API;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import okhttp3.HttpUrl;
import okhttp3.Request;

import static com.parse.Parse.getApplicationContext;

public class BaseAPI {

    private final String auth_key_header = "Authorization";

    public Request getAuthRequest(HttpUrl url, String value_header) {
        return new Request.Builder()
                .get()
                .url(url)
                .addHeader(auth_key_header, value_header)
                .build();
    }



    public Location getLocationByProvider(String provider) {
        Location location = null;

        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return location;
                }
                location = locationManager.getLastKnownLocation(provider);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return location;
    }
}
