package csis3175.w23.g11.rooftown.messages.ui.view;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.messages.ui.adapter.ConversationAdapter;
import csis3175.w23.g11.rooftown.messages.ui.viewmodel.ChatViewModel;

public class ConversationFragment extends Fragment {

    public static final String ARG_CHAT_ID = "chat_id";
    private static final String TAG = "CHATS";
    private ChatViewModel viewModel;
    private EditText editTextChatMessage;

    public static ConversationFragment newInstance() {
        return new ConversationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        editTextChatMessage = view.findViewById(R.id.editTextChatMessage);
        if (null == getArguments() || null == this.getArguments().getString(ARG_CHAT_ID)) {
            Log.e(TAG, "No arguments passed to fragment. Expected " + ARG_CHAT_ID);
            return;
        }
        UUID chatId = UUID.fromString(this.getArguments().getString(ARG_CHAT_ID));
        RecyclerView recyclerViewMessages = view.findViewById(R.id.recyclerViewChatMessages);
        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());
        layout.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layout);
        ConversationAdapter adapter = new ConversationAdapter(new ArrayList<>(), view.getContext());
        recyclerViewMessages.setAdapter(adapter);
        viewModel.setSelectedChatId(chatId);
        viewModel.markChatAsRead();
        viewModel.getChatMessages().observe(this.getViewLifecycleOwner(), (messages) -> {
            adapter.setChatMessages(messages);
            viewModel.markChatAsRead(); // User is already reading the conversation
            try {
                recyclerViewMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
            } catch (Exception ignored) {
            }
        });
        Button btnSendMessage = view.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(this::sendMessage);
    }

    public void sendMessage(View view) {
        viewModel.sendMessage(editTextChatMessage.getText().toString());
        editTextChatMessage.setText(null);
    }
}