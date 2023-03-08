package csis3175.w23.g11.rooftown.roommates.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import csis3175.w23.g11.rooftown.R;

public class ListAdapter extends BaseAdapter {

    //
    // depends on parent
    // output which view

    List<String> PostName;
    List<Integer> PostPics;

    public ListAdapter(List<String> postName, List<Integer> postPics) {
        PostName = postName;
        PostPics = postPics;
    }

    public List<String> getPostName() {
        return PostName;
    }

    public void setPostName(List<String> postName) {
        PostName = postName;
    }

    public List<Integer> getPostPics() {
        return PostPics;
    }

    public void setPostPics(List<Integer> postPics) {
        PostPics = postPics;
    }

    @Override
    public int getCount() {
        return PostName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listpost,viewGroup,false);
        }
        TextView txtViewPostItem = view.findViewById(R.id.txtViewPostItem);
        txtViewPostItem.setText(PostName.get(i));
        Drawable img = ContextCompat.getDrawable(viewGroup.getContext(),PostPics.get(i));
        img.setBounds(0,0,300,300);

        txtViewPostItem.setCompoundDrawables(null,null, img,null);
        txtViewPostItem.setCompoundDrawablePadding(8);
        txtViewPostItem.setGravity(Gravity.CENTER_VERTICAL);
        return null;
    }
}
