package csis3175.w23.g11.rooftown.posts.ui.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.ui.adapter.GridAdapter;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class GridViewFragment extends Fragment {
    private static final String TAG = "POSTS_GRID";
    private GridAdapter gridAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "============ Grid View Created =========");
        //Setup recycler view
        PostViewModel viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);

        RecyclerView recyclerGrid = view.findViewById(R.id.recyclerGrid);
        recyclerGrid.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

        // Setup Adapter
        gridAdapter = new GridAdapter(new ArrayList<>(), view.getContext(), this::onItemClicked);
        viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
            Log.d(TAG, "LiveData published, size: " + posts.size());
            gridAdapter.populatePosts(posts);
        });
        recyclerGrid.setAdapter(gridAdapter);

        //Initialize location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this.getContext(), "GPS Location is disabled.", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                Log.d(TAG, "LiveData published, size: " + posts.size());
                // calculate distance between current location and post location
                GeoLocation currentLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
                Collections.sort(posts, (p1, p2) -> {
                    double distanceP1 = GeoFireUtils.getDistanceBetween(convertLatLngToGeoLocation(p1), currentLocation);
                    double distanceP2 = GeoFireUtils.getDistanceBetween(convertLatLngToGeoLocation(p2), currentLocation);

                    if (distanceP1 < distanceP2) {
                        return -1;
                    } else if (distanceP2 < distanceP1) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                gridAdapter.populatePosts(posts);
            });
        });
    }

    private GeoLocation convertLatLngToGeoLocation (Post post){
        return new GeoLocation(post.getLatLong().latitude, post.getLatLong().longitude);
    }

    private void onItemClicked(UUID postId) {
        // Start a post detail window when post is clicked. Post id is given by the adapter's ViewHolder
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
        transaction.addToBackStack(TAG);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuRoommates).setChecked(true);
    }
}