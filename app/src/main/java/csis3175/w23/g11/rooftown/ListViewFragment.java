package csis3175.w23.g11.rooftown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

public class ListViewFragment extends Fragment {

    private List<LatLng> locationList = Arrays.asList(
            new LatLng(49.203711118998456, -122.91276244392093),  // NW campus
            new LatLng(49.20506401434402, -122.91321305502117),  // Place 1
            new LatLng(49.20070804313903, -122.9180578868112), // Place 2
            new LatLng(49.2263531659175, -122.99951733755651)   // Place 3
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        ListView listViewPost = rootView.findViewById(R.id.listViewPost);
        ArrayAdapter<LatLng> siteArrAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,locationList);
        listViewPost.setAdapter(siteArrAdapter);




        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }


}