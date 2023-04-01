package csis3175.w23.g11.rooftown.messages.ui.view;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.messages.ui.adapter.ConversationAdapter;
import csis3175.w23.g11.rooftown.messages.ui.viewmodel.ChatViewModel;
import csis3175.w23.g11.rooftown.posts.data.model.Post;

public class ConversationFragment extends Fragment {

    public static final String ARG_CHAT_ID = "chat_id";
    private static final String TAG = "CHATS";
    private ChatViewModel viewModel;
    private EditText editTextChatMessage;

    public static ConversationFragment newInstance() {
        return new ConversationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        editTextChatMessage = view.findViewById(R.id.editTextChatMessage);
        TextView txtViewPostTitle = view.findViewById(R.id.txtViewPostTitle);
        ImageView imgViewPostImage = view.findViewById(R.id.imgViewPostImage);
        if (null == getArguments() || null == this.getArguments().getString(ARG_CHAT_ID)) {
            Log.e(TAG, "No arguments passed to fragment. Expected " + ARG_CHAT_ID);
            return;
        }
        UUID chatId = UUID.fromString(this.getArguments().getString(ARG_CHAT_ID));
        RecyclerView recyclerViewMessages = view.findViewById(R.id.recyclerViewChatMessages);
        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());
        layout.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layout);
        ConversationAdapter adapter = new ConversationAdapter(new ArrayList<>());
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
        viewModel.getSelectedChat().observe(this.getViewLifecycleOwner(), (chatAndRelatedPost -> {
            Post relatedPost = chatAndRelatedPost.getRelatedPost();
            if (chatAndRelatedPost.getRelatedPost() != null) {
                txtViewPostTitle.setText(relatedPost.getDisplayTitle());
                if (relatedPost.getDisplayImage()!=null){
                    ImageFileHelper.readImage(this.getContext(), relatedPost.getDisplayImage(), (bitmap) -> imgViewPostImage.setImageBitmap(bitmap));
                }
            }
        }));
        view.findViewById(R.id.btnSendMessage).setOnClickListener(this::sendMessage);
    }

    public void sendMessage(View view) {
        viewModel.sendMessage(editTextChatMessage.getText().toString(), false);
        editTextChatMessage.setText(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuMessages).setChecked(true);
    }
}