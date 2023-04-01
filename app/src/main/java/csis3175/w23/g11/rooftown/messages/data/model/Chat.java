package csis3175.w23.g11.rooftown.messages.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

import csis3175.w23.g11.rooftown.common.CurrentUserHelper;

@Entity(tableName = "CHATS")
public class Chat {
    @PrimaryKey
    @NonNull
    private UUID chatId;
    @ColumnInfo(name = "initiator")
    private String initiator;
    @ColumnInfo(name = "counter_party")
    private String counterParty;
    @ColumnInfo(name = "last_activity_at")
    private Date lastActivityAt;
    @ColumnInfo(name = "last_activity_by")
    private String lastActivityBy;
    @ColumnInfo(name = "last_message")
    private String lastMessage;
    @ColumnInfo(name = "last_read_at")
    private Date lastReadAt;
    @ColumnInfo(name = "related_post")
    private UUID relatedPost;

    public boolean isRead() {
        return isLastActivityByMe() || lastReadAt != null && lastActivityAt.before(lastReadAt);
    }

    public boolean isLastActivityByMe() {
        return lastActivityBy.equals(CurrentUserHelper.getCurrentUid());
    }

    public boolean isInitiatedByMe() {
        return initiator.equals(CurrentUserHelper.getCurrentUid());
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getCounterParty() {
        return counterParty;
    }

    public void setCounterParty(String counterParty) {
        this.counterParty = counterParty;
    }

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

    public Date getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Date lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public UUID getRelatedPost() {
        return relatedPost;
    }

    public void setRelatedPost(UUID relatedPost) {
        this.relatedPost = relatedPost;
    }
}
