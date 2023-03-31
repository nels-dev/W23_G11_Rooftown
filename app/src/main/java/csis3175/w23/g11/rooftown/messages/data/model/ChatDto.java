package csis3175.w23.g11.rooftown.messages.data.model;

import java.util.Date;
import java.util.List;

public class ChatDto {

    Date lastActivityAt;
    String lastActivityBy;
    String lastMessage;
    String relatedPost;
    List<ChatMessage> messages;
    List<String> participants;

    public Date getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Date lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public String getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(String lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getRelatedPost() {
        return relatedPost;
    }

    public void setRelatedPost(String relatedPost) {
        this.relatedPost = relatedPost;
    }

    public static class ChatMessage {
        String chatMessageId;
        Date sentAt;
        boolean systemMessage;
        String content;
        String sentBy;

        public String getChatMessageId() {
            return chatMessageId;
        }

        public void setChatMessageId(String chatMessageId) {
            this.chatMessageId = chatMessageId;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSentBy() {
            return sentBy;
        }

        public void setSentBy(String sentBy) {
            this.sentBy = sentBy;
        }
    }
}
