package csis3175.w23.g11.rooftown.messages;

import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;
import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MessagesViewHolder> {
    private static final String TAG = "CHAT MESSAGES";
    List<ChatMessageDto> chatMessages;
    Context context;
    String currentUserId;

    public ChatMessagesAdapter(List<ChatMessageDto> chatMessages,
                               Context context,
                               String currentUserId){
        this.chatMessages = chatMessages;
        this.context = context;
        this.currentUserId = currentUserId;
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
        ChatMessageDto chatMessage = chatMessages.get(position);
        Log.d(TAG, "Binding viewholder to " + chatMessage.getChatMessageId());
        holder.txtViewChatMessage.setText(getMessageContent(chatMessage));
        if(chatMessage.isSystemMessage()) {
            holder.txtViewChatMessage.setTextColor(context.getResources().getColor(R.color.md_theme_light_onSurface));
        }
        holder.layoutChatMessage.setLayoutDirection(getLayoutDirection(chatMessage));
    }

    private String getMessageContent(ChatMessageDto messageDto){
        if(messageDto.isSystemMessage()){
            return "[" + messageDto + "]";
        }else{
            return messageDto.getContent();
        }
    }

    private int getLayoutDirection(ChatMessageDto messageDto){
        if (messageDto.getSentBy().equals(currentUserId)){
            return LAYOUT_DIRECTION_LTR;
        }else{
            return LAYOUT_DIRECTION_RTL;
        }
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
