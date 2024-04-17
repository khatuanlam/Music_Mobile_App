package com.example.music_mobile_app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.CombinedRecyclerViewAdapter;
import com.example.music_mobile_app.adapter.SearchAlbumAdapter;
import com.example.music_mobile_app.adapter.SearchArtistAdapter;
import com.example.music_mobile_app.adapter.SearchTrackAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class SubSearchRecyclerViewFoundSongFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewArtist;
    private RecyclerView recyclerViewAlbum;

    public List<Track> trackList;
    public List<Artist> artistList;
    public List<AlbumSimple> albumList;
    private SearchTrackAdapter mAdapter;
    private SearchArtistAdapter mAdapterArtist;
    private SearchAlbumAdapter mAdapterALbum;

    private CombinedRecyclerViewAdapter mCombinedRecyclerViewAdapter;

    public SubSearchRecyclerViewFoundSongFragment() {
        this.trackList = new ArrayList<>();
        this.albumList = new ArrayList<>();
        this.artistList = new ArrayList<>();
    }

    public SubSearchRecyclerViewFoundSongFragment(List<Track> trackList, List<Artist> artistList, List<AlbumSimple> albumList) {
        this.trackList = trackList;
        this.albumList = albumList;
        this.artistList = artistList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_search_recyler_view_found_song, container, false);

        recyclerView = view.findViewById(R.id.search_recyclerViewFoundSongs);
        recyclerViewArtist = view.findViewById(R.id.search_recyclerViewFoundArtists);
        recyclerViewAlbum = view.findViewById(R.id.search_recyclerViewFoundAlbums);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManagerArtist = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManagerAlbum = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerViewArtist.setLayoutManager(layoutManagerArtist);
        recyclerViewAlbum.setLayoutManager(layoutManagerAlbum);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewArtist.setNestedScrollingEnabled(false);
        recyclerViewAlbum.setNestedScrollingEnabled(false);

        recyclerView.setHasFixedSize(false);
        recyclerViewArtist.setHasFixedSize(false);
        recyclerViewAlbum.setHasFixedSize(false);

        mAdapter = new SearchTrackAdapter(this, trackList);
        mAdapterArtist = new SearchArtistAdapter(this, artistList);
        mAdapterALbum = new SearchAlbumAdapter(this, albumList);

        recyclerView.setAdapter(mAdapter);
        recyclerViewArtist.setAdapter(mAdapterArtist);
        recyclerViewAlbum.setAdapter(mAdapterALbum);
        return view;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        mAdapter.setmDataList(trackList);
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
        mAdapterArtist.setmDataList(artistList);

    }

    public void setAlbumList(List<AlbumSimple> albumList) {
        this.albumList = albumList;
        mAdapterALbum.setmDataList(albumList);
    }
}