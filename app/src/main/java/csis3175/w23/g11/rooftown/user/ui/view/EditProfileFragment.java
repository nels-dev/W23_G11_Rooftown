package csis3175.w23.g11.rooftown.user.ui.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.messages.ui.view.ConversationFragment;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.ui.viewmodel.UserProfileViewModel;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EDIT_PROFILE";
    private UserProfileViewModel viewModel;
    private EditText editTextEmail;
    private TextView editTextDisplayName;
    private TextView editTextCity;
    private Spinner spinnerCountry;
    private TextView editTextPassword;
    private Button btnSaveProfile;
    private ImageView imgViewEditProfileImage;
    private String randomPassword;
    private String uploadFileName;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextDisplayName = view.findViewById(R.id.editTextDisplayName);
        editTextCity = view.findViewById(R.id.editTextCity);
        spinnerCountry = view.findViewById(R.id.spinnerCountry);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        imgViewEditProfileImage = view.findViewById(R.id.imgViewEditProfileImage);
        randomPassword = String.valueOf(new Random().nextInt(9999999) + 10000000);

        String[] countries = view.getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, countries);
        spinnerCountry.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.setUserId(CurrentUserHelper.getCurrentUid());
        viewModel.getUserProfile().observe(this.getViewLifecycleOwner(), (profile) -> {
            editTextPassword.setText(randomPassword);
            editTextEmail.setText(CurrentUserHelper.getCurrentUserEmail());
            editTextDisplayName.setText(profile.getUserName());
            editTextCity.setText(profile.getCity());
            if (null != profile.getCountry()) {
                spinnerCountry.setSelection(adapter.getPosition(profile.getCountry()));
            } else {
                spinnerCountry.setSelection(adapter.getPosition("Canada"));
            }

            if (null != profile.getImageFileName()) {
                ImageFileHelper.readImage(view.getContext(), profile.getImageFileName(),
                        (bitmap) -> imgViewEditProfileImage.setImageBitmap(bitmap));
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::imageChosen);
        imgViewEditProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        });
        btnSaveProfile.setOnClickListener((v) -> saveProfile(savedInstanceState));
        viewModel.loadData();
    }

    private void imageChosen(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
            Uri imageUri = result.getData().getData();
            ImageFileHelper.saveImage(this.getContext(), imageUri, uploadedImage -> {
                this.uploadFileName = uploadedImage.first;
                imgViewEditProfileImage.setImageBitmap(uploadedImage.second);
            });
        }
    }

    private void saveProfile(@Nullable Bundle savedInstanceState) {
        UserProfile p = viewModel.getUserProfile().getValue();
        p.setCity(editTextCity.getText().toString());
        p.setCountry(spinnerCountry.getSelectedItem().toString());
        p.setUserName(editTextDisplayName.getText().toString());
        if (null != uploadFileName) {
            //Image updated by user
            p.setImageFileName(uploadFileName);
        }
        if (!randomPassword.equals(editTextPassword.getText().toString())) {
            //Password changed by user
            //TODO: update password
        }
        viewModel.updateUserProfile(p, (unused) -> {
            Toast.makeText(EditProfileFragment.this.getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, ProfileFragment.class, savedInstanceState)
                    .commit();
        });
    }
}