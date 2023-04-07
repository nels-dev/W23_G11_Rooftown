package csis3175.w23.g11.rooftown.posts.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.LocationHelper;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    public static final int NO_OF_MARKERS_IN_INITIAL_VIEW = 5;
    private static final String TAG = "POSTS_MAP";
    private GoogleMap mMap;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = view.findViewById(R.id.map_container);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        // async get a list of posting
        // extract coordinate(lat lng)
        // mark in the map view
        if (getParentFragment() != null && getView() != null) {
            PostViewModel viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
            viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                addPostMarkers(posts);
                LocationHelper.performWithLocation(requireActivity(), location -> {
                    LatLngBounds.Builder bound = new LatLngBounds.Builder();

                    if (location != null) {
                        bound.include(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                    if (!posts.isEmpty()) {
                        for (int i = 0; i < Math.min(posts.size(), NO_OF_MARKERS_IN_INITIAL_VIEW); i++) {
                            bound.include(posts.get(i).getLatLong());
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bound.build(), 200));
                });
            });
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }

            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                Post post = (Post) marker.getTag();
                // Create a custom view for the info window
                View infoWindowView = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_info_window, null);
                // Set the title and snippet of the info window based on the marker's title and snippet
                TextView titleTextView = infoWindowView.findViewById(R.id.txtViewInfoWindowTitle);
                titleTextView.setTextColor(Color.BLACK);
                titleTextView.setGravity(Gravity.CENTER);
                titleTextView.setTypeface(null, Typeface.BOLD);
                titleTextView.setText(marker.getTitle());

                TextView snippetTextView = infoWindowView.findViewById(R.id.txtViewInfoWindowSnippet);
                snippetTextView.setTextColor(Color.GRAY);
                snippetTextView.setText(marker.getSnippet());

                return infoWindowView;
            }
        });

        mMap.setOnMarkerClickListener(marker -> {
            Post post = (Post) marker.getTag();
            if (post != null) {
                marker.setTitle(((post.getPostType() == PostType.ROOM) ? post.getLocation() : post.getInitiatorName()));
                marker.setSnippet(((post.getPostType() == PostType.ROOM) ? post.getRoomDescription() : post.getInitiatorDescription()));
                marker.showInfoWindow();
            }
            return true;
        });

        mMap.setOnInfoWindowClickListener(marker -> {
            Post post = (Post) marker.getTag();
            openPostDetail(post.getPostId());
        });
    }

    private void openPostDetail(UUID postId) {
        PostDetailFragment postDetailFragment = PostDetailFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(PostDetailFragment.ARG_POST_ID, postId.toString());
        postDetailFragment.setArguments(args);

        FragmentTransaction transaction;
        if (getParentFragment() != null) {
            transaction = getParentFragment().getParentFragmentManager().beginTransaction();
        } else {
            transaction = getParentFragmentManager().beginTransaction();
        }
        transaction.replace(R.id.mainContainer, postDetailFragment);
        transaction.addToBackStack(RoommatesFragment.TAG);
        transaction.commit();
    }

    private void addPostMarkers(List<Post> posts) {
        for (Post post : posts) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(post.getLatLong());
            markerOptions.title((post.getPostType() == PostType.ROOM) ? post.getLocation() : post.getInitiatorName());
            Marker marker = mMap.addMarker(markerOptions);
            if (marker != null) {
                marker.setTag(post);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

//        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
//            fusedLocationProviderClient.requestLocationUpdates(new LocationRequest().setInterval(5000), locationCallback, null);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}