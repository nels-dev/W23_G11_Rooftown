package csis3175.w23.g11.rooftown.messages.data.repository;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.local.ChatDao;
import csis3175.w23.g11.rooftown.messages.data.local.ChatMessageDao;
import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.messages.data.remote.ChatService;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;

public class ChatRepository {
    private static final String TAG = "CHATS";
    private final ChatDao chatDao;
    private final ChatMessageDao chatMessageDao;
    private final ChatService chatService;
    private final MutableLiveData<List<Chat>> outgoingChats = new MutableLiveData<>();
    private final MutableLiveData<List<Chat>> incomingChats = new MutableLiveData<>();
    private final MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>(new ArrayList<>());
    private volatile boolean loading = false;

    public ChatRepository() {
        chatDao = new ChatDao();
        chatService = new ChatService();
        chatMessageDao = new ChatMessageDao();
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

    public void loadAllChats() {
        Log.d(TAG, "Loading chats from database ");
        outgoingChats.setValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
        incomingChats.setValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
        Log.d(TAG, "Loading chats from remote");
        syncWithRemote();
    }

    public ListenerRegistration loadAndListenToMessages(UUID chatId) {
        chatMessages.setValue(chatMessageDao.getChatMessagesByChatId(chatId));
        return chatService.listenToChat(chatId, (messages) -> {
            chatMessageDao.insertMessagesIfNotExist(messages);
            chatMessages.postValue(chatMessageDao.getChatMessagesByChatId(chatId));

        });
    }

    public void syncWithRemote() {
        new Handler().post(() -> {
            if (!loading) {
                loading = true;
                chatService.loadChats(chats -> {
                    chatDao.insertOrUpdateChats(chats);
                    Log.d(TAG, "Received remote data, posting value to live data ");
                    outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
                    incomingChats.postValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
                    loading = false;
                });
            } else {
                Log.d(TAG, "Skip sync with remote, another thread is working on it");
            }
        });
    }

    public void sendMessage(UUID chatId, String content) {
        chatService.addMessageToChat(chatId, content);
    }

    public void markChatAsRead(UUID chatId) {
        chatDao.markAsRead(chatId);
    }


}
