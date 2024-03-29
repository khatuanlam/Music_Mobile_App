package com.example.music_mobile_app.ui.mydatabase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.SearchTrackAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SubSearchRecyclerViewFoundSongFragment extends Fragment {
    private RecyclerView recyclerView;
    public List<Track> trackList;
    private SearchTrackAdapter mAdapter;

    public SubSearchRecyclerViewFoundSongFragment(List<Track> trackList) {
        this.trackList = trackList;
    }

    public SubSearchRecyclerViewFoundSongFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_sub_search_recyler_view_found_song, container, false);

        recyclerView = view.findViewById(R.id.mydb_search_recyclerViewFoundSongs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchTrackAdapter(this, trackList);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        mAdapter.setmDataList(trackList);

    }


}