package csis3175.w23.g11.rooftown.util;

import com.google.firebase.auth.FirebaseAuth;

public class CurrentUserHelper {

    public static String getCurrentUid(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static String getCurrentUserEmail(){
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }


    public static String getCurrentUserName(){
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }
}
