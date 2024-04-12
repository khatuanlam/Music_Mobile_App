package com.example.music_mobile_app.ui.mydatabase.playlist;


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

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
//import com.example.music_mobile_app.adapter.mydatabase.playlist.SongOfPlaylistAdapter;
import com.example.music_mobile_app.adapter.mydatabase.ListSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDetailFragment extends Fragment {

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;
    private TextView textView;
    private ImageView imageView, imageViewBack;
    private Playlist playlist;
    private FragmentManager manager;

    private ListSongAdapter mSongOfPlaylistAdapter;

    private RecyclerView songOfPlaylistRecyclerView;

    private long userId;

    public PlaylistDetailFragment(Playlist playlist, long userId)
    {
        this.playlist = playlist;
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_playlist_detail, container, false);
        textView = view.findViewById(R.id.mydb_playlist_detail_fragment_textView);
        imageView = view.findViewById(R.id.mydb_playlist_detail_fragment_imageView);
        imageViewBack = view.findViewById(R.id.mydb_playlist_detail_fragment_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });
        textView.setText(playlist.getName());
        Glide.with(this)
                .load("https://cafefcdn.com/203337114487263232/2023/8/22/meme-dog-dead-meme-dog-838897705-16926815196131046585734-1692691053462-1692691053693351825934.jpg")
                .into(imageView);

        songOfPlaylistRecyclerView = view.findViewById(R.id.mydb_playlist_detail_fragment_songs_recyclerView);
        songOfPlaylistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        mSongOfPlaylistAdapter = new ListSongAdapter(
                getContext(),
                this,
                manager,
                new ArrayList<Song>(),
                favoriteSongsViewModel,
                userId,
                songsOfPlaylistViewModel,
                "Playlist Song",
                playlist);
        songOfPlaylistRecyclerView.setAdapter(mSongOfPlaylistAdapter);

        songsOfPlaylistViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mSongOfPlaylistAdapter.setmDataList(songs);
                Log.i("PLAYLIST D","CAP NHAT");
                Log.i("PLAYLIST D",String.valueOf(songs.size()));
            }
        });

        songsOfPlaylistViewModel.getIsDeleteSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleteSuccess) {
                if(isDeleteSuccess == null)
                    return;
                if (isDeleteSuccess) {
                    Toast.makeText(getContext(), "ĐÃ GỠ KHỎI PLAYLIST", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        favoriteSongsViewModel.getIsPostSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if(isSuccess == null)
                    return;
                if (isSuccess) {
                    Toast.makeText(getContext(), "ĐÃ THÊM VÀO DANH SÁCH YÊU THÍCH", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        songsOfPlaylistViewModel.getAllSongsByPlaylistID(playlist.getId());

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getIsDeleteSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getSongs().removeObservers(this);
    }
 }

