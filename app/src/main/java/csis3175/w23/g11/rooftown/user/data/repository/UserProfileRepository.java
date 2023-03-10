package csis3175.w23.g11.rooftown.user.data.repository;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.user.data.local.UserProfileDao;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.data.remote.UserProfileService;
import csis3175.w23.g11.rooftown.util.CallbackListener;

public class UserProfileRepository {
    private static final String TAG = "USER_PROFILE";
    private final UserProfileDao userProfileDao;
    private final UserProfileService userProfileService;
    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();

    public UserProfileRepository() {
        userProfileDao = new UserProfileDao();
        userProfileService = new UserProfileService();
    }

    public UserProfile getUserProfile(String userId) {
        return userProfileDao.getByUserId(userId);
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    // Load single user, data is forcefully sync with remote
    public void loadUser(String userId) {
        UserProfile p = userProfileDao.getByUserId(userId);
        if (p != null) {
            userProfile.setValue(p);
        }
        syncWithRemote(Arrays.asList(userId), (unused) -> {
            userProfile.postValue(userProfileDao.getByUserId(userId));
        });
    }

    // Load multiple users.
    // For performance concerns, data is NOT forcefully sync with remote if exist locally
    public void loadUsers(List<String> userIds, CallbackListener<Void> callback) {
        List<String> notFound = new ArrayList<>();
        for (String userId : userIds) {
            if (!userProfileDao.exist(userId)) {
                notFound.add(userId);
            }
        }
        if (!notFound.isEmpty()) {
            syncWithRemote(notFound, callback);
        } else {
            callback.callback(null);
        }
    }

    public void updateUserProfile(UserProfile profile, CallbackListener<Void> callback) {
        userProfileService.updateUserProfile(profile, callback);
    }

    public void createIfNotExist(UserProfile profile) {
        userProfileService.createIfNotExist(profile);
    }


    public void syncWithRemote(List<String> userIds, CallbackListener<Void> callback) {
        new Handler().post(() -> {
            userProfileService.loadUserProfiles(userIds, userProfiles -> {
                userProfileDao.insertOrUpdate(userProfiles);
                callback.callback(null);
            });
        });
    }
}
