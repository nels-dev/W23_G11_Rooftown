package csis3175.w23.g11.rooftown.messages.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import csis3175.w23.g11.rooftown.messages.data.model.Chat;
import csis3175.w23.g11.rooftown.messages.data.model.ChatDto;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;
import csis3175.w23.g11.rooftown.user.data.repository.UserProfileRepository;
import csis3175.w23.g11.rooftown.util.CurrentUserHelper;

/**
 * This service class is responsible for talking to Firestore
 * including retrieving, observing and saving documents
 */
public class ChatService {
    private static final String TAG = "CHATS";
    public static final String COLLECTION_CHAT = "CHATS";
    private final FirebaseFirestore fs;
    private final UserProfileRepository userProfileRepository;
    private final Query allChats;
    
    public ChatService(){
        fs = FirebaseFirestore.getInstance();
        userProfileRepository = new UserProfileRepository();
        allChats = fs.collection(COLLECTION_CHAT)
                .whereArrayContains("participants", CurrentUserHelper.getCurrentUid());
    }

    public void loadChats(ListUpdateListener<Chat> resultConsumer){
        allChats.get()
                .addOnCompleteListener(task -> {
                    List<DocumentSnapshot> resultList = task.getResult().getDocuments();
                    List<Chat> chats = new ArrayList<>();
                    for(DocumentSnapshot doc: resultList){
                        chats.add(toChat(doc));
                    }

                    //Make sure all users are loaded
                    Set<String> userIds = extractAllUsers(chats);
                    userProfileRepository.loadUsers(new ArrayList<>(userIds), ()-> {
                        for(Chat c: chats){
                            c.setPartnerName(c.getInitiator().equals(CurrentUserHelper.getCurrentUid())
                                    ? userProfileRepository.getUserName(c.getCounterParty())
                                    : userProfileRepository.getUserName(c.getInitiator()));
                        }
                        resultConsumer.onListUpdated(chats);
                    });
                });
    }

    private Set<String> extractAllUsers(List<Chat> chats) {

        Set<String> userIds = new HashSet<>();
        for(Chat c: chats){
            userIds.add(c.getInitiator());
            userIds.add(c.getCounterParty());
        }
        return userIds;
    }

    public ListenerRegistration listenToChat(UUID chatId, ListUpdateListener<ChatMessage> resultConsumer){
        return fs.collection(COLLECTION_CHAT).document(chatId.toString())
                .addSnapshotListener(MetadataChanges.EXCLUDE,
                (value, error) -> {
                    if(value!=null) {
                        Log.d(TAG, "Chat has changed. Extracting messages");
                        List<ChatMessage> messages = getMessages(value);
                        resultConsumer.onListUpdated(messages);
                    }
                });
    }

    public void saveChat(Chat chat, List<ChatMessage> messages){
        Log.d(TAG, ">> Invoke save chat to firestore: " + chat.getChatId().toString());
        ChatDto dto = toDto(chat, messages);
        fs.collection(COLLECTION_CHAT)
                .document(chat.getChatId().toString())
                .set(dto)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Chat document saved");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Cannot save document", e);
                });
    }

    private ChatDto toDto(Chat chat, List<ChatMessage> messages){
        List<ChatDto.ChatMessage> messageDtos = new ArrayList<>();
        if(messages!=null && !messages.isEmpty()){
            for(ChatMessage cm: messages){
                ChatDto.ChatMessage mdto = new ChatDto.ChatMessage();
                mdto.setChatMessageId(cm.getChatMessageId().toString());
                mdto.setContent(cm.getContent());
                mdto.setSentAt(cm.getSentAt());
                mdto.setSentBy(cm.getSentBy());
                mdto.setSystemMessage(cm.isSystemMessage());
                messageDtos.add(mdto);
            }
        }
        ChatDto dto = new ChatDto();
        dto.setLastActivityAt(chat.getLastActivityAt());
        dto.setLastMessage(chat.getLastMessage());
        dto.setLastActivityBy(chat.getLastActivityBy());
        dto.setParticipants(new ArrayList<>(Arrays.asList(chat.getInitiator(), chat.getCounterParty())));
        dto.setMessages(messageDtos);
        return dto;
    }

    private List<ChatMessage> getMessages(DocumentSnapshot doc){
        List<ChatMessage> result = new ArrayList<>();
        ChatDto dto = doc.toObject(ChatDto.class);
        if(dto.getMessages()!=null){
            for(ChatDto.ChatMessage m: dto.getMessages()){
                ChatMessage message = new ChatMessage();
                message.setChatMessageId(UUID.fromString(m.getChatMessageId()));
                message.setSystemMessage(m.isSystemMessage());
                message.setSentBy(m.getSentBy());
                message.setContent(m.getContent());
                message.setSentAt(m.getSentAt());
                message.setChatId(UUID.fromString(doc.getId()));
                result.add(message);
            }
        }
        return result;
    }

    private Chat toChat(DocumentSnapshot doc){
        ChatDto dto = doc.toObject(ChatDto.class);
        Chat chat = new Chat();
        chat.setChatId(UUID.fromString(doc.getId()));
        chat.setLastActivityAt(dto.getLastActivityAt());
        chat.setLastActivityBy(dto.getLastActivityBy());
        chat.setLastMessage(dto.getLastMessage());
        chat.setInitiator(dto.getParticipants().get(0));
        chat.setCounterParty(dto.getParticipants().get(1));
        return chat;
    }

    public interface ListUpdateListener<T> {
        void onListUpdated(List<T> list);
    }

    public void addMessageToChat(UUID chatId, String messageContent){
        fs.collection(COLLECTION_CHAT).document(chatId.toString())
                .get()
                .addOnSuccessListener((doc)->{
                    Chat chat = toChat(doc);
                    List<ChatMessage> messages = getMessages(doc);
                    ChatMessage newMessage = new ChatMessage();
                    newMessage.setChatId(chatId);
                    newMessage.setChatMessageId(UUID.randomUUID());
                    newMessage.setContent(messageContent);
                    newMessage.setSentAt(new Date());
                    newMessage.setSentBy(CurrentUserHelper.getCurrentUid());
                    newMessage.setSystemMessage(false);
                    messages.add(newMessage);
                    chat.setLastActivityAt(newMessage.getSentAt());
                    chat.setLastMessage(newMessage.getContent());
                    chat.setLastActivityBy(newMessage.getSentBy());
                    saveChat(chat, messages);
                });
    }
}
