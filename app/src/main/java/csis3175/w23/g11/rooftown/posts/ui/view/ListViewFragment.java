package csis3175.w23.g11.rooftown.posts.ui.view;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.ui.adapter.ListAdapter;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class ListViewFragment extends Fragment {
    private ListAdapter listAdapter;
    private PostViewModel viewModel;
    private static final String TAG = "POSTS_LIST";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);

        //Setup recycler view
        RecyclerView recyclerListView = view.findViewById(R.id.recyclerList);
        recyclerListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Setup adapter
        if (getParentFragment() != null) {
            PostViewModel viewModel = ((RoommatesFragment) getParentFragment()).getViewModel();

            RecyclerView recyclerList = view.findViewById(R.id.recyclerList);
            recyclerList.setLayoutManager(new LinearLayoutManager(view.getContext()));

            listAdapter = new ListAdapter(new ArrayList<>(), view.getContext(), this::onItemClicked);
            recyclerList.setAdapter(listAdapter);

            viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                Log.d(TAG, "Post List Size " + posts.size());
                listAdapter.populatePosts(posts);
            });
        }
    }
    private void onItemClicked(UUID postId){
        // Start a post detail window when post is clicked. Post id is given by the adapter's ViewHolder
        PostDetailFragment postDetailFragment = PostDetailFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(PostDetailFragment.ARG_POST_ID, postId.toString());
        postDetailFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.roommatesContainer, postDetailFragment);
        transaction.addToBackStack(TAG);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuMessages).setChecked(true);
    }
}