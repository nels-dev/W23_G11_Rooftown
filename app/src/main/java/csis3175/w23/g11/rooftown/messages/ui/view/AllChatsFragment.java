package csis3175.w23.g11.rooftown.messages.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.messages.ui.adapter.AllChatsAdapter;
import csis3175.w23.g11.rooftown.messages.ui.viewmodel.ChatViewModel;

public class AllChatsFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "CHATS";
    private TabLayout tabLayoutMessages;
    private AllChatsAdapter messagesAdapter;
    private ChatViewModel viewModel;
    private int selectedTab;

    public static AllChatsFragment newInstance() {
        return new AllChatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(ChatViewModel.class);

        //Setup recyclerView
        RecyclerView recyclerViewChats = view.findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // Setup adapter, including re-populating messages if LiveData changes
        messagesAdapter = new AllChatsAdapter(new ArrayList<>(), view.getContext(), this::onItemClicked);
        recyclerViewChats.setAdapter(messagesAdapter);

        viewModel.getOutgoingChats().observe(this.getViewLifecycleOwner(), chats -> {
            if (tabLayoutMessages.getSelectedTabPosition() == 0) {
                messagesAdapter.populateMessages(chats);
            }
        });
        viewModel.getIncomingChats().observe(this.getViewLifecycleOwner(), chats -> {
            if (tabLayoutMessages.getSelectedTabPosition() == 1) {
                messagesAdapter.populateMessages(chats);
            }
        });

        //Setup tab
        tabLayoutMessages = view.findViewById(R.id.tabLayoutMessages);
        tabLayoutMessages.getTabAt(selectedTab).select();
        tabLayoutMessages.addOnTabSelectedListener(this);
    }

    private void onItemClicked(UUID chatId) {
        // Start a chat window when item is pressed. chat id is given by the adapter's ViewHolder
        ConversationFragment conversationFragment = ConversationFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(ConversationFragment.ARG_CHAT_ID, chatId.toString());
        conversationFragment.setArguments(args);

        FragmentTransaction tx = getParentFragmentManager().beginTransaction();
        tx.replace(R.id.mainContainer, conversationFragment);
        tx.addToBackStack(TAG); // so that pressing "Back" button on android goes back to this fragment
        tx.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuMessages).setChecked(true);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            // Interest in
            messagesAdapter.populateMessages(viewModel.getOutgoingChats().getValue());
        } else {
            // Interest by
            messagesAdapter.populateMessages(viewModel.getIncomingChats().getValue());
        }
        this.selectedTab = tab.getPosition();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // do nothing
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // do nothing
    }
}