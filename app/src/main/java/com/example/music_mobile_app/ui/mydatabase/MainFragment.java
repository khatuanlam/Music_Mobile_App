package com.example.music_mobile_app.ui.mydatabase;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.TopAlbumPopularAdapter;
import com.example.music_mobile_app.adapter.mydatabase.TopFavoriteSongAdapter;
import com.example.music_mobile_app.adapter.mydatabase.TopSongPopularAdapter;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.AlbumRepository;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.repository.mydatabase.impl.AlbumRepositoryImpl;
import com.example.music_mobile_app.repository.mydatabase.impl.SongRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private EditText editText;

    private TopSongPopularAdapter mSongAdapter;

    private TopFavoriteSongAdapter mFavoriteSongAdapter;
    private TopAlbumPopularAdapter mAlbumAdapter;
    private FragmentManager manager;
    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private LiveData<List<Song>> popular_songListLiveData;
    private LiveData<List<Album>> popular_albumListLiveData;

    private LiveData<List<Song>> favorite_songListLiveData;

    private RecyclerView topPopularSongsRecyclerView;
    private RecyclerView favoriteSongsRecyclerView;
    private RecyclerView topPopularAlbumsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
        songRepository = new SongRepositoryImpl();
        albumRepository = new AlbumRepositoryImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_search, container, false);
        editText = view.findViewById(R.id.mydb_search_searchTextBox);


        topPopularSongsRecyclerView = view.findViewById(R.id.mydb_main_top_popular_songs_recyclerView);
        topPopularAlbumsRecyclerView = view.findViewById(R.id.mydb_main_top_popular_albums_recyclerView);
        favoriteSongsRecyclerView = view.findViewById(R.id.mydb_main_favorite_songs_recyclerView);


        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topPopularSongsRecyclerView.setLayoutManager(topsong_layoutManager);

        LinearLayoutManager favoritesong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        favoriteSongsRecyclerView.setLayoutManager(favoritesong_layoutManager);

        LinearLayoutManager topalbum_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topPopularAlbumsRecyclerView.setLayoutManager(topalbum_layoutManager);


        mSongAdapter = new TopSongPopularAdapter(getActivity(), this, new ArrayList<Song>());
        topPopularSongsRecyclerView.setAdapter(mSongAdapter);

        mAlbumAdapter = new TopAlbumPopularAdapter(getActivity(), this, manager, new ArrayList<Album>());
        topPopularAlbumsRecyclerView.setAdapter(mAlbumAdapter);

        mFavoriteSongAdapter = new TopFavoriteSongAdapter(getActivity(), this, new ArrayList<>());
        favoriteSongsRecyclerView.setAdapter(mFavoriteSongAdapter);

        popular_songListLiveData = songRepository.getTopPopularitySongs();
        popular_albumListLiveData = albumRepository.getTopPopularityAlbums();

        favorite_songListLiveData = songRepository.getAllFavoriteSongsFromIdUser(1);

        popular_songListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                if (songs != null) {
                    mSongAdapter.setmDataList(songs);
                    Log.i("SONG POPULAR","cap nhat");
                    songs.forEach(s -> System.out.println(s.toString()));
                }
            }
        });
        popular_albumListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                if (albums != null) {
                    mAlbumAdapter.setmDataList(albums);
                    Log.i("ALBUM POPULAR" ,"cap nhat");
                    albums.forEach(s -> System.out.println(s.toString()));
                }
            }
        });
        favorite_songListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                if (songs != null) {
                    mFavoriteSongAdapter.setmDataList(songs);
                    Log.i("UPDATE FAVORITE","cap nhat");
                    songs.forEach(s -> System.out.println(s.toString()));
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    manager.beginTransaction()
                            .replace(R.id.fragment, new SubSearchFragment())
                            .commit();
                }
            }
        });
        return view;
    }
 }

