package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.messages.ui.view.ConversationFragment;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class PostDetailFragment extends Fragment {
    public static final String ARG_POST_ID = "post_id";
    private static final String TAG = "POSTS";
    private PostViewModel viewModel;
    private ImageView imgViewPosting;
    private ImageView imgViewPostDetailAvatar;
    private Post post;

    public static PostDetailFragment newInstance() {
        return new PostDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancedState) {
        super.onViewCreated(view, savedInstancedState);
        if (getActivity() != null) {
            viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        }
        imgViewPosting = view.findViewById(R.id.imgViewPosting);
        TextView txtViewPosting1stLine = view.findViewById(R.id.txtViewPosting1stLine);
        TextView txtViewPosting2ndLine = view.findViewById(R.id.txtViewPosting2ndLine);
        TextView txtViewPosting3rdLine = view.findViewById(R.id.txtViewPosting3rdLine);
        LinearLayout linearLayoutInitiator = view.findViewById(R.id.linearLayoutInitiator);
        imgViewPostDetailAvatar = view.findViewById(R.id.imgViewPostDetailAvatar);
        TextView txtViewPostDetailInitiatorName = view.findViewById(R.id.txtViewPostDetailInitiatorName);
        TextView txtViewPostDetailInitiatorBrief = view.findViewById(R.id.txtViewPostDetailInitiatorBrief);
        TextView txtViewPostDetailInitiatorDescription = view.findViewById(R.id.txtViewPostDetailInitiatorDescription);
        FloatingActionButton btnEditPost = view.findViewById(R.id.btnEditPost);
        Button btnPostingInterested = view.findViewById(R.id.btnPostingInterested);

        if (null == getArguments() || null == this.getArguments().getString(ARG_POST_ID)) {
            Log.e(TAG, "No post is passed to this fragment. Expected " + ARG_POST_ID);
            return;
        }

        post = viewModel.getPost(UUID.fromString(this.getArguments().getString(ARG_POST_ID)));
        String currentUid = CurrentUserHelper.getCurrentUid();
        if (post.getInitiator().equals(currentUid)) {
            btnPostingInterested.setVisibility(View.GONE);
        } else {
            btnEditPost.setVisibility(View.GONE);
        }

        btnPostingInterested.setOnClickListener(this::interestInClicked);

        String initiatorBrief = "";
        if (post.getInitiatorGender() != null && (post.getInitiatorGender().equals("Male") || post.getInitiatorGender().equals("Female"))) {
            initiatorBrief += post.getInitiatorGender();
        }
        if (post.getInitiatorAge() != null && post.getInitiatorAge().isEmpty()) {
            initiatorBrief += (initiatorBrief.isEmpty() ? "" : ", ") + post.getInitiatorAge();
        }

        if (post.getPostType() == PostType.ROOM) {
            if (post.getRoomImage() != null) {
                ImageFileHelper.readImage(view.getContext(), post.getRoomImage(), (bitmap) -> imgViewPosting.setImageBitmap(bitmap));
            } else {
                imgViewPosting.setImageResource(R.drawable.placeholder_room);
            }
            txtViewPosting1stLine.setText(post.getLocation());
            txtViewPosting2ndLine.setText(String.format("%s, %s", post.getCity(), post.getCountry()));
            txtViewPosting3rdLine.setText(String.format("%s, %s\n%s", post.isFurnished() ? "Furnished" : "Unfurnished", post.isSharedBathroom() ? "Shared Bathroom" : "Private Bathroom", post.getRoomDescription()));

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
            txtViewPostDetailInitiatorBrief.setText(initiatorBrief);
            txtViewPostDetailInitiatorDescription.setText(post.getInitiatorDescription());
            btnEditPost.setOnClickListener((v) -> {
                EditRoomPostFragment editRoomPostFragment = EditRoomPostFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(EditRoomPostFragment.ARG_POST_ID, this.getArguments().getString(ARG_POST_ID));
                editRoomPostFragment.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.mainContainer, editRoomPostFragment).addToBackStack(TAG) // so that pressing "Back" button on android goes back to this fragment
                        .commit();
            });
        } else if (post.getPostType() == PostType.PERSON) {
            String initiatorDetail = "";
            if (!initiatorBrief.isEmpty()) {
                initiatorDetail += initiatorBrief;
            }
            if (post.getInitiatorDescription() != null && !post.getInitiatorDescription().isEmpty()) {
                initiatorDetail += (initiatorDetail.isEmpty() ? "" : "\n") + post.getInitiatorDescription();
            }
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
            txtViewPosting2ndLine.setText(String.format("%s, %s", post.getLocation(), post.getCity()));
            txtViewPosting3rdLine.setText(initiatorDetail);

            TextView txtViewPostBy = view.findViewById(R.id.txtViewPostBy);
            txtViewPostBy.setVisibility(View.GONE);
            linearLayoutInitiator.setVisibility(View.GONE);
            imgViewPostDetailAvatar.setVisibility(View.GONE);
            txtViewPostDetailInitiatorName.setVisibility(View.GONE);
            txtViewPostDetailInitiatorBrief.setVisibility(View.GONE);
            txtViewPostDetailInitiatorDescription.setVisibility(View.GONE);
            btnEditPost.setOnClickListener((v) -> {
                EditPersonPostFragment editPersonPostFragment = EditPersonPostFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(EditPersonPostFragment.ARG_POST_ID, this.getArguments().getString(ARG_POST_ID));
                editPersonPostFragment.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.mainContainer, editPersonPostFragment).addToBackStack(TAG) // so that pressing "Back" button on android goes back to this fragment
                        .commit();
            });
        }
    }

    private void interestInClicked(View view) {
        if (viewModel == null) return;
        viewModel.showInterest(post.getInitiator(), post.getPostId(), chatId -> {
            ConversationFragment conversationFragment = ConversationFragment.newInstance();
            Bundle args = new Bundle();
            args.putString(ConversationFragment.ARG_CHAT_ID, chatId.toString());
            conversationFragment.setArguments(args);

            FragmentTransaction tx = getParentFragmentManager().beginTransaction();
            tx.replace(R.id.mainContainer, conversationFragment);
            tx.addToBackStack(TAG);
            tx.commit();
        });
    }
}