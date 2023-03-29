package csis3175.w23.g11.rooftown.user.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import csis3175.w23.g11.rooftown.user.data.model.UserProfile;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM USER_PROFILE where userId=:userId")
    UserProfile getByUserId(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserProfile profile);

    @Update
    void update(UserProfile profile);

    @Query("SELECT COUNT(1) > 0 FROM user_profile WHERE userId=:userId")
    boolean exist(String userId);

    @Upsert
    void insertOrUpdate(List<UserProfile> profiles);
}
