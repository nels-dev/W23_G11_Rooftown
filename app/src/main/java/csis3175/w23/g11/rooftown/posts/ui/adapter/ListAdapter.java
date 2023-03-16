package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.posts.data.model.Post;
import csis3175.w23.g11.rooftown.posts.data.model.PostType;
import csis3175.w23.g11.rooftown.util.ImageFileHelper;

public class ListAdapter extends BaseAdapter {

    //
    // depends on parent
    // output which view
    private List<Post> postList;
    Context context;

//    List<String> PostName;
//    List<Integer> PostPics;

    public ListAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Post getItem(int i) {
        return postList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_postitem,parent,false);
        }
        // Get the Post object for the current position
        Post post = postList.get(i);

        TextView listViewTitle = convertView.findViewById(R.id.txtViewPostItem);
        ImageView imgViewPost = convertView.findViewById(R.id.imgViewPostItem);
        TextView txtViewPostDescription = convertView.findViewById(R.id.txtViewPostDescription);
        if (post.getPostType() == PostType.ROOM) {
            ImageFileHelper.readImage(context, post.getRoomImage(), imgViewPost::setImageBitmap);
            listViewTitle.setText(post.getLocation());
            txtViewPostDescription.setText(String.format("%s, %s", post.getCity(), post.getCountry()));
        } else if (post.getPostType() == PostType.PERSON) {
            ImageFileHelper.readImage(context, post.getInitiatorImage(), imgViewPost::setImageBitmap);
            listViewTitle.setText(post.getInitiatorName());
            txtViewPostDescription.setText(String.format("%s (%s, %s)", post.getLocation(), post.getInitiatorGender(), post.getInitiatorAge()));
        } else {
            imgViewPost.setImageResource(0);
            listViewTitle.setText("");
            txtViewPostDescription.setText("");
        }

        return convertView;
    }

    public void populatePosts(List<Post> posts) {
        this.postList.clear();
        this.postList.addAll(posts);
        this.notifyDataSetChanged();
    }
}