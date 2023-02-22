package csis3175.w23.g11.rooftown.user.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.user.data.model.UserProfile;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class UserProfileDao {
    private static final String TAG = "USER_PROFILE";
    private static final String TABLE = "USER_PROFILE";
    private static final String[] ALL_COLUMNS = new String[]{
            "user_id",
            "user_name",
    };

    public String getNameByUserId(String userId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "user_id=?",
                new String[]{userId},
                null,
                null,
                null);
        String name = null;
        while(cursor.moveToNext()){
            name = cursor.getString(1);
        }
        cursor.close();
        return name;
    }

    public void insert(UserProfile profile){
        ContentValues cv = new ContentValues();
        cv.put("user_id", profile.getUserId());
        cv.put("user_name", profile.getUserName());
        DatabaseHelper.getInstance().getWritableDatabase().insert(TABLE, null, cv);
    }

    public void update(UserProfile profile){
        ContentValues cv = new ContentValues();
        cv.put("user_name", profile.getUserName());
        DatabaseHelper.getInstance().getWritableDatabase().update(TABLE, cv, "user_id=?", new String[]{profile.getUserId()});
    }

    public boolean exist(String userId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase()
                .query(TABLE, new String[]{"user_id"}, "user_id=?", new String[]{userId}, null, null, null );
        boolean exist = cursor.getCount() >0;
        cursor.close();
        return exist;
    }

    public void insertOrUpdate(List<UserProfile> profiles){
        for(UserProfile up: profiles){
            if(exist(up.getUserId())){
                update(up);
            }else{
                insert(up);
            }
        }
    }
}
