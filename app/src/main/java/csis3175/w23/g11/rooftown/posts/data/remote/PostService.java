package csis3175.w23.g11.rooftown.posts.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostDto;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.util.CallbackListener;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class PostService {
    private static final String TAG = "POSTS";
    public static final String COLLECTION_POST = "POSTS";
    private final FirebaseFirestore fs;
    private final Query allPosts;

    public PostService() {
        fs = FirebaseFirestore.getInstance();
        allPosts = fs.collection(COLLECTION_POST)
                .whereNotEqualTo("postStatus", PostStatus.CANCELLED.name());
    }

    public ListenerRegistration listenToAllPosts(CallbackListener<List<Post>> resultConsumer) {
        return allPosts.addSnapshotListener(MetadataChanges.EXCLUDE, (value, error) -> {
            if (value == null) return;
            Log.d(TAG, "listenToAllPosts");
            Log.d(TAG, "value:" + value);
            Log.d(TAG, "getDocuments size:" + value.getDocuments().size());
            List<Post> posts = new ArrayList<>();
            for (DocumentSnapshot doc : value.getDocuments()) {
                Log.d(TAG, "doc:" + doc);
                posts.add(toPost(doc));
            }
            resultConsumer.callback(posts);
        });
    }

    public void savePost(Post post) {
        fs.collection(COLLECTION_POST)
                .document(post.getPostId().toString())
                .set(post)
                .addOnSuccessListener((Void unused) -> Log.d(TAG, "Post document saved"))
                .addOnFailureListener((@NonNull Exception e) -> Log.d(TAG, "Cannot save document", e));
    }

    private Post toPost(DocumentSnapshot doc) {
        PostDto dto = doc.toObject(PostDto.class);
        Post post = new Post();
        if (dto != null) {
            post.setPostId(UUID.fromString(doc.getId()));
            post.setPostType(PostType.valueOf(dto.getPostType()));
            post.setLocation(dto.getLocation());
            post.setCity(dto.getCity());
            post.setCountry(dto.getCountry());
            post.setLatLong(DatabaseHelper.fromLatLngString(dto.getLatLong()));
            post.setNumOfRooms(dto.getNumOfRooms());
            post.setFurnished(dto.isFurnished());
            post.setSharedBathroom(dto.isSharedBathroom());
            post.setRoomDescription(dto.getRoomDescription());
            post.setRoomImage(dto.getRoomImage());
            post.setInitiator(dto.getInitiator());
            post.setInitiatorName(dto.getInitiatorName());
            post.setInitiatorGender(dto.getInitiatorGender());
            post.setInitiatorAge(dto.getInitiatorAge());
            post.setInitiatorDescription(dto.getInitiatorDescription());
            post.setInitiatorImage(dto.getInitiatorImage());
            post.setPostStatus(PostStatus.valueOf(dto.getPostStatus()));
            post.setPostAt(dto.getPostAt());
        }
        return post;
    }
}
