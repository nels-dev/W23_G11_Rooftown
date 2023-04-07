package csis3175.w23.g11.rooftown.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;

public class LocationHelper {

    public static void performWithLocation(Activity activity, CallbackListener<Location> locationCallback) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "You have not granted permission to access your location. The functionality will be limited.", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.getFusedLocationProviderClient(activity).getLastLocation().addOnSuccessListener(locationCallback::callback);
    }

    public static void retryUntilCurrentLocationIsAvailable(Activity activity, CallbackListener<Location> locationCallback) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get location
            LocationServices.getFusedLocationProviderClient(activity)
                    .getCurrentLocation(100, new CancellationTokenSource().getToken())
                    .addOnSuccessListener(activity, location -> {
                        if (location != null) {
                            locationCallback.callback(location);
                        }
                    }).addOnFailureListener((exception) -> {
                        Log.e("LOCATION_HELPER", "Exception getting location", exception);
                        new Handler().postDelayed(() -> retryUntilCurrentLocationIsAvailable(activity, locationCallback), 5000);
                    });
        } else {
            Log.w("LOCATION_HELPER", "Location permission not granted, retrying after 5 seconds");
            new Handler().postDelayed(() -> retryUntilCurrentLocationIsAvailable(activity, locationCallback), 5000);
        }
    }
}
