package csis3175.w23.g11.rooftown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.databinding.FragmentHomeBinding;
import csis3175.w23.g11.rooftown.databinding.LayoutHomebuttonBinding;
import csis3175.w23.g11.rooftown.messages.ui.view.AllChatsFragment;
import csis3175.w23.g11.rooftown.posts.ui.view.MyPostFragment;
import csis3175.w23.g11.rooftown.posts.ui.view.RoommatesFragment;


public class HomeFragment extends Fragment {
    private static final String TAG = "HOME";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        List<Integer> btnTitles = new ArrayList<>(Arrays.asList(R.string.txtHomeButton1Title, R.string.txtHomeButton2Title, R.string.txtHomeButton3Title));
        List<Integer> btnContents = new ArrayList<>(Arrays.asList(R.string.txtHomeButton1Content, R.string.txtHomeButton2Content, R.string.txtHomeButton3Content));
        List<Integer> btnDrawables = new ArrayList<>(Arrays.asList(R.drawable.ic_outline_search_60, R.drawable.ic_outline_add_box_60, R.drawable.ic_outline_question_answer_60));
        List<Integer> backgrounds = new ArrayList<>(Arrays.asList(R.drawable.roommates, R.drawable.room, R.drawable.chat));
        List<Integer> btnTexts = new ArrayList<>(Arrays.asList(R.string.bottomNavRoommatesButtonTitle, R.string.bottomNavPostingButtonTitle, R.string.bottomNavMessagesButtonTitle));
        List<Fragment> btnTargetFragments = new ArrayList<>(Arrays.asList(new RoommatesFragment(), new MyPostFragment(), new AllChatsFragment()));

        for (int i = 0; i < btnTitles.size(); i++) {
            int pos = i;
            LayoutHomebuttonBinding buttonBinding = LayoutHomebuttonBinding.inflate(inflater, container, false);
            buttonBinding.txtTitle.setText(btnTitles.get(pos));
            buttonBinding.txtDesc.setText(btnContents.get(pos));
            buttonBinding.imgViewIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), btnDrawables.get(pos), null));
            buttonBinding.imgViewCardBackground.setImageDrawable(ResourcesCompat.getDrawable(getResources(), backgrounds.get(pos), null));
            buttonBinding.btnAction.setText(btnTexts.get(pos));
            View.OnClickListener onClickListener = (View v) -> requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, btnTargetFragments.get(pos))
                    .addToBackStack(TAG)
                    .commit();
            buttonBinding.cardViewHomeButton.setOnClickListener(onClickListener);
            buttonBinding.btnAction.setOnClickListener(onClickListener);
            binding.linearLayoutHome.addView(buttonBinding.getRoot());
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuHome).setChecked(true);
    }
}