package csis3175.w23.g11.rooftown.user.data.remote;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatDto;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;

/**
 * This service class is responsible for talking to Firestore
 * including retrieving, observing and saving documents
 */
public class UserProfileService {
    private static final String TAG = "USER_PROFILES";
    public static final String COLLECTION_USER_PROFILES = "USER_PROFILES";
    private final FirebaseFirestore fs;

    public UserProfileService(){
        fs = FirebaseFirestore.getInstance();
    }

    public void loadUserProfiles(List<String> userIds, ListUpdateListener<UserProfile> resultConsumer){
        fs.collection(COLLECTION_USER_PROFILES)
                .whereIn(FieldPath.documentId(), userIds)
                .get()
                .addOnCompleteListener(task -> {
                    List<DocumentSnapshot> resultList = task.getResult().getDocuments();
                    List<UserProfile> userProfiles = new ArrayList<>();
                    for(DocumentSnapshot doc: resultList){
                        userProfiles.add(doc.toObject(UserProfile.class));
                    }
                    resultConsumer.onListUpdated(userProfiles);
                });
    }

    public interface ListUpdateListener<T> {
        void onListUpdated(List<T> list);
    }
}
