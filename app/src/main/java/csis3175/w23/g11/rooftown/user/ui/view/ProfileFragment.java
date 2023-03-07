package csis3175.w23.g11.rooftown.user.ui.view;

import android.os.Bundle;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.user.ui.viewmodel.UserProfileViewModel;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class ProfileFragment extends Fragment {

    private static final String TAG = "USER_PROFILE";
    private UserProfileViewModel viewModel;
    private TextView txtViewEmail;
    private TextView txtViewDisplayName;
    private TextView txtViewCity;
    private TextView txtViewCountry;
    private TextView txtViewPassword;
    private ImageView imgViewProfileImage;
    private Button btnSignOut;
    private FloatingActionButton btnEditProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuProfile).setChecked(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtViewEmail = view.findViewById(R.id.txtViewEmail);
        txtViewDisplayName = view.findViewById(R.id.txtViewDisplayName);
        txtViewCity = view.findViewById(R.id.txtViewCity);
        txtViewCountry = view.findViewById(R.id.txtViewCountry);
        txtViewPassword = view.findViewById(R.id.txtViewPassword);
        imgViewProfileImage = view.findViewById(R.id.imgViewProfileImage);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.setUserId(CurrentUserHelper.getCurrentUid());
        viewModel.getUserProfile().observe(this.getViewLifecycleOwner(), (profile) -> {

            // No real password is being written/fetched, only a dummy one
            txtViewPassword.setText(String.valueOf(new Random().nextInt(9999999) + 10000000));

            txtViewEmail.setText(CurrentUserHelper.getCurrentUserEmail());
            txtViewDisplayName.setText(profile.getUserName());
            txtViewCity.setText(profile.getCity());
            txtViewCountry.setText(profile.getCountry());
            if (null != profile.getImageFileName()) {
                ImageFileHelper.readImage(view.getContext(), profile.getImageFileName(),
                        (bitmap) -> imgViewProfileImage.setImageBitmap(bitmap));
            }
        });
        btnSignOut.setOnClickListener((v) -> FirebaseAuth.getInstance().signOut());
        btnEditProfile.setOnClickListener((v) -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, EditProfileFragment.class, savedInstanceState)
                    .addToBackStack(TAG) // so that pressing "Back" button on android goes back to this fragment
                    .commit();
        });
        viewModel.loadData();
    }
}