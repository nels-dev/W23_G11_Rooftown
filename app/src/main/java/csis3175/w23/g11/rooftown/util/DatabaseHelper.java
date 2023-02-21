package csis3175.w23.g11.rooftown.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance = null;
    private static final SimpleDateFormat storageDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TAG = "DATABASE";
    private static final String DATABASE_NAME = "rooftown.db"; //In-memory database
    private static final int DATABASE_VERSION = 1;

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
        Log.d(TAG, "Creating database tables");
        db.execSQL("drop table if exists CHAT;");
        db.execSQL("drop table if exists CHAT_MESSAGE;");
        db.execSQL("drop table if exists USER_PROFILE;");
        db.execSQL("create table CHAT(" +
                "chat_id varchar(36) primary key, " +
                "initiator varchar(36), " +
                "counter_party varchar(36)," +
                "partner_name varchar(100), " +
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
                "user_name varchar(100)) ");
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not necessary unless database version is changed
    }
}
