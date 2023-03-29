package csis3175.w23.g11.rooftown.messages.data.local;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;

@Dao
public interface ChatDao {

    @Query("SELECT * FROM CHATS WHERE initiator = :userId ORDER BY last_activity_at DESC")
    List<Chat> getChatsByInitiator(String userId);

    @Query("SELECT * FROM CHATS WHERE counter_party = :userId ORDER BY last_activity_at DESC")
    List<Chat> getChatsByCounterParty(String userId);

    @Query("SELECT * FROM CHATS WHERE chatId = :chatId")
    Chat getChatById(UUID chatId);

    @Query("SELECT count(1) from CHATS where last_read_at is null or last_activity_at > last_read_at")
    int getNumberOfUnread();


    @Upsert
    void insertOrUpdateChats(List<Chat> chat);

    @Update
    void updateChat(Chat chat);


    @Query("update CHATS set last_activity_at=CURRENT_TIMESTAMP where chatId = :chatId")
    void markAsRead(UUID chatId);
}
