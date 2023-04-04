package csis3175.w23.g11.rooftown.posts.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.common.CallbackListener;
import csis3175.w23.g11.rooftown.messages.data.repository.ChatRepository;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.repository.PostRepository;

public class PostViewModel extends ViewModel {
    private static final String TAG = "POSTS";
    private final LiveData<List<Post>> posts;
    private final PostRepository postRepository;
    private final ChatRepository chatRepository;

    public PostViewModel() {
        postRepository = new PostRepository();
        chatRepository = new ChatRepository();
        posts = postRepository.getPosts();
    }

    public LiveData<List<Post>> getAllPosts() {
        return posts;
    }

    public Post getPost(UUID postId) {
        List<Post> _posts = posts.getValue();
        if (_posts != null) {
            for (Post post : _posts) {
                if (post.getPostId().equals(postId)) {
                    return post;
                }
            }
        }
        return null;
    }

    public Post getMyPost(String uid) {
        List<Post> _posts = posts.getValue();
        if (_posts != null) {
            for (Post post : _posts) {
                if (post.getInitiator().equals(uid)) {
                    return post;
                }
            }
        }
        return null;
    }

    public void loadData(LatLng currentLocation) {
        postRepository.loadPosts(currentLocation);
    }

    public void createPost(Post post, CallbackListener<Void> callback) {
        postRepository.createPost(post, callback);
    }

    public void updatePost(Post post, CallbackListener<Void> callback) {
        postRepository.updatePost(post, callback);
    }

    public void showInterest(String to, UUID postId, CallbackListener<UUID> callback) {
        chatRepository.sendMessage(to, postId, "User is interested in your posting! Send a response?", true, callback);
    }
}
