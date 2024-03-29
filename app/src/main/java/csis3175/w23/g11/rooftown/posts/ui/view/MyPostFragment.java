package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.databinding.FragmentMyPostBinding;
import csis3175.w23.g11.rooftown.databinding.LayoutHomebuttonBinding;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class MyPostFragment extends Fragment {
    public static final String TAG = "MY_POST";
    private FragmentMyPostBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Integer> btnTitles = new ArrayList<>(Arrays.asList(R.string.txtPostTypeButton1Title, R.string.txtPostTypeButton2Title));
        List<Integer> btnContents = new ArrayList<>(Arrays.asList(R.string.txtPostTypeButton1Content, R.string.txtPostTypeButton2Content));
        List<Integer> btnDrawables = new ArrayList<>(Arrays.asList(R.drawable.ic_outline_add_home_60, R.drawable.ic_outline_person_add_alt_60));
        List<PostType> btnPostTypes = new ArrayList<>(Arrays.asList(PostType.ROOM, PostType.PERSON));

        for (int i = 0; i < btnTitles.size(); i++) {
            LayoutHomebuttonBinding buttonBinding = LayoutHomebuttonBinding.inflate(LayoutInflater.from(view.getContext()), binding.getRoot(), false);

            buttonBinding.txtTitle.setText(btnTitles.get(i));
            buttonBinding.txtDesc.setText(btnContents.get(i));
            buttonBinding.imgViewIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), btnDrawables.get(i), null));
            CardView card = buttonBinding.cardViewHomeButton;
            if (btnPostTypes.get(i) == PostType.ROOM) {
                View.OnClickListener onClickListener = (View v) -> {
                    NewRoomPostFragment newRoomPostFragment = NewRoomPostFragment.newInstance();
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContainer, newRoomPostFragment)
                            .addToBackStack(TAG)
                            .commit();
                };
                card.setOnClickListener(onClickListener);
                buttonBinding.btnAction.setOnClickListener(onClickListener);
            } else if (btnPostTypes.get(i) == PostType.PERSON) {
                View.OnClickListener onClickListener = (View v) -> {
                    NewPersonPostFragment newPersonPostFragment = NewPersonPostFragment.newInstance();
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContainer, newPersonPostFragment)
                            .addToBackStack(TAG)
                            .commit();
                };
                card.setOnClickListener(onClickListener);
                buttonBinding.btnAction.setOnClickListener(onClickListener);
            }

            binding.linearLayoutPostType.addView(buttonBinding.getRoot());
        }
        if (getActivity() != null) {
            PostViewModel viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
            String currentUid = CurrentUserHelper.getCurrentUid();
            Post post = viewModel.getMyPost(currentUid);
            if (post != null) {
                PostDetailFragment postDetailFragment = PostDetailFragment.newInstance();
                Bundle args = new Bundle();
                args.putString(PostDetailFragment.ARG_POST_ID, post.getPostId().toString());
                postDetailFragment.setArguments(args);
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainContainer, postDetailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuPosting).setChecked(true);
    }
}