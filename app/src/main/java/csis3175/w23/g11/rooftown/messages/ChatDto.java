package csis3175.w23.g11.rooftown.messages;

import java.util.Date;

public class ChatDto {

    private String chatId;

    private String nameOfCounterparty;

    private String lastMessage;

    private Date lastActivity;

    private boolean lastActivityByCurrentUser;

    private boolean read;

    public String getChatId() {
        return chatId;
    }

    public void setId(String chatId) {
        this.chatId = chatId;
    }

    public String getNameOfCounterparty() {
        return nameOfCounterparty;
    }

    public void setNameOfCounterparty(String nameOfCounterparty) {
        this.nameOfCounterparty = nameOfCounterparty;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public boolean isLastActivityByCurrentUser() {
        return lastActivityByCurrentUser;
    }

    public void setLastActivityByCurrentUser(boolean lastActivityByCurrentUser) {
        this.lastActivityByCurrentUser = lastActivityByCurrentUser;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


}
