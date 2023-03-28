package csis3175.w23.g11.rooftown.posts.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.repository.PostRepository;
import csis3175.w23.g11.rooftown.util.CallbackListener;

public class PostViewModel extends ViewModel {
    private final LiveData<List<Post>> posts;
    private final PostRepository postRepository;
    private ListenerRegistration allPostsRegistration;

    public PostViewModel() {
        postRepository = new PostRepository();
        posts = postRepository.getPosts();
    }

    public LiveData<List<Post>> getAllPosts() { return posts; }

    public void loadData() {
        if (allPostsRegistration != null) {
            allPostsRegistration.remove();
        }
        allPostsRegistration = postRepository.loadAndListenToPosts();
    }

    public void createPost(Post post, CallbackListener<Void> callback) {
        postRepository.createPost(post, callback);
    }

    public void updatePost(Post post, CallbackListener<Void> callback) {
        postRepository.updatePost(post, callback);
    }
}
