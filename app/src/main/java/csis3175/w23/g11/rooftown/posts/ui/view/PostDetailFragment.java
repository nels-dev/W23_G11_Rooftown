package csis3175.w23.g11.rooftown.posts.ui.view;

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

import org.w3c.dom.Text;

import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class PostDetailFragment extends Fragment {
    private Post post;
    private PostViewModel viewModel;
    public static final String ARG_POST_ID = "post_id";
    private static final String TAG = "POSTS";
    private ImageView imgViewPosting;
    private TextView txtViewPosting1stLine;
    private TextView txtViewPosting2ndLine;
    private TextView txtViewPosting3rdLine;
    private ImageView imgViewPostDetailAvator;
    private TextView txtViewPostDetailInitiatorName;
    private TextView txtViewPostDetailInitiatorDescription;

    public static PostDetailFragment newInstance(){
        return new PostDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container,false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancedState){
        super.onViewCreated(view, savedInstancedState);
        viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        imgViewPosting = view.findViewById(R.id.imgViewPosting);
        txtViewPosting1stLine = view.findViewById(R.id.txtViewPosting1stLine);
        txtViewPosting2ndLine = view.findViewById(R.id.txtViewPosting2ndLine);
        txtViewPosting3rdLine = view.findViewById(R.id.txtViewPosting3rdLine);
        imgViewPostDetailAvator = view.findViewById(R.id.imgViewPostDetailAvator);
        txtViewPostDetailInitiatorName = view.findViewById(R.id.txtViewPostDetailInitiatorName);
        txtViewPostDetailInitiatorDescription = view.findViewById(R.id.txtViewPostDetailInitiatorDescription);


        if (null == getArguments() || null == this.getArguments().getString(ARG_POST_ID)){
            Log.e(TAG, "No post is passed to this fragment. Expected " + ARG_POST_ID);
            return;
        }

        Post post = viewModel.getPost(UUID.fromString(this.getArguments().getString(ARG_POST_ID)));

        if (post.getPostType() == PostType.ROOM){
            ImageFileHelper.readImage(view.getContext(), post.getRoomImage(), (bitmap) -> imgViewPosting.setImageBitmap(bitmap));
            txtViewPosting1stLine.setText(post.getLocation());
            txtViewPosting2ndLine.setText(post.getCity());
            txtViewPosting3rdLine.setText(post.getRoomDescription());

            ImageFileHelper.readImage(view.getContext(), post.getInitiatorImage(), (bitmap) -> imgViewPostDetailAvator.setImageBitmap(bitmap));
            txtViewPostDetailInitiatorName.setText(post.getInitiatorName());
            txtViewPostDetailInitiatorDescription.setText(post.getInitiatorDescription());
        } else if (post.getPostType() == PostType.PERSON){
            ImageFileHelper.readImage(view.getContext(), post.getInitiatorImage(), (bitmap) -> imgViewPosting.setImageBitmap(bitmap));
            txtViewPosting1stLine.setText(post.getInitiatorName());
            txtViewPosting2ndLine.setText(post.getInitiatorGender());
            txtViewPosting3rdLine.setText(post.getInitiatorDescription());

            TextView txtViewPostBy = view.findViewById(R.id.txtViewPostBy);
            txtViewPostBy.setVisibility(View.INVISIBLE);
            imgViewPostDetailAvator.setVisibility(View.INVISIBLE);
            txtViewPostDetailInitiatorName.setVisibility(View.INVISIBLE);
            txtViewPostDetailInitiatorDescription.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuPosting).setChecked(true);
    }
}