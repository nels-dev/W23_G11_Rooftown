package csis3175.w23.g11.rooftown.messages.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "CHATS")
public class Chat {
    @PrimaryKey
    @NonNull
    private UUID chatId;
    @ColumnInfo(name = "initiator")
    private String initiator;
    @ColumnInfo(name = "counter_party")
    private String counterParty;
    @ColumnInfo(name = "partner_name")
    private String partnerName;
    @ColumnInfo(name = "partner_image")
    private String partnerImage;
    @ColumnInfo(name = "last_activity_at")
    private Date lastActivityAt;
    @ColumnInfo(name = "last_activity_by")
    private String lastActivityBy;
    @ColumnInfo(name = "last_message")
    private String lastMessage;
    @ColumnInfo(name = "last_read_at")
    private Date lastReadAt;

    public String getPartnerImage() {
        return partnerImage;
    }

    public void setPartnerImage(String partnerImage) {
        this.partnerImage = partnerImage;
    }

    public boolean isRead() {
        return lastReadAt != null && lastActivityAt.before(lastReadAt);
    }

    public boolean isLastActivityByMe() {
        return lastActivityBy.equals(FirebaseAuth.getInstance().getUid());
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

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public Date getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Date lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}
