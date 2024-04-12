package com.example.music_mobile_app.ui.mydatabase.popular.song;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.ListSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.TopPopularSongViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllPopularSongsFragment extends Fragment {

    private TopPopularSongViewModel topPopularSongViewModel;
    private AllPlaylistViewModel allPlaylistViewModel;

    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private TextView textView;
    private ImageView imageViewBack;
    private FragmentManager manager;

    private ListSongAdapter listSongAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    private long userId;

    public AllPopularSongsFragment(long id)
    {
        this.userId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_all_popular_songs, container, false);
        imageViewBack = view.findViewById(R.id.mydb_all_popular_songs_fragment_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });
        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_all_popular_songs_fragment_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        topPopularSongViewModel = new ViewModelProvider(this).get(TopPopularSongViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);
        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);

        listSongAdapter = new ListSongAdapter(getActivity(), this, manager, new ArrayList<Song>(), favoriteSongsViewModel, userId, songsOfPlaylistViewModel, "Popular Song", null);
        songOfAlbumRecyclerView.setAdapter(listSongAdapter);

        topPopularSongViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                listSongAdapter.setmDataList(songs);
                Log.i("FAVORITE","CAP NHAT");
                Log.i("FAVORITE",String.valueOf(songs.size()));
            }
        });
        favoriteSongsViewModel.getIsPostSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleteSuccess) {
                if(isDeleteSuccess == null)
                    return;
                if (isDeleteSuccess) {
                    Toast.makeText(getContext(), "ĐÃ THÊM VÀO DANH SÁCH YÊU THÍCH", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        songsOfPlaylistViewModel.getIsPostSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPostSuccess) {
                if(isPostSuccess == null)
                    return;
                if (isPostSuccess) {
                    Toast.makeText(getContext(), "ĐÃ THÊM VÀO PLAYLIST", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        allPlaylistViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                listSongAdapter.setOpenPlaylists(playlists);
            }
        });

        topPopularSongViewModel.loadSong();
        allPlaylistViewModel.getAllPlaylistsByIdUser(userId);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topPopularSongViewModel.getSongs().removeObservers(this);
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
        songsOfPlaylistViewModel.getIsPostSuccess().removeObservers(this);
    }
 }

