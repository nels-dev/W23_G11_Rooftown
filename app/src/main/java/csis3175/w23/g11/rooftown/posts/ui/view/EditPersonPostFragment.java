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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.posts.ui.viewmodel.PostViewModel;

public class EditPersonPostFragment extends Fragment {
    public static final String ARG_POST_ID = "post_id";
    private static final String TAG = "EDIT_PERSON_POST";
    private PostViewModel postViewModel;
    private Post post;
    private EditText editTextPostLocation;
    private EditText editTextPostCity;
    private Spinner spinnerPostCountry;
    private EditText editTextPostPostalCode;
    private EditText editTextPostInitiatorName;
    private Spinner spinnerPostInitiatorGender;
    private EditText editTextPostInitiatorAge;
    private EditText editTextPostInitiatorDescription;
    private ImageView imgViewPostInitiatorImage;
    private String uploadPostInitiatorImageFileName;
    private ActivityResultLauncher<Intent> activityResultLauncherInitiatorImage;

    public static EditPersonPostFragment newInstance() {
        return new EditPersonPostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_person_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postViewModel = new ViewModelProvider(getActivity()).get(PostViewModel.class);
        post = postViewModel.getPost(UUID.fromString(this.getArguments().getString(ARG_POST_ID)));

        editTextPostLocation = view.findViewById(R.id.editTextPostLocation);
        editTextPostCity = view.findViewById(R.id.editTextPostCity);
        spinnerPostCountry = view.findViewById(R.id.spinnerPostCountry);
        editTextPostPostalCode = view.findViewById(R.id.editTextPostPostalCode);
        editTextPostInitiatorName = view.findViewById(R.id.editTextPostInitiatorName);
        spinnerPostInitiatorGender = view.findViewById(R.id.spinnerPostInitiatorGender);
        editTextPostInitiatorAge = view.findViewById(R.id.editTextPostInitiatorAge);
        editTextPostInitiatorDescription = view.findViewById(R.id.editTextPostInitiatorDescription);
        imgViewPostInitiatorImage = view.findViewById(R.id.imgViewPostInitiatorImage);
        Button btnSavePost = view.findViewById(R.id.btnSavePost);
        btnSavePost.setText(R.string.txtSavePost);
        Button btnCancelPost = view.findViewById(R.id.btnCancelPost);
        btnCancelPost.setVisibility(View.VISIBLE);

        String[] countries = view.getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, countries);
        spinnerPostCountry.setAdapter(countryAdapter);
        String[] genders = view.getResources().getStringArray(R.array.gender);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, genders);
        spinnerPostInitiatorGender.setAdapter(genderAdapter);

        editTextPostLocation.setText(post.getLocation());
        editTextPostCity.setText(post.getCity());
        spinnerPostCountry.setSelection(countryAdapter.getPosition((post.getCountry() != null) ? post.getCountry() : "Canada"));
        editTextPostPostalCode.setText(post.getPostalCode());
        editTextPostInitiatorName.setText(post.getInitiatorName());
        spinnerPostInitiatorGender.setSelection(genderAdapter.getPosition(post.getInitiatorGender()));
        editTextPostInitiatorAge.setText(post.getInitiatorAge());
        editTextPostInitiatorDescription.setText(post.getInitiatorDescription());
        if (post.getInitiatorImage() != null) {
            this.uploadPostInitiatorImageFileName = post.getInitiatorImage();
            ImageFileHelper.readImage(view.getContext(), post.getInitiatorImage(), (bitmap) -> imgViewPostInitiatorImage.setImageBitmap(bitmap));
        }

        activityResultLauncherInitiatorImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::initiatorImageChosen);
        imgViewPostInitiatorImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherInitiatorImage.launch(intent);
        });

        btnSavePost.setOnClickListener((v) -> updatePost(savedInstanceState));
        btnCancelPost.setOnClickListener((v) -> cancelPost(savedInstanceState));
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

    private void updatePost(@Nullable Bundle savedInstanceState) {
        if (editTextPostLocation.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter location", Toast.LENGTH_SHORT).show();
            return;
        }
        post.setLocation(editTextPostLocation.getText().toString());
        if (editTextPostCity.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter city", Toast.LENGTH_SHORT).show();
            return;
        }
        post.setCity(editTextPostCity.getText().toString());
        String country = spinnerPostCountry.getSelectedItem().toString();
        post.setCountry(country);
        if (editTextPostPostalCode.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter postal code", Toast.LENGTH_SHORT).show();
            return;
        }
        String postalCode = editTextPostPostalCode.getText().toString();
        post.setPostalCode(editTextPostPostalCode.getText().toString());
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(postalCode + ", " + country, 1);
            if (addresses != null && addresses.size() > 0) {
                post.setLatLong(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()));
                post.setGeohash(GeoFireUtils.getGeoHashForLocation(new GeoLocation(addresses.get(0).getLatitude(), addresses.get(0).getLongitude())));
            } else {
                Toast.makeText(this.getContext(), "Invalid postal code", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (editTextPostInitiatorName.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter display name", Toast.LENGTH_SHORT).show();
            return;
        }
        post.setInitiatorName(editTextPostInitiatorName.getText().toString());
        post.setInitiatorGender(spinnerPostInitiatorGender.getSelectedItem().toString());
        post.setInitiatorAge(editTextPostInitiatorAge.getText().toString());
        if (editTextPostInitiatorDescription.getText().toString().isEmpty()) {
            Toast.makeText(this.getContext(), "Please enter your description", Toast.LENGTH_SHORT).show();
            return;
        }
        post.setInitiatorDescription(editTextPostInitiatorDescription.getText().toString());
        if (uploadPostInitiatorImageFileName != null) {
            post.setInitiatorImage(uploadPostInitiatorImageFileName);
        }
        postViewModel.updatePost(post, (unused) -> {
            Toast.makeText(EditPersonPostFragment.this.getContext(), "Post updated", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction().replace(R.id.mainContainer, MyPostFragment.class, savedInstanceState).commit();
        });
    }

    private void cancelPost(@Nullable Bundle savedInstanceState) {
        post.setPostStatus(PostStatus.CANCELLED);
        postViewModel.updatePost(post, (unused) -> {
            Toast.makeText(EditPersonPostFragment.this.getContext(), "Post updated", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().beginTransaction().replace(R.id.mainContainer, MyPostFragment.class, savedInstanceState).commit();
        });
    }
}