package csis3175.w23.g11.rooftown.messages.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class ChatDao {
    private static final String TAG = "CHATS";
    private static final String TABLE = "CHAT";
    private static final String[] ALL_COLUMNS = new String[]{
            "chat_id",
            "initiator",
            "counter_party",
            "partner_name",
            "last_activity_at",
            "last_activity_by",
            "last_message",
            "last_read_at",
            "partner_image"
    };

    public List<Chat> getChatsByInitiator(String userId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "initiator=?",
                new String[]{userId},
                null,
                null,
                "last_activity_at desc");
        List<Chat> result = new ArrayList<>();
        while(cursor.moveToNext()){
            result.add(getData(cursor));
        }
        cursor.close();
        return result;
    }

    public List<Chat> getChatsByCounterParty(String userId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().query(
                TABLE,
                ALL_COLUMNS,
                "counter_party=?",
                new String[]{userId},
                null,
                null,
                "last_activity_at desc");
        List<Chat> result = new ArrayList<>();
        while(cursor.moveToNext()){
            result.add(getData(cursor));
        }
        cursor.close();
        return result;
    }

    private Chat getData(Cursor cursor){
        Chat chat = new Chat();
        chat.setChatId(UUID.fromString(cursor.getString(0)));
        chat.setInitiator(cursor.getString(1));
        chat.setCounterParty(cursor.getString(2));
        chat.setPartnerName(cursor.getString(3));
        chat.setLastActivityAt(DatabaseHelper.fromDateString(cursor.getString(4)));
        chat.setLastActivityBy(cursor.getString(5));
        chat.setLastMessage(cursor.getString(6));
        chat.setLastReadAt(DatabaseHelper.fromDateString(cursor.getString(7)));
        chat.setPartnerImage(cursor.getString(8));
        return chat;
    }

    public void insertChat(Chat chat){
        Log.d(TAG, ">> Inserting chat to local DB: " + chat.getChatId().toString());
        ContentValues cv = new ContentValues();
        cv.put("chat_id", chat.getChatId().toString());
        cv.put("initiator", chat.getInitiator());
        cv.put("counter_party", chat.getCounterParty());
        cv.put("partner_name", chat.getPartnerName());
        cv.put("last_activity_at", DatabaseHelper.toDateString(chat.getLastActivityAt()));
        cv.put("last_activity_by", chat.getLastActivityBy());
        cv.put("last_message", chat.getLastMessage());
        cv.put("partner_image", chat.getPartnerImage());
        DatabaseHelper.getInstance().getWritableDatabase().insert(TABLE, null, cv);
        Log.d(TAG, "<< Insert chat to local DB done");
    }

    public void updateChat(Chat chat){
        ContentValues cv = new ContentValues();
        cv.put("partner_name", chat.getPartnerName());
        cv.put("partner_image", chat.getPartnerImage());
        cv.put("last_activity_at", DatabaseHelper.toDateString(chat.getLastActivityAt()));
        cv.put("last_activity_by", chat.getLastActivityBy());
        cv.put("last_message", chat.getLastMessage());
        DatabaseHelper.getInstance().getWritableDatabase().update(TABLE, cv, "chat_id=?", new String[]{chat.getChatId().toString()});
    }

    private boolean exist(UUID chatId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase()
                .query(TABLE, new String[]{"chat_id"}, "chat_id=?", new String[]{chatId.toString()}, null, null, null );
        boolean exist = cursor.getCount() >0;
        cursor.close();
        return exist;
    }

    public void insertOrUpdateChats(List<Chat> chats){
        for(Chat c: chats){
            if(exist(c.getChatId())){
                updateChat(c);
            }else{
                insertChat(c);
            }
        }
    }

    public void markAsRead(UUID chatId){
        ContentValues cv = new ContentValues();
        cv.put("last_read_at", DatabaseHelper.toDateString(new Date()));
        DatabaseHelper.getInstance().getWritableDatabase().update(TABLE, cv, "chat_id=?", new String[]{chatId.toString()});
    }
}
