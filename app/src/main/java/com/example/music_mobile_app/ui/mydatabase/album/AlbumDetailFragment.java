package com.example.music_mobile_app.ui.mydatabase.album;


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

import com.example.music_mobile_app.adapter.mydatabase.ListSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.album.SongsOfAlbumViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlbumDetailFragment extends Fragment {

    private SongsOfAlbumViewModel songsOfAlbumViewModel;

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private TextView textView;
    private CircleImageView avt;
    private ImageView imageView, imageViewBack;
    private Album album;
    private FragmentManager manager;

    private ListSongAdapter mFavoriteSongsAdapter;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;

    private RecyclerView songOfAlbumRecyclerView;
    private AllPlaylistViewModel allPlaylistViewModel;


    private long userId;
    public LiteSongRepository liteSongRepository;

    public AlbumDetailFragment(Album album, long userId, LiteSongRepository liteSongRepository)
    {
        this.album = album;
        this.userId = userId;
        this.liteSongRepository = liteSongRepository;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_album_detail, container, false);
        textView = view.findViewById(R.id.mydb_album_detail_fragment_textView);
        imageView = view.findViewById(R.id.mydb_album_detail_fragment_imageView);
        imageViewBack = view.findViewById(R.id.mydb_album_detail_fragment_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();
                if(avt != null)
                    avt.setVisibility(View.VISIBLE);

            }
        });
        textView.setText(album.getName());
        Glide.with(this)
                .load(album.getImage())
                .into(imageView);

        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_album_detail_fragment_songs_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        songsOfAlbumViewModel = new ViewModelProvider(this).get(SongsOfAlbumViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);
        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);

        mFavoriteSongsAdapter = new ListSongAdapter(getContext(),
                this,
                manager,
                new ArrayList<Song>(),
                favoriteSongsViewModel,
                userId,
                songsOfPlaylistViewModel,
                "Album Song",
                null,
                liteSongRepository);
        songOfAlbumRecyclerView.setAdapter(mFavoriteSongsAdapter);

        songsOfAlbumViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mFavoriteSongsAdapter.setmDataList(songs);
                Log.i("ALBUM DETAIL","CAP NHAT");
                Log.i("ALBUM DETAIL",String.valueOf(songs.size()));
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
                mFavoriteSongsAdapter.setOpenPlaylists(playlists);
            }
        });

        songsOfAlbumViewModel.getAllSongsByAlbum(album.getId());
        allPlaylistViewModel.getAllPlaylistsByIdUser(userId);


        com.example.music_mobile_app.ui.MainFragment mainFragment = (com.example.music_mobile_app.ui.MainFragment) getParentFragment();

        if (mainFragment != null) {
            avt = mainFragment.getView().findViewById(R.id.avt);
            avt.setVisibility(View.GONE);
        }
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        songsOfAlbumViewModel.getSongs().removeObservers(this);
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getIsPostSuccess().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
    }
 }

