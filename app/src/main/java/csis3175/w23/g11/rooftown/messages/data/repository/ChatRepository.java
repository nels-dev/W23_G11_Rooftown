package csis3175.w23.g11.rooftown.messages.data.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.common.AppDatabase;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.messages.data.local.ChatDao;
import csis3175.w23.g11.rooftown.messages.data.local.ChatMessageDao;
import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.messages.data.remote.ChatService;

public class ChatRepository {
    private static final String TAG = "CHATS";
    private final ChatDao chatDao;
    private final ChatMessageDao chatMessageDao;


    private final ChatService chatService;
    private final MutableLiveData<List<Chat>> outgoingChats = new MutableLiveData<>();
    private final MutableLiveData<List<Chat>> incomingChats = new MutableLiveData<>();
    private final MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> numberOfUnread = new MutableLiveData<>();

    public ChatRepository() {

        chatService = new ChatService();
        chatDao = AppDatabase.getInstance().chatDao();
        chatMessageDao = AppDatabase.getInstance().chatMessageDao();
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


    public MutableLiveData<Integer> getNumberOfUnread() {
        return numberOfUnread;
    }

    public ListenerRegistration loadAndListenToChats() {
        AsyncTask.execute(() -> {
            outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
            incomingChats.postValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
        });
        return chatService.listenToAllChats(this::remoteCallBackWithData);
    }

    public ListenerRegistration loadAndListenToMessages(UUID chatId) {
        AsyncTask.execute(() -> chatMessages.postValue(chatMessageDao.getChatMessagesByChatId(chatId)));
        return chatService.listenToChatMessages(chatId, (messages) -> {
            AsyncTask.execute(() -> {
                chatMessageDao.insertMessagesIfNotExist(messages);
                chatMessages.postValue(chatMessageDao.getChatMessagesByChatId(chatId));
            });
        });
    }

    public void remoteCallBackWithData(List<Chat> chats) {
        chatDao.insertOrUpdateChats(chats);
        Log.d(TAG, "Received remote data, posting value to live data ");
        outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
        incomingChats.postValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
        numberOfUnread.postValue(chatDao.getNumberOfUnread());
    }

    public void sendMessage(UUID chatId, String content) {
        AsyncTask.execute(() -> {
            ChatMessage newMessage = new ChatMessage();
            newMessage.setChatId(chatId);
            newMessage.setChatMessageId(UUID.randomUUID());
            newMessage.setContent(content);
            newMessage.setSentAt(new Date());
            newMessage.setSentBy(CurrentUserHelper.getCurrentUid());
            newMessage.setSystemMessage(false);
            chatMessageDao.insertMessagesIfNotExist(new ArrayList<>(Arrays.asList(newMessage)));

            Chat chat = chatDao.getChatById(chatId);
            chat.setLastActivityAt(newMessage.getSentAt());
            chat.setLastMessage(newMessage.getContent());
            chat.setLastActivityBy(newMessage.getSentBy());
            chatDao.updateChat(chat);

            chatService.addMessageToChat(newMessage);
        });
    }

    public void markChatAsRead(UUID chatId) {
        AsyncTask.execute(() -> {
            chatDao.markAsRead(chatId);
            numberOfUnread.postValue(chatDao.getNumberOfUnread());
            outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
            incomingChats.postValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
        });
    }


}
