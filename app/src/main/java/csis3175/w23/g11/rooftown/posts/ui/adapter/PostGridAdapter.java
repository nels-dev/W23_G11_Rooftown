package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.PostViewHolder> {
    private static final String TAG = "POSTS";
    List<Post> posts;
    Context context;
    public PostGridAdapter(List<Post> posts,
                           Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_postgriditem, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostGridAdapter.PostViewHolder holder, int position) {
        Post post = posts.get(position);
        if (post.getRoomImage() != null) {
            ImageFileHelper.readImage(context, post.getRoomImage(), bitmap -> holder.imgViewGridPost.setImageBitmap(bitmap));
        } else if (post.getInitiatorImage() != null) {
            ImageFileHelper.readImage(context, post.getInitiatorImage(), bitmap -> holder.imgViewGridPost.setImageBitmap(bitmap));
        } else {
            holder.imgViewGridPost.setImageResource(0);
        }
        holder.txtViewGridPost.setText(post.getLocation());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgViewGridPost;
        public TextView txtViewGridPost;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewGridPost = itemView.findViewById(R.id.imgViewGridPost);
            txtViewGridPost = itemView.findViewById(R.id.txtViewGridPost);
        }
    }

    public void populatePosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        this.notifyDataSetChanged();
    }
}
