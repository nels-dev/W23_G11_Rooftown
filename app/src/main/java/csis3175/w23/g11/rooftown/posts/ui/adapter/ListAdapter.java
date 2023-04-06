package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.common.ImageFileHelper;
import csis3175.w23.g11.rooftown.databinding.LayoutListitemBinding;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    List<Post> posts;
    OnClickListener listClickedListener;

    public ListAdapter(List<Post> posts, OnClickListener listClickedListener) {
        this.posts = posts;
        this.listClickedListener = listClickedListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutListitemBinding binding = LayoutListitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ListViewHolder holder = new ListViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Post post = posts.get(position);
        if (post.getPostType() == PostType.ROOM) {
            holder.binding.txtViewListTitle.setText(post.getLocation());
            holder.binding.txtViewListDescription.setText(post.getRoomDescription());
            holder.binding.imgViewList.setImageResource(R.drawable.placeholder_room);
            if (post.getRoomImage() != null) {
                ImageFileHelper.readImage(holder.itemView.getContext(), post.getRoomImage(), bitmap -> holder.binding.imgViewList.setImageBitmap(bitmap));
            }
        } else if (post.getPostType() == PostType.PERSON) {
            holder.binding.txtViewListTitle.setText(post.getInitiatorName());
            holder.binding.txtViewListDescription.setText(post.getInitiatorDescription());
            if ("Male".equals(post.getInitiatorGender())) {
                holder.binding.imgViewList.setImageResource(R.drawable.placeholder_person_male);
            } else if ("Female".equals(post.getInitiatorGender())) {
                holder.binding.imgViewList.setImageResource(R.drawable.placeholder_person_female);
            } else {
                holder.binding.imgViewList.setImageResource(R.drawable.placeholder_person_general);
            }
            if (post.getInitiatorImage() != null) {
                ImageFileHelper.readImage(holder.itemView.getContext(), post.getInitiatorImage(), bitmap -> holder.binding.imgViewList.setImageBitmap(bitmap));
            }
        } else {
            holder.binding.txtViewListTitle.setText("");
            holder.binding.txtViewListDescription.setText("");
            holder.binding.imgViewList.setImageResource(0);
        }
        holder.binding.getRoot().setOnClickListener(view -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                listClickedListener.onItemClicked(post.getPostId());
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

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public LayoutListitemBinding binding;

        public ListViewHolder(@NonNull LayoutListitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}