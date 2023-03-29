package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class PostDetailFragment extends Fragment {
    private PostViewModel viewModel;
    public static final String ARG_POST_ID = "post_id";
    private static final String TAG = "POSTS";
    private ImageView imgViewPosting;
    private TextView txtViewPosting1stLine;
    private TextView txtViewPosting2ndLine;
    private TextView txtViewPosting3rdLine;
    private ImageView imgViewPostDetailAvatar;
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
        imgViewPostDetailAvatar = view.findViewById(R.id.imgViewPostDetailAvatar);
        txtViewPostDetailInitiatorName = view.findViewById(R.id.txtViewPostDetailInitiatorName);
        txtViewPostDetailInitiatorDescription = view.findViewById(R.id.txtViewPostDetailInitiatorDescription);
        FloatingActionButton btnEditPost = view.findViewById(R.id.btnEditPost);
        Button btnPostingInterested = view.findViewById(R.id.btnPostingInterested);

        if (null == getArguments() || null == this.getArguments().getString(ARG_POST_ID)){
            Log.e(TAG, "No post is passed to this fragment. Expected " + ARG_POST_ID);
            return;
        }

        Post post = viewModel.getPost(UUID.fromString(this.getArguments().getString(ARG_POST_ID)));
        String currentUid = CurrentUserHelper.getCurrentUid();
        if (post.getInitiator().equals(currentUid)) {
            btnPostingInterested.setVisibility(View.GONE);
        } else {
            btnEditPost.setVisibility(View.GONE);
        }

        if (post.getPostType() == PostType.ROOM) {
            if (post.getRoomImage() != null) {
                ImageFileHelper.readImage(view.getContext(), post.getRoomImage(), (bitmap) -> imgViewPosting.setImageBitmap(bitmap));
            } else {
                imgViewPosting.setImageResource(R.drawable.placeholder_room);
            }
            txtViewPosting1stLine.setText(post.getLocation());
            txtViewPosting2ndLine.setText(post.getCity());
            txtViewPosting3rdLine.setText(post.getRoomDescription());

            if (post.getInitiatorImage() != null) {
                ImageFileHelper.readImage(view.getContext(), post.getInitiatorImage(), (bitmap) -> imgViewPostDetailAvatar.setImageBitmap(bitmap));
            } else if (post.getInitiatorGender().equals("Male")) {
                imgViewPostDetailAvatar.setImageResource(R.drawable.placeholder_person_male);
            } else if (post.getInitiatorGender().equals("Female")) {
                imgViewPostDetailAvatar.setImageResource(R.drawable.placeholder_person_female);
            } else {
                imgViewPostDetailAvatar.setImageResource(R.drawable.placeholder_person_general);
            }
            txtViewPostDetailInitiatorName.setText(post.getInitiatorName());
            txtViewPostDetailInitiatorDescription.setText(post.getInitiatorDescription());
            btnEditPost.setOnClickListener((v) -> {
                EditRoomPostFragment editRoomPostFragment = EditRoomPostFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(EditRoomPostFragment.ARG_POST_ID, this.getArguments().getString(ARG_POST_ID));
                editRoomPostFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, editRoomPostFragment)
                        .addToBackStack(TAG) // so that pressing "Back" button on android goes back to this fragment
                        .commit();
            });
        } else if (post.getPostType() == PostType.PERSON) {
            if (post.getInitiatorImage() != null) {
                ImageFileHelper.readImage(view.getContext(), post.getInitiatorImage(), (bitmap) -> imgViewPosting.setImageBitmap(bitmap));
            } else if (post.getInitiatorGender().equals("Male")) {
                imgViewPosting.setImageResource(R.drawable.placeholder_person_male);
            } else if (post.getInitiatorGender().equals("Female")) {
                imgViewPosting.setImageResource(R.drawable.placeholder_person_female);
            } else {
                imgViewPosting.setImageResource(R.drawable.placeholder_person_general);
            }
            txtViewPosting1stLine.setText(post.getInitiatorName());
            txtViewPosting2ndLine.setText(post.getInitiatorGender());
            txtViewPosting3rdLine.setText(post.getInitiatorDescription());

            TextView txtViewPostBy = view.findViewById(R.id.txtViewPostBy);
            txtViewPostBy.setVisibility(View.INVISIBLE);
            imgViewPostDetailAvatar.setVisibility(View.INVISIBLE);
            txtViewPostDetailInitiatorName.setVisibility(View.INVISIBLE);
            txtViewPostDetailInitiatorDescription.setVisibility(View.INVISIBLE);
            btnEditPost.setOnClickListener((v) -> {
                EditPersonPostFragment editPersonPostFragment = EditPersonPostFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(EditPersonPostFragment.ARG_POST_ID, this.getArguments().getString(ARG_POST_ID));
                editPersonPostFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, editPersonPostFragment)
                        .addToBackStack(TAG) // so that pressing "Back" button on android goes back to this fragment
                        .commit();
            });
        }
    }
}