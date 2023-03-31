package csis3175.w23.g11.rooftown.messages.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import csis3175.w23.g11.rooftown.user.data.model.UserProfile;

public class ChatAndCounterParty {
    @Embedded
    Chat chat;
    @Relation(parentColumn = "counter_party", entityColumn = "userId")
    UserProfile counterParty;
    @Relation(parentColumn = "initiator", entityColumn = "userId")
    UserProfile initiator;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public UserProfile getCounterParty() {
        return counterParty;
    }

    public void setCounterParty(UserProfile counterParty) {
        this.counterParty = counterParty;
    }

    public UserProfile getInitiator() {
        return initiator;
    }

    public void setInitiator(UserProfile initiator) {
        this.initiator = initiator;
    }
}
