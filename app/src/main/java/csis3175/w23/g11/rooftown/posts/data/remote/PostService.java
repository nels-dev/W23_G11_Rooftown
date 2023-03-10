package csis3175.w23.g11.rooftown.posts.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.util.CallbackListener;

public class PostService {
    private static final String TAG = "POSTS";
    public static final String COLLECTION_POST = "POSTS";
    private final FirebaseFirestore fs;
    private final Query allPosts;

    public PostService() {
        fs = FirebaseFirestore.getInstance();
        allPosts = fs.collection(COLLECTION_POST)
                .whereNotEqualTo("post_status", PostStatus.CANCELLED.name());
    }

    public ListenerRegistration listenToAllPosts(CallbackListener<List<Post>> resultConsumer) {
        return allPosts.addSnapshotListener(MetadataChanges.EXCLUDE, (@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) -> {
            if (value == null) return;
            for (DocumentChange docChange : value.getDocumentChanges()) {
                List<Post> delta = new ArrayList<>();
                if (docChange.getType() == DocumentChange.Type.ADDED) {
                    Log.d(TAG, "Post created: " + docChange.getDocument().getId());
                    delta.add(toPost(docChange.getDocument()));
                } else if (docChange.getType() == DocumentChange.Type.MODIFIED) {
                    Log.d(TAG, "Post updated: " + docChange.getDocument().getId());
                    delta.add(toPost(docChange.getDocument()));
                } else if (docChange.getType() == DocumentChange.Type.REMOVED) {
                    Log.d(TAG, "Post deleted: " + docChange.getDocument().getId());
                }
                resultConsumer.callback(delta);
            }
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
        Post post = doc.toObject(Post.class);
        if (post != null) {
            post.setPostId(UUID.fromString(doc.getId()));
        }
        return post;
    }
}
