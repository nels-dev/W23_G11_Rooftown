package csis3175.w23.g11.rooftown.posts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import csis3175.w23.g11.rooftown.MainActivity;
import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class RoommatesFragment extends Fragment {
    private TabLayout roommatesTabs;
    private GridViewFragment gridViewFragment;
    private ListViewFragment listViewFragment;
    private MapViewFragment mapViewFragment;
    private PostViewModel viewModel;
    int currTabPos = 0;

    public PostViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roommates, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        viewModel.loadData();
        gridViewFragment = new GridViewFragment();
        listViewFragment = new ListViewFragment();
        mapViewFragment = new MapViewFragment();
        roommatesTabs = view.findViewById(R.id.roommatesTabs);
        TabLayout.Tab currTab = roommatesTabs.getTabAt(currTabPos);
        if (currTab != null) {
            currTab.select();
        }
        switch (currTabPos) {
            case 1:
                getChildFragmentManager().beginTransaction().replace(R.id.roommatesContainer, listViewFragment).commit();
                break;
            case 2:
                getChildFragmentManager().beginTransaction().replace(R.id.roommatesContainer, mapViewFragment).commit();
                break;
            default:
                getChildFragmentManager().beginTransaction().replace(R.id.roommatesContainer, gridViewFragment).commit();
                break;
        }

        roommatesTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        currTabPos = 1;
                        getChildFragmentManager().beginTransaction().replace(R.id.roommatesContainer, listViewFragment).commit();
                        break;
                    case 2:
                        currTabPos = 2;
                        getChildFragmentManager().beginTransaction().replace(R.id.roommatesContainer, mapViewFragment).commit();
                        ((MainActivity) requireActivity()).switchToMapViewFragment();
                        break;
                    default:
                        currTabPos = 0;
                        getChildFragmentManager().beginTransaction().replace(R.id.roommatesContainer, gridViewFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuRoommates).setChecked(true);
    }
}