package csis3175.w23.g11.rooftown.roommates.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.roommates.data.model.Post;
import csis3175.w23.g11.rooftown.roommates.ui.adapter.ListAdapter;

public class ListViewFragment extends Fragment {

    private List<Post> postList = Arrays.asList(
            new Post("Douglas College", new LatLng(49.203711118998456, -122.91276244392093), "This is Douglas College", R.drawable.place1),
            new Post("Metrotown", new LatLng(49.20506401434402, -122.91321305502117), "This is Metrotown", R.drawable.place2),
            new Post("Vancouver", new LatLng(49.20070804313903, -122.9180578868112), "This is Vancouver", R.drawable.place3),
            new Post("New Westminster", new LatLng(49.2263531659175, -122.99951733755651), "This is West Minster", R.drawable.place4),
            new Post("Douglas College", new LatLng(49.203711118998456, -122.91276244392093), "This is Douglas College", R.drawable.place1),
            new Post("Metrotown", new LatLng(49.20506401434402, -122.91321305502117), "This is Metrotown", R.drawable.place2),
            new Post("Vancouver", new LatLng(49.20070804313903, -122.9180578868112), "This is Vancouver", R.drawable.place3),
            new Post("New Westminster", new LatLng(49.2263531659175, -122.99951733755651), "This is West Minster", R.drawable.place4),
            new Post("Douglas College", new LatLng(49.203711118998456, -122.91276244392093), "This is Douglas College", R.drawable.place1),
            new Post("Metrotown", new LatLng(49.20506401434402, -122.91321305502117), "This is Metrotown", R.drawable.place2),
            new Post("Vancouver", new LatLng(49.20070804313903, -122.9180578868112), "This is Vancouver", R.drawable.place3),
            new Post("New Westminster", new LatLng(49.2263531659175, -122.99951733755651), "This is West Minster", R.drawable.place4),
            new Post("Douglas College", new LatLng(49.203711118998456, -122.91276244392093), "This is Douglas College", R.drawable.place1),
            new Post("Metrotown", new LatLng(49.20506401434402, -122.91321305502117), "This is Metrotown", R.drawable.place2),
            new Post("Vancouver", new LatLng(49.20070804313903, -122.9180578868112), "This is Vancouver", R.drawable.place3),
            new Post("New Westminster", new LatLng(49.2263531659175, -122.99951733755651), "This is West Minster", R.drawable.place4),
            new Post("Douglas College", new LatLng(49.203711118998456, -122.91276244392093), "This is Douglas College", R.drawable.place1),
            new Post("Metrotown", new LatLng(49.20506401434402, -122.91321305502117), "This is Metrotown", R.drawable.place2),
            new Post("Vancouver", new LatLng(49.20070804313903, -122.9180578868112), "This is Vancouver", R.drawable.place3),
            new Post("New Westminster", new LatLng(49.2263531659175, -122.99951733755651), "This is West Minster", R.drawable.place4)
    );


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
//        ListView listViewPost = rootView.findViewById(R.id.listViewPost);
//        ArrayAdapter<LatLng> siteArrAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,locationList);
//        listViewPost.setAdapter(siteArrAdapter)

        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        ListView listView = view.findViewById(R.id.listViewPost);

        ListAdapter listAdapter = new ListAdapter(postList);

        listView.setAdapter(listAdapter);

        return view;
    }


}