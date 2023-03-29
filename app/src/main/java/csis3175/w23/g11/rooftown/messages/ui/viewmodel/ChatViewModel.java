package csis3175.w23.g11.rooftown.messages.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.messages.data.repository.ChatRepository;

public class ChatViewModel extends ViewModel {
    private final LiveData<List<Chat>> outgoingChats;
    private final LiveData<List<Chat>> incomingChats;
    private final LiveData<List<ChatMessage>> chatMessages;
    private final LiveData<Integer> numberOfUnread;
    private final ChatRepository chatRepository;
    private ListenerRegistration singleChatRegistration;
    private ListenerRegistration allChatsRegistration;

    private UUID selectedChatId;

    public ChatViewModel() {
        this.chatRepository = new ChatRepository();
        outgoingChats = chatRepository.getOutgoingChats();
        incomingChats = chatRepository.getIncomingChats();
        chatMessages = chatRepository.getChatMessages();
        numberOfUnread = chatRepository.getNumberOfUnread();
    }

    public LiveData<List<Chat>> getOutgoingChats() {
        return outgoingChats;
    }

    public LiveData<List<Chat>> getIncomingChats() {
        return incomingChats;
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    public LiveData<Integer> getNumberOfUnread() {
        return numberOfUnread;
    }

    public void loadData() {
        if (allChatsRegistration != null) {
            allChatsRegistration.remove();
        }
        allChatsRegistration = chatRepository.loadAndListenToChats();
    }

    public void setSelectedChatId(UUID chatId) {
        // Whenever the "listen to" chatId is changed,
        // the LiveData of ChatMessages has to be repopulated
        // and the previous listener has to be removed to avoid leakage
        if (singleChatRegistration != null) {
            singleChatRegistration.remove();
        }
        selectedChatId = chatId;
        singleChatRegistration = chatRepository.loadAndListenToMessages(chatId);
    }

    public void sendMessage(String content) {
        chatRepository.sendMessage(selectedChatId, content);
    }

    public void markChatAsRead() {
        chatRepository.markChatAsRead(selectedChatId);

    }
}