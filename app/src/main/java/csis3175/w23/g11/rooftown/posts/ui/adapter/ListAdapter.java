package csis3175.w23.g11.rooftown.posts.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.roommates.data.model.Post;

public class ListAdapter extends BaseAdapter {

    //
    // depends on parent
    // output which view
    private List<Post> postList;

//    List<String> PostName;
//    List<Integer> PostPics;

    public ListAdapter(List<Post> postList) {
        this.postList = postList;
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
        listViewTitle.setText(post.getTitle());

        ImageView imgViewPost = convertView.findViewById(R.id.imgViewPostItem);
        imgViewPost.setImageResource(post.getPostPic());

        TextView txtViewPostDescription = convertView.findViewById(R.id.txtViewPostDescription);
        txtViewPostDescription.setText(post.getPostDescription());

        return convertView;
    }
}