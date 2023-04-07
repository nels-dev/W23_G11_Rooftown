package csis3175.w23.g11.rooftown.user.data.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.common.AppDatabase;
import csis3175.w23.g11.rooftown.common.CallbackListener;
import csis3175.w23.g11.rooftown.user.data.local.UserProfileDao;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.user.data.remote.UserProfileService;

public class UserProfileRepository {
    private final UserProfileDao userProfileDao;
    private final UserProfileService userProfileService;
    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();

    public UserProfileRepository() {
        userProfileDao = AppDatabase.getInstance().userProfileDao();
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
        AsyncTask.execute(() -> {
            UserProfile p = userProfileDao.getByUserId(userId);
            if (p != null) {
                userProfile.postValue(p);
            }
            syncWithRemote(Arrays.asList(userId), (unused) -> {
                userProfile.postValue(userProfileDao.getByUserId(userId));
            });
        });
    }

    // Load multiple users
    public void loadUsers(List<String> userIds, CallbackListener<Void> callback) {
        AsyncTask.execute(() -> {
            syncWithRemote(userIds, callback);
        });
    }

    public void updateUserProfile(UserProfile profile, CallbackListener<Void> callback) {
        AsyncTask.execute(() -> {
            userProfileDao.update(profile);
            userProfileService.updateUserProfile(profile, callback);
        });

    }

    public void createIfNotExist(UserProfile profile) {
        AsyncTask.execute(() -> {
            userProfileDao.insert(profile);
            userProfileService.createIfNotExist(profile);
        });

    }

    public void syncWithRemote(List<String> userIds, CallbackListener<Void> callback) {

        userProfileService.loadUserProfiles(userIds, userProfiles -> {
            AsyncTask.execute(() -> {
                userProfileDao.insertOrUpdate(userProfiles);
                callback.callback(null);
            });
        });
    }
}
