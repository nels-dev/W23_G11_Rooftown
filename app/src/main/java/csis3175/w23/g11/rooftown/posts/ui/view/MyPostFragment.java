package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;

public class MyPostFragment extends Fragment {
    private static final String TAG = "MY_POST";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_post, container, false);
        LinearLayout linearLayoutPostType = view.findViewById(R.id.linearLayoutPostType);
        List<Integer> btnTitles = new ArrayList<>(Arrays.asList(R.string.txtPostTypeButton1Title, R.string.txtPostTypeButton2Title));
        List<Integer> btnContents = new ArrayList<>(Arrays.asList(R.string.txtPostTypeButton1Content, R.string.txtPostTypeButton2Content));
        List<Integer> btnDrawables = new ArrayList<>(Arrays.asList(R.drawable.ic_outline_add_home_60, R.drawable.ic_outline_person_add_alt_60));
        List<PostType> btnPostTypes = new ArrayList<>(Arrays.asList(PostType.ROOM, PostType.PERSON));

        for (int i = 0; i < btnTitles.size(); i++) {
            View button = inflater.inflate(R.layout.layout_homebutton, container, false);
            TextView buttonTitle = button.findViewById(R.id.txtViewHomeButtonTitle);
            buttonTitle.setText(btnTitles.get(i));
            TextView buttonContent = button.findViewById(R.id.txtViewHomeButtonContent);
            buttonContent.setText(btnContents.get(i));
            buttonContent.setCompoundDrawablesWithIntrinsicBounds(null,
                    ResourcesCompat.getDrawable(getResources(), btnDrawables.get(i), null),
                    null,
                    null);
            CardView card = button.findViewById(R.id.cardViewHomeButton);
            if (btnPostTypes.get(i) == PostType.ROOM) {
                card.setOnClickListener((View v) -> {
                    NewRoomPostFragment newRoomPostFragment = NewRoomPostFragment.newInstance();
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContainer, newRoomPostFragment)
                            .addToBackStack(TAG)
                            .commit();
                });
            } else if (btnPostTypes.get(i) == PostType.PERSON) {
                card.setOnClickListener((View v) -> {
                    NewRoomPostFragment newRoomPostFragment = NewRoomPostFragment.newInstance();
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContainer, newRoomPostFragment)
                            .addToBackStack(TAG)
                            .commit();
                });
            }

            linearLayoutPostType.addView(button);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuPosting).setChecked(true);
    }
}