package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.ui.adapter.PostGridAdapter;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class GridViewFragment extends Fragment {
    private PostGridAdapter postGridAdapter;
    private static final String TAG = "POSTS";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getParentFragment() != null) {
            PostViewModel viewModel = ((RoommatesFragment) getParentFragment()).getViewModel();

            RecyclerView recyclerViewPostsGrid = view.findViewById(R.id.recyclerViewPostsGrid);
            recyclerViewPostsGrid.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

            postGridAdapter = new PostGridAdapter(new ArrayList<>(), view.getContext());
            recyclerViewPostsGrid.setAdapter(postGridAdapter);

            viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                Log.d(TAG, "Post List Size: " + posts.size());
                postGridAdapter.populatePosts(posts);
            });
        }
    }
}