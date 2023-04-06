package csis3175.w23.g11.rooftown.posts.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.posts.data.model.Post;

@Dao
public interface PostDao {

    @Query("SELECT * FROM POSTS WHERE post_status<>'CANCELLED'")
    List<Post> getPosts();

    @Query("SELECT * FROM POSTS WHERE postId = :postId")
    Post getByPostId(UUID postId);

    @Query("SELECT * FROM POSTS WHERE initiator = :userId AND post_status<>'CANCELLED'")
    Post getPostByInitiator(String userId);


    @Update
    void update(Post post);

    @Insert
    void insert(Post post);

    @Query("SELECT count(1)>0 FROM POSTS WHERE postId=:postId")
    boolean exist(UUID postId);

    @Upsert
    void insertOrUpdate(List<Post> posts);
}
