package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.databinding.LayoutGriditemBinding;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    List<Post> posts;
    OnClickListener gridClickedListener;

    public GridAdapter(List<Post> posts, OnClickListener gridClickedListener) {
        this.posts = posts;
        this.gridClickedListener = gridClickedListener;
    }

    @NonNull
    @Override
    public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutGriditemBinding binding = LayoutGriditemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GridViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, int position) {
        Post post = posts.get(position);

        if (post.getPostType() == PostType.ROOM) {
            holder.binding.imgViewGrid.setImageResource(0);
            if (post.getRoomImage() != null) {
                ImageFileHelper.readImage(holder.itemView.getContext(), post.getRoomImage(), bitmap -> holder.binding.imgViewGrid.setImageBitmap(bitmap));
            } else {
                holder.binding.imgViewGrid.setImageResource(R.drawable.placeholder_room);
            }
        } else if (post.getPostType() == PostType.PERSON) {
            holder.binding.imgViewGrid.setImageResource(0);
            if (post.getInitiatorImage() != null) {
                ImageFileHelper.readImage(holder.itemView.getContext(), post.getInitiatorImage(), bitmap -> holder.binding.imgViewGrid.setImageBitmap(bitmap));
            } else {
                if ("Male".equals(post.getInitiatorGender())) {
                    holder.binding.imgViewGrid.setImageResource(R.drawable.placeholder_person_male);
                } else if ("Female".equals(post.getInitiatorGender())) {
                    holder.binding.imgViewGrid.setImageResource(R.drawable.placeholder_person_female);
                } else {
                    holder.binding.imgViewGrid.setImageResource(R.drawable.placeholder_person_general);
                }
            }
        } else {
            holder.binding.imgViewGrid.setImageResource(0);
        }
        holder.binding.getRoot().setOnClickListener(view -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                gridClickedListener.onItemClicked(post.getPostId());
            }
        });
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
        public LayoutGriditemBinding binding;

        public GridViewHolder(@NonNull LayoutGriditemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
