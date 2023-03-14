package csis3175.w23.g11.rooftown.roommates.ui.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

import csis3175.w23.g11.rooftown.R;
import csis3175.w23.g11.rooftown.roommates.data.model.Post;
import csis3175.w23.g11.rooftown.roommates.ui.adapter.GridAdapter;

public class GridViewFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_grid_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerGrid = view.findViewById(R.id.recyclerGrid);
        recyclerGrid.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

        // Setup Adapter
        GridAdapter gridAdapter = new GridAdapter(postList);
        recyclerGrid.setAdapter(gridAdapter);

    }
}