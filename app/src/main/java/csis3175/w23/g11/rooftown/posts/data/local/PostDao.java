package csis3175.w23.g11.rooftown.posts.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostStatus;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class PostDao {
    private static final String TAG = "POSTS";
    private static final String TABLE = "POST";
    private static final String[] ALL_COLUMNS = new String[]{
            "post_id",
            "post_type",
            "location",
            "city",
            "country",
            "lat_long",
            "num_of_rooms",
            "furnished",
            "shared_bathroom",
            "room_description",
            "room_image",
            "initiator",
            "initiator_name",
            "initiator_gender",
            "initiator_age",
            "initiator_description",
            "initiator_image",
            "post_status",
            "post_at"
    };

    public List<Post> getPosts() {
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "post_status!=?",
                new String[]{"CANCELLED"},
                null,
                null,
                null
        );
        List<Post> posts = new ArrayList<>();
        while (cursor.moveToNext()) {
            posts.add(getData(cursor));
        }
        cursor.close();
        return posts;
    }

    public Post getByPostId(UUID postId) {
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "post_id=?",
                new String[]{postId.toString()},
                null,
                null,
                null
        );
        Post post = null;
        while (cursor.moveToNext()) {
            post = getData(cursor);
        }
        cursor.close();
        return post;
    }

    public Post getPostByInitiator(String userId) {
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "initiator=? AND post_status!=?",
                new String[]{userId, "CANCELLED"},
                null,
                null,
                null
        );
        Post post = null;
        while (cursor.moveToNext()) {
            post = getData(cursor);
        }
        cursor.close();
        return post;
    }

    private Post getData(Cursor cursor) {
        Post post = new Post();
        post.setPostId(UUID.fromString(cursor.getString(0)));
        post.setPostType(PostType.valueOf(cursor.getString(1)));
        post.setLocation(cursor.getString(2));
        post.setCity(cursor.getString(3));
        post.setCountry(cursor.getString(4));
        post.setLatLong(DatabaseHelper.fromLatLngString(cursor.getString(5)));
        post.setNumOfRooms(cursor.getString(6));
        post.setFurnished(DatabaseHelper.fromBooleanString(cursor.getString(7)));
        post.setSharedBathroom(DatabaseHelper.fromBooleanString(cursor.getString(8)));
        post.setRoomDescription(cursor.getString(9));
        post.setRoomImage(cursor.getString(10));
        post.setInitiator(cursor.getString(11));
        post.setInitiatorName(cursor.getString(12));
        post.setInitiatorGender(cursor.getString(13));
        post.setInitiatorAge(cursor.getString(14));
        post.setInitiatorDescription(cursor.getString(15));
        post.setInitiatorImage(cursor.getString(16));
        post.setPostStatus(PostStatus.valueOf(cursor.getString(17)));
        post.setPostAt(DatabaseHelper.fromDateString(cursor.getString(18)));
        return post;
    }

    public void insert(Post post) {
        Log.d(TAG, ">> Inserting post to local DB: " + post.getPostId().toString());
        ContentValues cv = new ContentValues();
        cv.put("post_id", post.getPostId().toString());
        cv.put("post_type", post.getPostType().name());
        cv.put("location", post.getLocation());
        cv.put("city", post.getCity());
        cv.put("country", post.getCountry());
        cv.put("lat_long", DatabaseHelper.toLatLngString(post.getLatLong()));
        cv.put("num_of_rooms", post.getNumOfRooms());
        cv.put("furnished", DatabaseHelper.toBooleanString(post.isFurnished()));
        cv.put("shared_bathroom", DatabaseHelper.toBooleanString(post.isSharedBathroom()));
        cv.put("room_description", post.getRoomDescription());
        cv.put("room_image", post.getRoomImage());
        cv.put("initiator", post.getInitiator());
        cv.put("initiator_name", post.getInitiatorName());
        cv.put("initiator_gender", post.getInitiatorGender());
        cv.put("initiator_age", post.getInitiatorAge());
        cv.put("initiator_description", post.getInitiatorDescription());
        cv.put("initiator_image", post.getInitiatorImage());
        cv.put("post_status", post.getPostStatus().name());
        cv.put("post_at", DatabaseHelper.toDateString(post.getPostAt()));
        DatabaseHelper.getInstance().getWritableDatabase().insert(TABLE, null, cv);
        Log.d(TAG, "<< Insert post to local DB done");
    }

    public void update(Post post) {
        ContentValues cv = new ContentValues();
        cv.put("post_type", post.getPostType().name());
        cv.put("location", post.getLocation());
        cv.put("city", post.getCity());
        cv.put("country", post.getCountry());
        cv.put("lat_long", DatabaseHelper.toLatLngString(post.getLatLong()));
        cv.put("num_of_rooms", post.getNumOfRooms());
        cv.put("furnished", DatabaseHelper.toBooleanString(post.isFurnished()));
        cv.put("shared_bathroom", DatabaseHelper.toBooleanString(post.isSharedBathroom()));
        cv.put("room_description", post.getRoomDescription());
        cv.put("room_image", post.getRoomImage());
        cv.put("initiator", post.getInitiator());
        cv.put("initiator_name", post.getInitiatorName());
        cv.put("initiator_gender", post.getInitiatorGender());
        cv.put("initiator_age", post.getInitiatorAge());
        cv.put("initiator_description", post.getInitiatorDescription());
        cv.put("initiator_image", post.getInitiatorImage());
        cv.put("post_status", post.getPostStatus().name());
        cv.put("post_at", DatabaseHelper.toDateString(post.getPostAt()));
        DatabaseHelper.getInstance().getWritableDatabase().update(TABLE, cv, "post_id=?", new String[]{post.getPostId().toString()});
    }

    public boolean exist(UUID postId) {
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                new String[]{"post_id"},
                "post_id=?",
                new String[]{postId.toString()},
                null,
                null,
                null
        );
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public void insertOrUpdate(List<Post> posts) {
        for (Post post: posts) {
            if (exist(post.getPostId())) {
                update(post);
            } else {
                insert(post);
            }
        }
    }
}
