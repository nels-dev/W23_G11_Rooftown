package csis3175.w23.g11.rooftown.messages.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatAndCounterParty;
import csis3175.w23.g11.rooftown.messages.data.model.ChatAndRelatedPost;

@Dao
public interface ChatDao {

    @Transaction
    @Query("SELECT * FROM CHATS WHERE initiator = :userId ORDER BY last_activity_at DESC")
    List<ChatAndCounterParty> getChatsByInitiator(String userId);

    @Transaction
    @Query("SELECT * FROM CHATS WHERE counter_party = :userId ORDER BY last_activity_at DESC")
    List<ChatAndCounterParty> getChatsByCounterParty(String userId);


    @Query("SELECT count(1) from CHATS where last_activity_by <> :myUserId AND (last_read_at is null or last_activity_at > last_read_at)")
    int getNumberOfUnread(String myUserId);

    @Transaction
    @Query("SELECT * FROM CHATS WHERE chatId=:chatId")
    ChatAndRelatedPost getChatAndRelatedPost(UUID chatId);

    @Insert
    void insertChat(Chat chat);

    @Query("update CHATS set last_activity_at=:lastActivityAt, last_activity_by=:lastActivityBy, last_message=:lastMessage where chatId=:chatId")
    void updateChat(UUID chatId, Date lastActivityAt, String lastActivityBy, String lastMessage);

    @Query("select count(1)>0 from CHATS where chatId=:chatId")
    boolean exist(UUID chatId);


    @Query("update CHATS set last_read_at=:lastReadAt where chatId = :chatId")
    void markAsRead(Date lastReadAt, UUID chatId);
}
