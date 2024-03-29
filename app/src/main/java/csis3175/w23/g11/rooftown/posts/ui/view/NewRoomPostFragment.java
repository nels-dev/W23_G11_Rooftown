package csis3175.w23.g11.rooftown.posts.ui.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.ui.viewmodel.UserProfileViewModel;

public class NewRoomPostFragment extends Fragment {

    private static final String TAG = "NEW_ROOM_POST";
    private PostViewModel postViewModel;
    private EditText editTextPostLocation;
    private EditText editTextPostCity;
    private Spinner spinnerPostCountry;
    private EditText editTextPostPostalCode;
    private EditText editTextPostNumOfRooms;
    private RadioGroup radGrpPostFurnished;
    private RadioGroup radGrpPostSharedBathroom;
    private EditText editTextPostRoomDescription;
    private ImageView imgViewPostRoomImage;
    private EditText editTextPostInitiatorName;
    private Spinner spinnerPostInitiatorGender;
    private EditText editTextPostInitiatorAge;
    private EditText editTextPostInitiatorDescription;
    private ImageView imgViewPostInitiatorImage;
    private String uploadPostRoomImageFileName;
    private ActivityResultLauncher<Intent> activityResultLauncherRoomImage;
    private String uploadPostInitiatorImageFileName;
    private ActivityResultLauncher<Intent> activityResultLauncherInitiatorImage;

    public static NewRoomPostFragment newInstance() {
        return new NewRoomPostFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_room_post, container, false);
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
        editTextPostPostalCode = view.findViewById(R.id.editTextPostPostalCode);
        editTextPostNumOfRooms = view.findViewById(R.id.editTextPostNumOfRooms);
        radGrpPostFurnished = view.findViewById(R.id.radGrpPostFurnished);
        radGrpPostSharedBathroom = view.findViewById(R.id.radGrpPostSharedBathroom);
        editTextPostRoomDescription = view.findViewById(R.id.editTextPostRoomDescription);
        imgViewPostRoomImage = view.findViewById(R.id.imgViewPostRoomImage);
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
        } else {
            spinnerPostCountry.setSelection(countryAdapter.getPosition("Canada"));
        }

        activityResultLauncherRoomImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::roomImageChosen);
        imgViewPostRoomImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherRoomImage.launch(intent);
        });
        activityResultLauncherInitiatorImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::initiatorImageChosen);
        imgViewPostInitiatorImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherInitiatorImage.launch(intent);
        });

        btnSavePost.setOnClickListener((v) -> createPost(savedInstanceState));
    }

    private void roomImageChosen(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
            Uri imageUri = result.getData().getData();
            ImageFileHelper.saveImage(this.getContext(), imageUri, uploadedImage -> {
                this.uploadPostRoomImageFileName = uploadedImage.first;
                imgViewPostRoomImage.setImageBitmap(uploadedImage.second);
            });
        }
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
        newPost.setPostType(PostType.ROOM);
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
        String country = spinnerPostCountry.getSelectedItem().toString();
        newPost.setCountry(country);
        if (editTextPostPostalCode.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter postal code", Toast.LENGTH_SHORT).show();
            return;
        }
        String postalCode = editTextPostPostalCode.getText().toString();
        newPost.setPostalCode(editTextPostPostalCode.getText().toString());
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(postalCode + ", " + country, 1);
            if (addresses != null && addresses.size() > 0) {
                newPost.setLatLong(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()));
                newPost.setGeohash(GeoFireUtils.getGeoHashForLocation(new GeoLocation(addresses.get(0).getLatitude(), addresses.get(0).getLongitude())));
            } else {
                Toast.makeText(this.getContext(), "Invalid postal code", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (editTextPostNumOfRooms.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter number of rooms", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setNumOfRooms(editTextPostNumOfRooms.getText().toString());
        if (radGrpPostFurnished.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this.getContext(), "Please choose whether is furnished or not", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setFurnished(radGrpPostFurnished.getCheckedRadioButtonId() == R.id.radBtnPostFurnishedYes);
        if (radGrpPostSharedBathroom.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this.getContext(), "Please choose whether is shared bathroom or not", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setSharedBathroom(radGrpPostSharedBathroom.getCheckedRadioButtonId() == R.id.radBtnPostSharedBathroomYes);
        if (editTextPostRoomDescription.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter the room description", Toast.LENGTH_SHORT).show();
            return;
        }
        newPost.setRoomDescription(editTextPostRoomDescription.getText().toString());
        if (uploadPostRoomImageFileName != null) {
            newPost.setRoomImage(uploadPostRoomImageFileName);
        }
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
            Toast.makeText(NewRoomPostFragment.this.getContext(), "Post created", Toast.LENGTH_SHORT).show();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContainer, MyPostFragment.class, savedInstanceState)
                    .commit();
        });
    }
}