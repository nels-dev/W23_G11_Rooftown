package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    List<Post> posts;
    Context context;

    public GridAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgViewGrid;

        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewGrid = itemView.findViewById(R.id.imgViewGrid);

//            this.itemView.setOnClickListener(v -> {
//                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
//                    itemClickedListener.onClick(chatId);
//                }
//            });
        }
    }

    @NonNull
    @Override
    public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_griditem, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, int position) {
        Post post = posts.get(position);
        if (post.getRoomImage() != null) {
            ImageFileHelper.readImage(context, post.getRoomImage(), bitmap -> holder.imgViewGrid.setImageBitmap(bitmap));
        } else if (post.getInitiatorImage() != null) {
            ImageFileHelper.readImage(context, post.getInitiatorImage(), bitmap -> holder.imgViewGrid.setImageBitmap(bitmap));
        } else {
            holder.imgViewGrid.setImageResource(0);
        }
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
}
