package csis3175.w23.g11.rooftown.messages.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "CHAT_MESSAGE")
public class ChatMessage {

    @PrimaryKey
    @NonNull
    private UUID chatMessageId;
    @ColumnInfo(name = "chat_id")
    @NonNull
    private UUID chatId;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "sent_at")
    private Date sentAt;
    @ColumnInfo(name = "system_message")
    private boolean systemMessage;
    @ColumnInfo(name = "sent_by")
    private String sentBy;

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(UUID chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(boolean systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
