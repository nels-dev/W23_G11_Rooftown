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
        List<Fragment> btnTargetFragments = new ArrayList<>(Arrays.asList(new RoommatesFragment(), new MyPostFragment(), new AllChatsFragment()));

        for (int i = 0; i < btnTitles.size(); i++) {
            int pos = i;
            LayoutHomebuttonBinding buttonBinding = LayoutHomebuttonBinding.inflate(inflater, container, false);
            buttonBinding.txtViewHomeButtonTitle.setText(btnTitles.get(pos));
            buttonBinding.txtViewHomeButtonContent.setText(btnContents.get(pos));
            buttonBinding.txtViewHomeButtonContent.setCompoundDrawablesWithIntrinsicBounds(null,
                    ResourcesCompat.getDrawable(getResources(), btnDrawables.get(pos), null),
                    null,
                    null);
            buttonBinding.cardViewHomeButton.setOnClickListener((View v) -> requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, btnTargetFragments.get(pos))
                    .addToBackStack(TAG)
                    .commit());
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