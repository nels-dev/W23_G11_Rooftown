package csis3175.w23.g11.rooftown.roommates.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.roommates.data.model.Post;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    List<Post> posts;

    public GridAdapter(List<Post> posts) {
        this.posts = posts;
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
        holder.imgViewGrid.setImageResource(post.getPostPic());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
