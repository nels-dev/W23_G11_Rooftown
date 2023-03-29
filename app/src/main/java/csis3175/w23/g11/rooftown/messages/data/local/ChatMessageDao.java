package csis3175.w23.g11.rooftown.messages.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;

@Dao
public interface ChatMessageDao {


    @Query("SELECT * FROM CHAT_MESSAGE where chat_id=:chatId order by sent_at desc limit 1")
    ChatMessage getLatestChatMessageByChatId(UUID chatId);

    @Query("SELECT * FROM CHAT_MESSAGE where chat_id=:chatId")
    List<ChatMessage> getChatMessagesByChatId(UUID chatId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMessagesIfNotExist(List<ChatMessage> messages);
}
