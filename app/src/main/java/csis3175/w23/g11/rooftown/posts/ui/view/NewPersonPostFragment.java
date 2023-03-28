package csis3175.w23.g11.rooftown.posts.ui.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Date;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.ui.viewmodel.UserProfileViewModel;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class NewPersonPostFragment extends Fragment {

    private static final String TAG = "NEW_ROOM_POST";
    private PostViewModel postViewModel;
    private EditText editTextPostLocation;
    private EditText editTextPostCity;
    private Spinner spinnerPostCountry;
    private EditText editTextPostInitiatorName;
    private Spinner spinnerPostInitiatorGender;
    private EditText editTextPostInitiatorAge;
    private EditText editTextPostInitiatorDescription;
    private ImageView imgViewPostInitiatorImage;
    private String uploadPostInitiatorImageFileName;
    private ActivityResultLauncher<Intent> activityResultLauncherInitiatorImage;

    public static NewPersonPostFragment newInstance() {
        return new NewPersonPostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_person_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        UserProfileViewModel userProfileViewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        userProfileViewModel.setUserId(CurrentUserHelper.getCurrentUid());
        userProfileViewModel.loadData();

        editTextPostLocation = view.findViewById(R.id.editTextPostLocation);
        editTextPostCity = view.findViewById(R.id.editTextPostCity);
        spinnerPostCountry = view.findViewById(R.id.spinnerPostCountry);
        editTextPostInitiatorName = view.findViewById(R.id.editTextPostInitiatorName);
        spinnerPostInitiatorGender = view.findViewById(R.id.spinnerPostInitiatorGender);
        editTextPostInitiatorAge = view.findViewById(R.id.editTextPostInitiatorAge);
        editTextPostInitiatorDescription = view.findViewById(R.id.editTextPostInitiatorDescription);
        imgViewPostInitiatorImage = view.findViewById(R.id.imgViewPostInitiatorImage);
        Button btnSavePost = view.findViewById(R.id.btnSavePost);

        String[] countries = view.getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, countries);
        spinnerPostCountry.setAdapter(countryAdapter);
        String[] genders = view.getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, genders);
        spinnerPostInitiatorGender.setAdapter(genderAdapter);
        spinnerPostInitiatorGender.setSelection(Arrays.asList(genders).indexOf("Prefer not to disclose"));
        UserProfile profile = userProfileViewModel.getUserProfile().getValue();
        if (profile != null) {
            editTextPostInitiatorName.setText(profile.getUserName());
            editTextPostCity.setText(profile.getCity());
            spinnerPostCountry.setSelection(countryAdapter.getPosition((profile.getCountry() != null) ? profile.getCountry() : "Canada"));
            if (profile.getImageFileName() != null) {
                this.uploadPostInitiatorImageFileName = profile.getImageFileName();
                ImageFileHelper.readImage(view.getContext(), profile.getImageFileName(),
                        (bitmap) -> imgViewPostInitiatorImage.setImageBitmap(bitmap));
            }
        }

        activityResultLauncherInitiatorImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::initiatorImageChosen);
        imgViewPostInitiatorImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherInitiatorImage.launch(intent);
        });

        btnSavePost.setOnClickListener((v) -> createPost(savedInstanceState));
    }

    private void initiatorImageChosen(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
            Uri imageUri = result.getData().getData();
            ImageFileHelper.saveImage(this.getContext(), imageUri, uploadedImage -> {
                this.uploadPostInitiatorImageFileName = uploadedImage.first;
                imgViewPostInitiatorImage.setImageBitmap(uploadedImage.second);
            });
        }
    }

    private void createPost(@Nullable Bundle savedInstanceState) {
        Post newPost = new Post();
        newPost.setPostType(PostType.PERSON);
        if (editTextPostLocation.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter location", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setLocation(editTextPostLocation.getText().toString());
        if (editTextPostCity.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter city", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setCity(editTextPostCity.getText().toString());
        newPost.setCountry(spinnerPostCountry.getSelectedItem().toString());
        newPost.setLatLong(new LatLng(Math.random() / 5.0 + 49.1, Math.random() / 2.5 - 123.2));
        newPost.setInitiator(CurrentUserHelper.getCurrentUid());
        if (editTextPostInitiatorName.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter display name", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setInitiatorName(editTextPostInitiatorName.getText().toString());
        newPost.setInitiatorGender(spinnerPostInitiatorGender.getSelectedItem().toString());
        newPost.setInitiatorAge(editTextPostInitiatorAge.getText().toString());
        if (editTextPostInitiatorDescription.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter your description", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setInitiatorDescription(editTextPostInitiatorDescription.getText().toString());
        if (uploadPostInitiatorImageFileName != null) {
            newPost.setInitiatorImage(uploadPostInitiatorImageFileName);
        }
        newPost.setPostStatus(PostStatus.OPEN);
        newPost.setPostAt(new Date());
        postViewModel.createPost(newPost, (unused) -> {
            Toast.makeText(NewPersonPostFragment.this.getContext(), "Post created", Toast.LENGTH_SHORT).show();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, MyPostFragment.class, savedInstanceState)
                    .commit();
        });
    }
}