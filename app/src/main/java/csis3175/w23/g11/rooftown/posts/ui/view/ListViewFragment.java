package csis3175.w23.g11.rooftown.posts.ui.view;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.common.LocationHelper;
import csis3175.w23.g11.rooftown.databinding.FragmentListViewBinding;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.ui.adapter.ListAdapter;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class ListViewFragment extends Fragment {
    private static final String TAG = "POSTS_LIST";
    private ListAdapter listAdapter;
    private Location currentLocation;
    private FragmentListViewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PostViewModel viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        //Setup recycler view

        binding.recyclerList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listAdapter = new ListAdapter(new ArrayList<>(), this::onItemClicked);
        binding.recyclerList.setAdapter(listAdapter);

        LocationHelper.performWithLocation(requireActivity(), loc -> {
            this.currentLocation = loc;
            if (this.getView() != null) {
                viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                    Log.d(TAG, "LiveData published, size: " + posts.size());
                    sortByDistance(posts);
                    listAdapter.populatePosts(posts);
                });
                viewModel.loadData(new LatLng(loc.getLatitude(), loc.getLongitude()), CurrentUserHelper.getCurrentUid());
            }
        });
    }


    private void sortByDistance(List<Post> posts) {
        if (currentLocation == null) return;
        GeoLocation gloc = new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        Collections.sort(posts, (p1, p2) -> {
            double distanceP1 = GeoFireUtils.getDistanceBetween(convertLatLngToGeoLocation(p1), gloc);
            double distanceP2 = GeoFireUtils.getDistanceBetween(convertLatLngToGeoLocation(p2), gloc);

            if (distanceP1 < distanceP2) {
                return -1;
            } else if (distanceP2 < distanceP1) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    private GeoLocation convertLatLngToGeoLocation(Post post) {
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