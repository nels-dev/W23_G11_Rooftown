package csis3175.w23.g11.rooftown.messages;

import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;

public class AllChatsAdapter extends RecyclerView.Adapter<AllChatsAdapter.MessagesViewHolder> {
    private static final String TAG = "MESSAGES";
    List<ChatDto> chats;
    Context context;
    ChatItemClickedListener itemClickedListener;

    public AllChatsAdapter(List<ChatDto> chats,
                           Context context,
                           ChatItemClickedListener itemClickedListener){
        this.chats = chats;
        this.context = context;
        this.itemClickedListener = itemClickedListener;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a new ViewHolder and wraps the inflated item view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatlistitem, parent, false);
        return new MessagesViewHolder(view, itemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        // When the ViewHolder "window" is used to render the item at position
        ChatDto chat = chats.get(position);
        Log.d(TAG, "Binding viewholder to " + chat.getChatId());
        CharSequence timeSpan = DateUtils.getRelativeTimeSpanString(chat.getLastActivity().getTime());
        holder.txtViewChatItemTitle.setText(chat.getLastMessage());
        holder.txtViewChatItemContent.setText("Sent by " + (chat.isLastActivityByCurrentUser() ? "you" : chat.getNameOfCounterparty()) + " " + timeSpan);
        holder.chatId = chat.getChatId();
    }

    @Override
    public long getItemId(int position) {
        return NO_ID;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewChatItemTitle;
        public TextView txtViewChatItemContent;
        public ImageView imgViewChatItem;
        public String chatId;

        public MessagesViewHolder(@NonNull View itemView, ChatItemClickedListener itemClickedListener) {
            super(itemView);
            txtViewChatItemTitle = itemView.findViewById(R.id.txtViewChatItemTitle);
            txtViewChatItemContent = itemView.findViewById(R.id.txtViewChatItemContent);
            imgViewChatItem = itemView.findViewById(R.id.imgViewChatItem);
            this.itemView.setOnClickListener(v -> {
                if(getAdapterPosition()!=RecyclerView.NO_POSITION){
                    itemClickedListener.onClick(chatId);
                }
            });
        }
    }

    public void populateMessages(List<ChatDto> chats){
        Log.d(TAG, "Messages populated");
        this.chats.clear();
        this.chats.addAll(chats);
        this.notifyDataSetChanged();
    }

}
