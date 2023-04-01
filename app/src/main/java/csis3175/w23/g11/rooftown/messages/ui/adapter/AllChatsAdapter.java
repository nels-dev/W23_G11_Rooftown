package csis3175.w23.g11.rooftown.messages.ui.adapter;

import static androidx.recyclerview.widget.RecyclerView.NO_ID;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.messages.data.model.ChatAndCounterParty;
import csis3175.w23.g11.rooftown.messages.ui.view.ChatItemClickedListener;

public class AllChatsAdapter extends RecyclerView.Adapter<AllChatsAdapter.MessagesViewHolder> {
    private static final String TAG = "CHATS";
    List<ChatAndCounterParty> chats;
    Context context;
    ChatItemClickedListener itemClickedListener;

    public AllChatsAdapter(List<ChatAndCounterParty> chats, Context context, ChatItemClickedListener itemClickedListener) {
        this.chats = chats;
        this.context = context;
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create a new ViewHolder and wraps the inflated item view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatlistitem, parent, false);
        return new MessagesViewHolder(view, itemClickedListener);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        ChatAndCounterParty chat = chats.get(position);
        holder.txtViewChatItemTitle.setText(chat.getChat().isInitiatedByMe() ? chat.getCounterParty().getUserName() : chat.getInitiator().getUserName());
        holder.txtViewChatItemContent.setText(chat.getChat().getLastMessage());
        holder.txtViewChatItemTime.setText(DateUtils.getRelativeTimeSpanString(chat.getChat().getLastActivityAt().getTime()));

        if (!chat.getChat().isRead()) {
            holder.txtViewChatItemTitle.setTypeface(Typeface.DEFAULT_BOLD);
            holder.txtViewChatItemContent.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.txtViewChatItemTitle.setTypeface(Typeface.DEFAULT);
            holder.txtViewChatItemContent.setTypeface(Typeface.DEFAULT);
        }
        String imageFile = chat.getChat().isInitiatedByMe() ? chat.getCounterParty().getImageFileName() : chat.getInitiator().getImageFileName();
        if (null != imageFile) {
            ImageFileHelper.readImage(context, imageFile, bitmap -> holder.imgViewChatItem.setImageBitmap(bitmap));
        }
        holder.chatId = chat.getChat().getChatId();
    }

    @Override
    public long getItemId(int position) {
        return NO_ID;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void populateMessages(List<ChatAndCounterParty> chats) {
        this.chats.clear();
        this.chats.addAll(chats);
        this.notifyDataSetChanged();
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewChatItemTitle;
        public TextView txtViewChatItemContent;
        public TextView txtViewChatItemTime;
        public ImageView imgViewChatItem;
        public UUID chatId;

        public MessagesViewHolder(@NonNull View itemView, ChatItemClickedListener itemClickedListener) {
            super(itemView);
            txtViewChatItemTitle = itemView.findViewById(R.id.txtViewChatItemTitle);
            txtViewChatItemContent = itemView.findViewById(R.id.txtViewChatItemContent);
            txtViewChatItemTime = itemView.findViewById(R.id.txtViewChatItemTime);
            imgViewChatItem = itemView.findViewById(R.id.imgViewChatItem);
            this.itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    itemClickedListener.onClick(chatId);
                }
            });
        }
    }

}
