package csis3175.w23.g11.rooftown.messages.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.util.DatabaseHelper;

public class ChatMessageDao {

    private static final String TABLE = "CHAT_MESSAGE";


    public ChatMessage getLatestChatMessageByChatId(UUID chatId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().rawQuery(
                "select chat_id, chat_message_id, content, sent_at, system_message, sent_by" +
                    " from CHAT_MESSAGE where chat_id=? order by sent_at desc limit 1", new String[]{chatId.toString()});
        List<Chat> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            ChatMessage cm = getData(cursor);
            cursor.close();
            return cm;
        }else{
            cursor.close();
            return null;
        }
    }

    public List<ChatMessage> getChatMessagesByChatId(UUID chatId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase().rawQuery(
                "select chat_id, chat_message_id, content, sent_at, system_message, sent_by" +
                        " from CHAT_MESSAGE where chat_id=?", new String[]{chatId.toString()});
        List<ChatMessage> result = new ArrayList<>();
        while(cursor.moveToNext()){
            result.add(getData(cursor));
        }
        cursor.close();
        return result;
    }

    private ChatMessage getData(Cursor cursor){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(UUID.fromString(cursor.getString(0)));
        chatMessage.setChatMessageId(UUID.fromString(cursor.getString(1)));
        chatMessage.setContent(cursor.getString(2));
        chatMessage.setSentAt(DatabaseHelper.fromDateString(cursor.getString(3)));
        chatMessage.setSystemMessage(cursor.getInt(4)==1);
        chatMessage.setSentBy(cursor.getString(5));
        return chatMessage;
    }

    private boolean exist(UUID chatId){
        Cursor cursor = DatabaseHelper.getInstance().getReadableDatabase()
                .query(TABLE, new String[]{"chat_message_id"}, "chat_message_id=?", new String[]{chatId.toString()}, null, null, null );
        boolean exist = cursor.getCount() >0;
        cursor.close();
        return exist;
    }

    public void insertMessagesIfNotExist(List<ChatMessage> messages){
        for(ChatMessage cm: messages){
            if(!exist(cm.getChatMessageId())){
                insertMessage(cm);
            }
        }

    }

    public void insertMessage(ChatMessage message){
        ContentValues cv = new ContentValues();
        cv.put("chat_message_id", message.getChatMessageId().toString());
        cv.put("content", message.getContent());
        cv.put("chat_id", message.getChatId().toString());
        cv.put("sent_at", DatabaseHelper.toDateString(message.getSentAt()));
        cv.put("sent_by", message.getSentBy());
        cv.put("system_message", message.isSystemMessage() ? 1:0);
        DatabaseHelper.getInstance().getWritableDatabase().insert(TABLE, null, cv);
    }
}
