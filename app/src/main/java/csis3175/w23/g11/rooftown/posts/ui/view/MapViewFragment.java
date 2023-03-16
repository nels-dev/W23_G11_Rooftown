package csis3175.w23.g11.rooftown.posts.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private static final String TAG = "POSTS_MAP";

    private final List<Post> postList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = view.findViewById(R.id.map_container);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // async get a list of posting
        // extract coordinate(lat lng)
        // mark in the map view
        if (getParentFragment() != null) {
            PostViewModel viewModel = ((RoommatesFragment) getParentFragment()).getViewModel();
            viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                Log.d(TAG, "getAllPosts");
                postList.clear();
                postList.addAll(posts);
                addPostMarkers(postList);
                if (!postList.isEmpty()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(postList.get(0).getLatLong()));
                }
            });
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });
        }

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if (locationResult == null){
                    return;
                }
                for(Location location: locationResult.getLocations()){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }
        };
//        fusedLocationProviderClient.requestLocationUpdates(new LocationRequest().setInterval(10000), locationCallback, null);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }   // use default info window background

            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                // Create a custom view for the info window
                View infoWindowView = getLayoutInflater().inflate(R.layout.layout_custom_info_window, null);

                // Set the title and snippet of the info window based on the marker's title and snippet
                TextView titleTextView = infoWindowView.findViewById(R.id.txtViewInfoWindowTitle);
                titleTextView.setTextColor(Color.BLACK);
                titleTextView.setGravity(Gravity.CENTER);
                titleTextView.setTypeface(null, Typeface.BOLD);
                titleTextView.setText(marker.getTitle());

                TextView snippetTextView = infoWindowView.findViewById(R.id.txtViewInfoWindowSnippet);
                snippetTextView.setTextColor(Color.GRAY);
                snippetTextView.setText(marker.getSnippet());

//                ImageView imgViewInfoWin = infoWindowView.findViewById(R.id.imgViewInfoWin);
                return infoWindowView;
            }
        });

        mMap.setOnMarkerClickListener(marker -> {
            Post post = (Post)marker.getTag();
            if (post != null) {
                marker.setTitle(((post.getPostType() == PostType.ROOM)?post.getLocation():post.getInitiatorName()));
                marker.setSnippet(((post.getPostType() == PostType.ROOM)?post.getRoomDescription():post.getInitiatorDescription()));

                marker.showInfoWindow();
            }
            return true;
        });
    }

    private void addPostMarkers(List<Post> posts) {
        for (Post post : posts) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(post.getLatLong());
            markerOptions.title((post.getPostType() == PostType.ROOM)?post.getLocation():post.getInitiatorName());
            Marker marker = mMap.addMarker(markerOptions);
            if (marker!= null) {
                marker.setTag(post);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();

//        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
//            fusedLocationProviderClient.requestLocationUpdates(new LocationRequest().setInterval(5000), locationCallback, null);
//        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
}