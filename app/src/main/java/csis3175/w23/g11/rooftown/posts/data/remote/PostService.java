package csis3175.w23.g11.rooftown.posts.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.common.CallbackListener;
import csis3175.w23.g11.rooftown.common.Converters;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostDto;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;

public class PostService {
    public static final String COLLECTION_POST = "POSTS";
    private static final String TAG = "POSTS";
    private final FirebaseFirestore fs;
    final double radiusInM = 10000;

    public PostService() {
        fs = FirebaseFirestore.getInstance();
    }

    public void fetchPosts(LatLng currentLocation, CallbackListener<List<Post>> resultConsumer) {
        // a post, callback listener with a list post
        GeoLocation geoLocation = new GeoLocation(currentLocation.latitude, currentLocation.longitude);
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(geoLocation, radiusInM);


        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = fs.collection(COLLECTION_POST)
                    .whereEqualTo("postStatus", PostStatus.OPEN.name())
                    .orderBy("geohash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);
            tasks.add(q.get());
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(t -> {
                    List<Post> posts = new ArrayList<>();
                    for (Task<QuerySnapshot> task : tasks) {
                        QuerySnapshot snap = task.getResult();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            posts.add(toPost(doc));
                        }
                    }
                    resultConsumer.callback(posts);
                });
    }

    public void savePost(Post post, CallbackListener<Void> callback) {
        Log.d(TAG, ">> Invoke save post to Firestore: " + post.getPostId().toString());
        PostDto dto = toDto(post);
        fs.collection(COLLECTION_POST)
                .document(post.getPostId().toString())
                .set(dto)
                .addOnSuccessListener(callback::callback)
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
            post.setPostalCode(dto.getPostalCode());
            post.setLatLong(Converters.fromLatLngString(dto.getLatLong()));
            post.setGeohash(dto.getGeohash());
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

    private PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setPostType(post.getPostType().toString());
        dto.setLocation(post.getLocation());
        dto.setCity(post.getCity());
        dto.setCountry(post.getCountry());
        dto.setPostalCode(post.getPostalCode());
        dto.setLatLong(Converters.toLatLngString(post.getLatLong()));
        dto.setGeohash(post.getGeohash());
        dto.setNumOfRooms(post.getNumOfRooms());
        dto.setFurnished(post.isFurnished());
        dto.setSharedBathroom(post.isSharedBathroom());
        dto.setRoomDescription(post.getRoomDescription());
        dto.setRoomImage(post.getRoomImage());
        dto.setInitiator(post.getInitiator());
        dto.setInitiatorName(post.getInitiatorName());
        dto.setInitiatorGender(post.getInitiatorGender());
        dto.setInitiatorAge(post.getInitiatorAge());
        dto.setInitiatorDescription(post.getInitiatorDescription());
        dto.setInitiatorImage(post.getInitiatorImage());
        dto.setPostStatus(post.getPostStatus().toString());
        dto.setPostAt(post.getPostAt());
        return dto;
    }
}
