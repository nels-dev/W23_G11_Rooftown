package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private static final String TAG = "LISTS";
    List<Post> posts;
    Context context;
    OnClickListener listClickedListener;

    public ListAdapter(List<Post> posts, Context context, OnClickListener listClickedListener) {
        this.posts = posts;
        this.context = context;
        this.listClickedListener = listClickedListener;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgViewList;
        public TextView txtViewListTitle;
        public TextView txtViewListDescription;
        public UUID postId;

        public ListViewHolder(@NonNull View itemView, OnClickListener listClickedListener){
            super(itemView);
            imgViewList = itemView.findViewById(R.id.imgViewList);
            txtViewListTitle = itemView.findViewById(R.id.txtViewListTitle);
            txtViewListDescription = itemView.findViewById(R.id.txtViewListDescription);

            this.itemView.setOnClickListener(v -> {
                if(getAdapterPosition()!= RecyclerView.NO_POSITION){
                    listClickedListener.onItemClicked(postId);
                }
            });
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ListViewHolder(view, listClickedListener);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.txtViewListTitle.setText(post.getLocation());
        holder.txtViewListDescription.setText(post.getRoomDescription());

        if (post.getRoomImage() !=null){
            ImageFileHelper.readImage(context, post.getRoomImage(), bitmap -> holder.imgViewList.setImageBitmap(bitmap));
        } else if (post.getInitiatorImage() != null){
            ImageFileHelper.readImage(context, post.getInitiatorImage(), bitmap -> holder.imgViewList.setImageBitmap(bitmap));
        } else {
            holder.imgViewList.setImageResource(0);
        }
        holder.postId = post.getPostId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void populatePosts(List<Post> posts){
        this.posts.clear();
        this.posts.addAll(posts);
        this.notifyDataSetChanged();
    }

    public static interface OnClickListener{
        void onItemClicked(UUID postId);
    }
}