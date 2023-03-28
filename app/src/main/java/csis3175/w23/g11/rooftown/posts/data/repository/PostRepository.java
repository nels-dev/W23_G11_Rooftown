package csis3175.w23.g11.rooftown.posts.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.posts.data.local.PostDao;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.remote.PostService;
import csis3175.w23.g11.rooftown.util.CallbackListener;

public class PostRepository {
    private static final String TAG = "POSTS";
    private final PostDao postDao;
    private final PostService postService;
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    public PostRepository () {
        postDao = new PostDao();
        postService = new PostService();
    }

    public LiveData<List<Post>> getPosts() { return posts; }

    public ListenerRegistration loadAndListenToPosts() {
        posts.setValue(postDao.getPosts());
        return postService.listenToAllPosts(this::remoteCallBackWithData);
    }

    public void remoteCallBackWithData(List<Post> posts) {
        postDao.insertOrUpdate(posts);
    }

    public void createPost(Post post, CallbackListener<Void> callback) {
        post.setPostId(UUID.randomUUID());
        postDao.insert(post);
        postService.savePost(post, callback);
    }

    public void updatePost(Post post, CallbackListener<Void> callback) {
        postDao.update(post);
        postService.savePost(post, callback);
    }
}
