package csis3175.w23.g11.rooftown;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.messages.ui.view.AllChatsFragment;
import csis3175.w23.g11.rooftown.posts.ui.view.RoommatesFragment;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout linearLayoutHome = view.findViewById(R.id.linearLayoutHome);
        List<Integer> btnTitles = new ArrayList<>(Arrays.asList(R.string.txtHomeButton1Title, R.string.txtHomeButton2Title, R.string.txtHomeButton3Title));
        List<Integer> btnContents = new ArrayList<>(Arrays.asList(R.string.txtHomeButton1Content, R.string.txtHomeButton2Content, R.string.txtHomeButton3Content));
        List<Integer> btnDrawables = new ArrayList<>(Arrays.asList(R.drawable.ic_outline_search_60, R.drawable.ic_outline_add_box_60, R.drawable.ic_outline_question_answer_60));
        List<Fragment> btnTargetFragments = new ArrayList<>(Arrays.asList(new RoommatesFragment(), new PostingFragment(), new AllChatsFragment()));

        for (int i = 0; i < btnTitles.size(); i++) {
            int pos = i;
            View button = inflater.inflate(R.layout.layout_homebutton, container, false);
            TextView buttonTitle = button.findViewById(R.id.txtViewHomeButtonTitle);
            buttonTitle.setText(btnTitles.get(pos));
            TextView buttonContent = button.findViewById(R.id.txtViewHomeButtonContent);
            buttonContent.setText(btnContents.get(pos));
            buttonContent.setCompoundDrawablesWithIntrinsicBounds(null,
                    ResourcesCompat.getDrawable(getResources(), btnDrawables.get(pos), null ),
                    null,
                    null);
            CardView card = button.findViewById(R.id.cardViewHomeButton);
            card.setOnClickListener((View v) -> requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, btnTargetFragments.get(pos))
                    .commit());
            linearLayoutHome.addView(button);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuHome).setChecked(true);
    }
}