package com.example.music_mobile_app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_mobile_app.R;
<<<<<<< HEAD
import com.example.music_mobile_app.manager.SearchManager.adapter.SearchTrackAdapter;
=======
import com.example.music_mobile_app.adapter.SearchTrackAdapter;
>>>>>>> origin/main

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SubSearchRecyclerViewFoundSongFragment extends Fragment {
    private RecyclerView recyclerView;
    public List<Track> trackList;
    private SearchTrackAdapter mAdapter;

    public SubSearchRecyclerViewFoundSongFragment(List<Track> trackList) {
        this.trackList = trackList;
    }

<<<<<<< HEAD
=======
    public SubSearchRecyclerViewFoundSongFragment() {
    }

>>>>>>> origin/main
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_search_recyler_view_found_song, container, false);

        recyclerView = view.findViewById(R.id.search_recyclerViewFoundSongs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchTrackAdapter(this, trackList);
        recyclerView.setAdapter(mAdapter);

<<<<<<< HEAD

=======
>>>>>>> origin/main
        return view;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        mAdapter.setmDataList(trackList);

    }
<<<<<<< HEAD
=======


>>>>>>> origin/main
}