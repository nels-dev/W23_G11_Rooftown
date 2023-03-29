package csis3175.w23.g11.rooftown.user.data.remote;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import csis3175.w23.g11.rooftown.common.CallbackListener;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;

/**
 * This service class is responsible for talking to Firestore
 * including retrieving, observing and saving documents
 */
public class UserProfileService {
    public static final String COLLECTION_USER_PROFILES = "USER_PROFILES";
    private static final String TAG = "USER_PROFILES";
    private final FirebaseFirestore fs;

    public UserProfileService() {
        fs = FirebaseFirestore.getInstance();
    }

    public void loadUserProfiles(List<String> userIds, CallbackListener<List<UserProfile>> resultConsumer) {
        fs.collection(COLLECTION_USER_PROFILES)
                .whereIn(FieldPath.documentId(), userIds)
                .get()
                .addOnCompleteListener(task -> {
                    List<DocumentSnapshot> resultList = task.getResult().getDocuments();
                    List<UserProfile> userProfiles = new ArrayList<>();
                    for (DocumentSnapshot doc : resultList) {
                        userProfiles.add(doc.toObject(UserProfile.class));
                    }
                    resultConsumer.callback(userProfiles);
                });
    }

    public void createIfNotExist(UserProfile userProfile) {
        DocumentReference document = fs.collection(COLLECTION_USER_PROFILES).document(userProfile.getUserId());
        document.get().addOnCompleteListener(task -> {
            if (!task.getResult().exists()) {
                document.set(userProfile);
            }
        });
    }

    public void updateUserProfile(UserProfile profile, CallbackListener<Void> callback) {
        fs.collection(COLLECTION_USER_PROFILES).document(profile.getUserId())
                .set(profile)
                .addOnSuccessListener(callback::callback)
                .addOnFailureListener(e -> Log.e(TAG, "Unable to update user profile", e));
    }
}
