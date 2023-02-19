package csis3175.w23.g11.rooftown.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;

public class AllChatsFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private RecyclerView recyclerViewChats;
    private TabLayout tabLayoutMessages;
    private AllChatsAdapter messagesAdapter;
    private static final String TAG = "ALL_CHATS";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Setup recyclerView
        recyclerViewChats = view.findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(view.getContext()));
        List<ChatDto> list = retrieveChats(false);
        messagesAdapter = new AllChatsAdapter(list, view.getContext(), this::onItemClicked);
        recyclerViewChats.setAdapter(messagesAdapter);

        //Setup tab
        tabLayoutMessages = view.findViewById(R.id.tabLayoutMessages);
        tabLayoutMessages.addOnTabSelectedListener(this);
    }

    private void onItemClicked(String chatId){
        // Start a chat window when item is pressed. chat id is given by the adapter's ViewHolder
        SingleChatFragment singleChatFragment = SingleChatFragment.newInstance();
        Bundle args = new Bundle();
        args.putString(SingleChatFragment.ARG_CHAT_ID, chatId);
        singleChatFragment.setArguments(args);

        FragmentTransaction tx = getParentFragmentManager().beginTransaction();
        tx.replace(R.id.mainContainer, singleChatFragment);
        tx.addToBackStack(TAG); // so that pressing "Back" button on android goes back to this fragment
        tx.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);
        bottomNav.getMenu().findItem(R.id.bottomNavMenuMessages).setChecked(true);
    }

    @NonNull
    private List<ChatDto> retrieveChats(boolean isIncoming) {
        if(isIncoming){
            ChatDto sendByOther = new ChatDto();
            sendByOther.setId(UUID.randomUUID().toString());
            sendByOther.setLastActivity(new Date());
            sendByOther.setLastMessage("Hi there");
            sendByOther.setNameOfCounterparty("Nelson");
            sendByOther.setRead(false);
            sendByOther.setLastActivityByCurrentUser(false);
            ChatDto sendByMyself = new ChatDto();
            sendByMyself.setId(UUID.randomUUID().toString());
            sendByMyself.setLastActivity(new Date(System.currentTimeMillis()-1000*3600*24));
            sendByMyself.setLastMessage("This is a long long long long long long long long message.");
            sendByMyself.setNameOfCounterparty("Peter");
            sendByMyself.setRead(false);
            sendByMyself.setLastActivityByCurrentUser(true);
            return new ArrayList<>(Arrays.asList(sendByOther, sendByMyself));
        }else{
            ChatDto sendByOther = new ChatDto();
            sendByOther.setId(UUID.randomUUID().toString());
            sendByOther.setLastActivity(new Date(System.currentTimeMillis()-1000*3700));
            sendByOther.setLastMessage("Yoyo");
            sendByOther.setNameOfCounterparty("Daniel");
            sendByOther.setRead(true);
            sendByOther.setLastActivityByCurrentUser(false);
            ChatDto sendByMyself = new ChatDto();
            sendByMyself.setId(UUID.randomUUID().toString());
            sendByMyself.setLastActivity(new Date(System.currentTimeMillis()-1000*3600*24));
            sendByMyself.setLastMessage("Hey man");
            sendByMyself.setNameOfCounterparty("Peter");
            sendByMyself.setRead(false);
            sendByMyself.setLastActivityByCurrentUser(true);
            ChatDto another = new ChatDto();
            another.setId(UUID.randomUUID().toString());
            another.setLastActivity(new Date(System.currentTimeMillis()-1000*3600*24));
            another.setLastMessage("Hey yo");
            another.setNameOfCounterparty("Peter");
            another.setRead(true);
            another.setLastActivityByCurrentUser(true);
            return new ArrayList<>(Arrays.asList(sendByOther, sendByMyself, another));
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition()==0){
            // Interest in
            messagesAdapter.populateMessages(retrieveChats(false));
        }else{
            // Interest by
            messagesAdapter.populateMessages(retrieveChats(true));
        }
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