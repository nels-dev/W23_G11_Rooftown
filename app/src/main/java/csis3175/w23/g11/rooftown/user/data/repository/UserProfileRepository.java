package csis3175.w23.g11.rooftown.user.data.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csis3175.w23.g11.rooftown.user.data.local.UserProfileDao;
import csis3175.w23.g11.rooftown.user.data.remote.UserProfileService;

public class UserProfileRepository {
    private static final String TAG = "USER_PROFILE";
    private final UserProfileDao userProfileDao;
    private final UserProfileService userProfileService;

    public UserProfileRepository(){
        userProfileDao = new UserProfileDao();
        userProfileService = new UserProfileService();
    }

    public String getUserName(String userId){
        return userProfileDao.getNameByUserId(userId);
    }

    public void loadUsers(List<String> userIds, Runnable callback){
        List<String> notFound = new ArrayList<>();
        for(String userId: userIds){
            if(!userProfileDao.exist(userId)){
                notFound.add(userId);
            }
        }
        if(!notFound.isEmpty()){
            syncWithRemote(notFound, callback);
        }else{
            callback.run();
        }
    }


    public void syncWithRemote(List<String> userIds, Runnable callback){
        userProfileService.loadUserProfiles(userIds, userProfiles->{
            userProfileDao.insertOrUpdate(userProfiles);
            callback.run();
        });
    }
}
