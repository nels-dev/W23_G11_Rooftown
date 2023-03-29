package csis3175.w23.g11.rooftown.common;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import csis3175.w23.g11.rooftown.messages.data.local.ChatDao;
import csis3175.w23.g11.rooftown.messages.data.local.ChatMessageDao;
import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.posts.data.local.PostDao;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.user.data.local.UserProfileDao;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;

@Database(entities = {Chat.class, ChatMessage.class, Post.class, UserProfile.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance = null;

    public static AppDatabase getInstance() {
        if (null == instance) {
            throw new RuntimeException("Database not initialized");
        }
        return instance;
    }

    public static void init(Context context) {
        instance = Room.databaseBuilder(context, AppDatabase.class, "rooftop-db").fallbackToDestructiveMigration().build();
    }

    public abstract ChatDao chatDao();

    public abstract ChatMessageDao chatMessageDao();

    public abstract UserProfileDao userProfileDao();

    public abstract PostDao postDao();
}
