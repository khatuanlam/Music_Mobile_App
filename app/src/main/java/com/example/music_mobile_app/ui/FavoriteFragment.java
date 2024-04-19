package com.example.music_mobile_app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.MethodsManager;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerview;

    private TextView quantity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // //Hide header
        // RelativeLayout header =
        // getParentFragment().getView().findViewById(R.id.header);
        // header.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        prepareData(view);

        setFavoriteTracks();

        return view;
    }

    private void prepareData(View view) {
        quantity = view.findViewById(R.id.quantity);
        recyclerview = view.findViewById(R.id.favorite_recyclerview);
        LinearLayoutManager favorite_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(favorite_layout);
    }

    private void initView() {

    }

    private void setFavoriteTracks() {
        List<Track> trackList = ListManager.getInstance().getFavoriteTracks();
        if (trackList.isEmpty()) {
            // Nếu danh sách favorite chưa được lấy thì load lại để lấy
            MethodsManager.getInstance().getUserFavorite(true);
            Toast.makeText(getActivity(), "List is empty", Toast.LENGTH_SHORT).show();
        }
        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(), getParentFragment());
        adapter.notifyDataSetChanged();

        recyclerview.setAdapter(adapter);
    }
}
