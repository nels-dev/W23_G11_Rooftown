package csis3175.w23.g11.rooftown.messages.data.model;

import java.util.Date;
import java.util.UUID;

// Entity class
public class ChatMessage {
    private UUID chatId;
    private UUID chatMessageId;
    private String content;
    private Date sentAt;
    private boolean systemMessage;
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
