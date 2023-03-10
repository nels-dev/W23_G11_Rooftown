package csis3175.w23.g11.rooftown.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;
    private static final SimpleDateFormat storageDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = "DATABASE";
    private static final String DATABASE_NAME = "rooftown.db"; //In-memory database
    private static final int DATABASE_VERSION = 5;

    public static DatabaseHelper getInstance(){
        if(null==instance){
            Log.e(TAG, "Database not initialized. Call DatabaseHelper.init()");
            throw new RuntimeException("Database not initialized");
        }
        return instance;
    }
    public static void init(Context context){
        instance = new DatabaseHelper(context);
    }

    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dropAndInitDB(db);
    }

    private void dropAndInitDB(SQLiteDatabase db){
        db.execSQL("drop table if exists CHAT;");
        db.execSQL("drop table if exists CHAT_MESSAGE;");
        db.execSQL("drop table if exists USER_PROFILE;");
        db.execSQL("create table CHAT(" +
                "chat_id varchar(36) primary key, " +
                "initiator varchar(36), " +
                "counter_party varchar(36)," +
                "partner_name varchar(100), " +
                "partner_image varchar(100), " +
                "last_activity_at datetime, " +
                "last_activity_by varchar(36), " +
                "last_message text, " +
                "last_read_at datetime)");
        db.execSQL("create table CHAT_MESSAGE(chat_message_id varchar(36) primary key, " +
                "content text, " +
                "chat_id varchar(36), " +
                "sent_at datetime, " +
                "sent_by varchar(36), " +
                "system_message integer)");
        db.execSQL("create table USER_PROFILE(user_id varchar(36) primary key, " +
                "user_name varchar(100), city varchar(100), country varchar(50), image_file_name varchar(50))");
    }

    public static String toDateString(Date date){
        return storageDateFormat.format(date);
    }

    public static Date fromDateString(String date){
        if(date==null) return null;
        try {
            return storageDateFormat.parse(date);
        }catch(ParseException e){
            Log.e(TAG, "Unable to parse date string "+ date);
            return null;
        }
    }

    public static String toLatLngString(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }

    public static LatLng fromLatLngString(String latLng) {
        if (latLng == null) return null;
        try {
            String[] splitLatLng = latLng.split(",");
            if (splitLatLng.length != 2) return null;
            return new LatLng(Double.parseDouble(splitLatLng[0]), Double.parseDouble(splitLatLng[1]));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Unable to parse latLng string " + latLng);
            return null;
        }
    }

    public static String toBooleanString(boolean bool) {
        return bool ? "TRUE" : "FALSE";
    }

    public static boolean fromBooleanString(String bool) {
        return (bool != null && bool.equals("TRUE"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // So that the entire DB is re-created when there is schema change
        dropAndInitDB(db);
    }
}
