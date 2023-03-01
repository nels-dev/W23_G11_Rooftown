package csis3175.w23.g11.rooftown.user.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class UserProfileDao {
    private static final String TAG = "USER_PROFILE";
    private static final String TABLE = "USER_PROFILE";
    private static final String[] ALL_COLUMNS = new String[]{
            "user_id",
            "user_name",
            "city",
            "country",
            "image_file_name"
    };

    public UserProfile getByUserId(String userId) {
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "user_id=?",
                new String[]{userId},
                null,
                null,
                null);
        UserProfile profile = null;
        while (cursor.moveToNext()) {
            profile = new UserProfile();
            profile.setUserId(cursor.getString(0));
            profile.setUserName(cursor.getString(1));
            profile.setCity(cursor.getString(2));
            profile.setCountry(cursor.getString(3));
            profile.setImageFileName(cursor.getString(4));
        }
        cursor.close();
        return profile;
    }

    public void insert(UserProfile profile) {
        ContentValues cv = new ContentValues();
        cv.put("user_id", profile.getUserId());
        cv.put("user_name", profile.getUserName());
        cv.put("city", profile.getCity());
        cv.put("country", profile.getCountry());
        cv.put("image_file_name", profile.getImageFileName());
        DatabaseHelper.getInstance().getWritableDatabase().insert(TABLE, null, cv);
    }

    public void update(UserProfile profile) {
        ContentValues cv = new ContentValues();
        cv.put("user_name", profile.getUserName());
        cv.put("city", profile.getCity());
        cv.put("country", profile.getCountry());
        cv.put("image_file_name", profile.getImageFileName());
        DatabaseHelper.getInstance().getWritableDatabase().update(TABLE, cv, "user_id=?", new String[]{profile.getUserId()});
    }

    public boolean exist(String userId) {
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase()
                .query(TABLE, new String[]{"user_id"}, "user_id=?", new String[]{userId}, null, null, null);
        boolean exist = cursor.getCount() > 0;
        cursor.close();
        return exist;
    }

    public void insertOrUpdate(List<UserProfile> profiles) {
        for (UserProfile up : profiles) {
            if (exist(up.getUserId())) {
                update(up);
            } else {
                insert(up);
            }
        }
    }
}
