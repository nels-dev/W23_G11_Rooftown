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
import csis3175.w23.g11.rooftown.common.CallbackListener;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.messages.data.local.ChatDao;
import csis3175.w23.g11.rooftown.messages.data.local.ChatMessageDao;
import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatAndCounterParty;
import csis3175.w23.g11.rooftown.messages.data.model.ChatAndRelatedPost;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.messages.data.remote.ChatService;
import csis3175.w23.g11.rooftown.user.data.repository.UserProfileRepository;

public class ChatRepository {
    private static final String TAG = "CHATS";
    private final ChatDao chatDao;
    private final ChatMessageDao chatMessageDao;
    private final UserProfileRepository userProfileRepository;

    private final ChatService chatService;
    private final MutableLiveData<List<ChatAndCounterParty>> outgoingChats = new MutableLiveData<>();
    private final MutableLiveData<List<ChatAndCounterParty>> incomingChats = new MutableLiveData<>();
    private final MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> numberOfUnread = new MutableLiveData<>();
    private final MutableLiveData<ChatAndRelatedPost> selectedChat = new MutableLiveData<>();

    public ChatRepository() {

        chatService = new ChatService();
        chatDao = AppDatabase.getInstance().chatDao();
        chatMessageDao = AppDatabase.getInstance().chatMessageDao();
        userProfileRepository = new UserProfileRepository();
    }

    public LiveData<List<ChatAndCounterParty>> getOutgoingChats() {
        return outgoingChats;
    }

    public LiveData<List<ChatAndCounterParty>> getIncomingChats() {
        return incomingChats;
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    public LiveData<ChatAndRelatedPost> getSelectedChat() {
        return selectedChat;
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
        for (Chat c : chats) {
            if (chatDao.exist(c.getChatId())) {
                chatDao.updateChat(c.getChatId(), c.getLastActivityAt(), c.getLastActivityBy(), c.getLastMessage());
            } else {
                chatDao.insertChat(c);
            }
        }
        Log.d(TAG, "Received remote data, posting value to live data ");
        outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
        incomingChats.postValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
        numberOfUnread.postValue(chatDao.getNumberOfUnread(CurrentUserHelper.getCurrentUid()));
    }


    public void markChatAsRead(UUID chatId) {
        AsyncTask.execute(() -> {
            chatDao.markAsRead(new Date(), chatId);
            numberOfUnread.postValue(chatDao.getNumberOfUnread(CurrentUserHelper.getCurrentUid()));
            outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
            incomingChats.postValue(chatDao.getChatsByCounterParty(CurrentUserHelper.getCurrentUid()));
        });
    }

    public void loadChatAndRelatedPost(UUID chatId) {
        AsyncTask.execute(() -> {
            ChatAndRelatedPost chatAndRelatedPost = chatDao.getChatAndRelatedPost(chatId);
            selectedChat.postValue(chatAndRelatedPost);
        });
    }

    public void sendMessage(UUID chatId, String content, boolean isSystemMessage) {
        AsyncTask.execute(() -> {
            ChatMessage newMessage = new ChatMessage();
            newMessage.setChatId(chatId);
            newMessage.setChatMessageId(UUID.randomUUID());
            newMessage.setContent(content);
            newMessage.setSentAt(new Date());
            newMessage.setSentBy(CurrentUserHelper.getCurrentUid());
            newMessage.setSystemMessage(isSystemMessage);
            chatMessageDao.insertMessagesIfNotExist(Arrays.asList(newMessage));
            chatDao.updateChat(chatId, newMessage.getSentAt(), newMessage.getSentBy(), newMessage.getContent());
            chatService.addMessageToChat(newMessage);
        });
    }

    /*
        In this overloaded version of send message, no chatId is given.
        Instead, look for an existing chat and skip sending if found.
        Start a new chat if not found.
     */
    public void sendMessage(String counterParty, UUID postId, String messageContent, boolean isSystemMessage, CallbackListener<UUID> callback) {
        AsyncTask.execute(() -> {
            List<ChatAndCounterParty> existingChats = chatDao.getChatsByCounterParty(counterParty);
            if (null != existingChats && !existingChats.isEmpty()) {
                UUID chatId = existingChats.get(0).getChat().getChatId();
                callback.callback(chatId);
            } else {
                Chat chat = new Chat();
                chat.setInitiator(CurrentUserHelper.getCurrentUid());
                chat.setCounterParty(counterParty);
                chat.setChatId(UUID.randomUUID());
                chat.setLastActivityBy(CurrentUserHelper.getCurrentUid());
                chat.setLastActivityAt(new Date());
                chat.setLastMessage(messageContent);
                chat.setRelatedPost(postId);
                chatDao.insertChat(chat);

                ChatMessage newMessage = new ChatMessage();
                newMessage.setChatId(chat.getChatId());
                newMessage.setChatMessageId(UUID.randomUUID());
                newMessage.setContent(messageContent);
                newMessage.setSentAt(new Date());
                newMessage.setSentBy(CurrentUserHelper.getCurrentUid());
                newMessage.setSystemMessage(isSystemMessage);
                List<ChatMessage> messages = Arrays.asList(newMessage);
                chatMessageDao.insertMessagesIfNotExist(messages);

                chatService.saveChat(chat, messages);

                //Ensure that counter party's profile is loaded
                userProfileRepository.loadUsers(Arrays.asList(chat.getCounterParty()), (Void noVal) -> {
                    outgoingChats.postValue(chatDao.getChatsByInitiator(CurrentUserHelper.getCurrentUid()));
                    callback.callback(chat.getChatId());
                });
            }

        });
    }
}
