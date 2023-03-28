package csis3175.w23.g11.rooftown;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class PostDetailFragment extends Fragment {
    private Post post;
    private PostViewModel viewModel;
    public static final String ARG_POST_ID = "post_id";
    private static final String TAG = "POSTS";
    private ImageView imgViewPosting;
    private TextView txtViewPostingLocation;
    private TextView txtViewPostingDescription;

    public static PostDetailFragment newInstance(){
        return new PostDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posting, container,false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancedState){
        super.onViewCreated(view, savedInstancedState);
        viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        ImageView imgViewPosting = view.findViewById(R.id.imgViewPosting);
        TextView txtViewPostingLocation = view.findViewById(R.id.txtViewPostingLocation);
        TextView txtViewPostingDescription = view.findViewById(R.id.txtViewPostingDescription);
        if (null == getArguments() || null == this.getArguments().getString(ARG_POST_ID)){
            Log.e(TAG, "No post is passed to this fragment. Expected " + ARG_POST_ID);
            return;
        }
//        UUID postId = UUID.fromString(this.getArguments().getString(ARG_POST_ID));
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuPosting).setChecked(true);
    }
}