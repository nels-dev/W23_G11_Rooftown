package csis3175.w23.g11.rooftown.messages.ui.adapter;

import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;
import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.CurrentUserHelper;
import csis3175.w23.g11.rooftown.messages.data.model.ChatMessage;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MessagesViewHolder> {
    private static final String TAG = "CHATS";
    List<ChatMessage> chatMessages;
    Context context;

    public ConversationAdapter(List<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a new ViewHolder and wraps the inflated item view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatmessage, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        // When the ViewHolder "window" is used to render the item at position
        ChatMessage chatMessage = chatMessages.get(position);

        if (chatMessage.isSystemMessage()) {
            holder.txtViewChatMessage.setText("[" + chatMessage.getContent() + "]");
            holder.txtViewChatMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            holder.txtViewChatMessage.setText(chatMessage.getContent());
            holder.txtViewChatMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        holder.layoutChatMessage.setLayoutDirection(getLayoutDirection(chatMessage));
    }

    private int getLayoutDirection(ChatMessage messageDto) {
        if (messageDto.getSentBy().equals(CurrentUserHelper.getCurrentUid())) {
            return LAYOUT_DIRECTION_LTR;
        } else {
            return LAYOUT_DIRECTION_RTL;
        }
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages.clear();
        this.chatMessages.addAll(chatMessages);
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return NO_ID;
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewChatMessage;
        public LinearLayout layoutChatMessage;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewChatMessage = itemView.findViewById(R.id.txtViewChatMessage);
            layoutChatMessage = itemView.findViewById(R.id.layoutChatMessage);
        }
    }

}
