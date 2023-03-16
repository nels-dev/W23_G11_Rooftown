package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.ui.adapter.ListAdapter;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class ListViewFragment extends Fragment {
    private ListAdapter listAdapter;
    private static final String TAG = "POSTS_LIST";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
//        ListView listViewPost = rootView.findViewById(R.id.listViewPost);
//        ArrayAdapter<LatLng> siteArrAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,locationList);
//        listViewPost.setAdapter(siteArrAdapter)

        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getParentFragment() != null) {
            PostViewModel viewModel = ((RoommatesFragment) getParentFragment()).getViewModel();

            ListView listView = view.findViewById(R.id.listViewPost);
            listAdapter = new ListAdapter(new ArrayList<>(), view.getContext());

            listView.setAdapter(listAdapter);

            viewModel.getAllPosts().observe(this.getViewLifecycleOwner(), posts -> {
                Log.d(TAG, "Post List Size: " + posts.size());
                listAdapter.populatePosts(posts);
            });
        }
    }
}