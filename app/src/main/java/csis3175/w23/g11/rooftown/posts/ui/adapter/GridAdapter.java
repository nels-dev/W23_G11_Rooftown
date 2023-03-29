package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    private static final String TAG = "POSTS";
    List<Post> posts;
    Context context;
    OnClickListener gridClickedListener;

    public GridAdapter(List<Post> posts, Context context, OnClickListener gridClickedListener) {
        this.posts = posts;
        this.context = context;
        this.gridClickedListener = gridClickedListener;
    }

    @NonNull
    @Override
    public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_griditem, parent, false);
        return new GridViewHolder(view, gridClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, int position) {
        Post post = posts.get(position);
        if (post.getPostType() == PostType.ROOM) {
            if (post.getRoomImage() != null) {
                ImageFileHelper.readImage(context, post.getRoomImage(), bitmap -> holder.imgViewGrid.setImageBitmap(bitmap));
            } else {
                holder.imgViewGrid.setImageResource(R.drawable.placeholder_room);
            }
        } else if (post.getPostType() == PostType.PERSON) {
            if (post.getInitiatorImage() != null) {
                ImageFileHelper.readImage(context, post.getInitiatorImage(), bitmap -> holder.imgViewGrid.setImageBitmap(bitmap));
            } else if (post.getInitiatorGender().equals("Male")) {
                holder.imgViewGrid.setImageResource(R.drawable.placeholder_person_male);
            } else if (post.getInitiatorGender().equals("Female")) {
                holder.imgViewGrid.setImageResource(R.drawable.placeholder_person_female);
            } else {
                holder.imgViewGrid.setImageResource(R.drawable.placeholder_person_general);
            }
        } else {
            holder.imgViewGrid.setImageResource(0);
        }
        holder.postId = post.getPostId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void populatePosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        this.notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClicked(UUID postId);
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgViewGrid;
        public UUID postId;

        public GridViewHolder(@NonNull View itemView, OnClickListener gridClickedListener) {
            super(itemView);
            imgViewGrid = itemView.findViewById(R.id.imgViewGrid);

            this.itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    gridClickedListener.onItemClicked(postId);
                }
            });
        }
    }
}
