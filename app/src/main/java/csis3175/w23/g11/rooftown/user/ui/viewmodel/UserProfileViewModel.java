package csis3175.w23.g11.rooftown.user.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import csis3175.w23.g11.rooftown.common.CallbackListener;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.data.repository.UserProfileRepository;

public class UserProfileViewModel extends ViewModel {
    private final LiveData<UserProfile> userProfile;
    private final UserProfileRepository repository;
    private String userId;

    public UserProfileViewModel() {
        this.repository = new UserProfileRepository();
        userProfile = repository.getUserProfile();
    }

    public void updateUserProfile(UserProfile profile, CallbackListener<Void> callback) {
        repository.updateUserProfile(profile, callback);
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public void loadData() {
        repository.loadUser(userId);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}