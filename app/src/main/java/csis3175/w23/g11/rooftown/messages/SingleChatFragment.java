package csis3175.w23.g11.rooftown.messages;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;

public class SingleChatFragment extends Fragment {

    public static final String ARG_CHAT_ID = "chat_id";
    private static final String TAG = "CHAT";
    private SingleChatViewModel mViewModel;
    private String chatId;
    private RecyclerView recyclerViewMessages;
    private FirebaseAuth mAuth;

    public static SingleChatFragment newInstance() {
        return new SingleChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(null==   getArguments() || null==this.getArguments().getString(ARG_CHAT_ID)){
            Log.e(TAG, "No arguments passed to fragment. Expected " + ARG_CHAT_ID);
            return;
        }
        chatId = this.getArguments().getString(ARG_CHAT_ID);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewChatMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(view.getContext()));
        String userId = mAuth.getUid();
        List<ChatMessageDto> chatMessageDtos = getChatMessages(chatId);
        recyclerViewMessages.setAdapter(new ChatMessagesAdapter(chatMessageDtos, view.getContext(), userId));
        Log.d(TAG, "Loading chat:" + chatId);
    }

    private List<ChatMessageDto> getChatMessages(String chatId) {
        ChatMessageDto fromSystem = new ChatMessageDto();
        fromSystem.setChatId(chatId);
        fromSystem.setChatMessageId(UUID.randomUUID().toString());
        fromSystem.setContent("You have indicated your interest!");
        fromSystem.setSentAt(new Date(System.currentTimeMillis() - 1000*1900));
        fromSystem.setSentBy(mAuth.getUid()); //by me

        ChatMessageDto fromPartner = new ChatMessageDto();
        fromPartner.setChatId(chatId);
        fromPartner.setChatMessageId(UUID.randomUUID().toString());
        fromPartner.setContent("Hi there this is a chat content, I want to make it a bit long to test wrapping");
        fromPartner.setSentAt(new Date(System.currentTimeMillis() - 1000*1800));
        fromPartner.setSentBy("123"); //not by me

        ChatMessageDto fromMe = new ChatMessageDto();
        fromMe.setChatId(chatId);
        fromMe.setChatMessageId(UUID.randomUUID().toString());
        fromMe.setContent("Ok received.");
        fromMe.setSentAt(new Date(System.currentTimeMillis() - 1000*300));
        fromMe.setSentBy(mAuth.getUid()); //by me

        return new ArrayList<>(Arrays.asList(fromSystem, fromPartner, fromMe));
    }
}