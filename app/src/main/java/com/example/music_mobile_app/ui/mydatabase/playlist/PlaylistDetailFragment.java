package com.example.music_mobile_app.ui.mydatabase.playlist;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.music_mobile_app.ExtensionPlayerActivity;
import com.example.music_mobile_app.R;
//import com.example.music_mobile_app.adapter.mydatabase.playlist.SongOfPlaylistAdapter;
import com.example.music_mobile_app.adapter.mydatabase.ListSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaylistDetailFragment extends Fragment {

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;
    private TextView textView;
    private CircleImageView avt;
    private ImageButton btn_play;
    private ImageView imageView, imageViewBack;
    private Playlist playlist;
    private FragmentManager manager;

    private ListSongAdapter mSongOfPlaylistAdapter;

    private RecyclerView songOfPlaylistRecyclerView;

    private long userId;
    public LiteSongRepository liteSongRepository;
    public PlaylistDetailFragment(Playlist playlist, long userId, LiteSongRepository liteSongRepository)
    {
        this.playlist = playlist;
        this.userId = userId;
        this.liteSongRepository = liteSongRepository;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }
    public void setThumbnailForPlaylist(String url)
    {
        String defaultUrl = "https://i.pinimg.com/originals/ae/8a/c2/ae8ac2fa217d23aadcc913989fcc34a2.png";
        if(url.isEmpty())
            url = defaultUrl;
        Glide.with(this)
                .load(url)
                .into(imageView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_playlist_detail, container, false);
        textView = view.findViewById(R.id.mydb_playlist_detail_fragment_textView);
        imageView = view.findViewById(R.id.mydb_playlist_detail_fragment_imageView);
        imageViewBack = view.findViewById(R.id.mydb_playlist_detail_fragment_back);
        btn_play = view.findViewById(R.id.mydb_playlist_detail_btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ExtensionPlayerActivity.class);
                intent.setAction("Play Playlist");
                intent.putExtra("playlistId", playlist.getId());
                intent.putExtra("userIdMyDb", userId);
                getContext().startActivity(intent);
            }
        });
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
        textView.setText(playlist.getName());


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
                playlist,
                liteSongRepository);
        songOfPlaylistRecyclerView.setAdapter(mSongOfPlaylistAdapter);

        songsOfPlaylistViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mSongOfPlaylistAdapter.setmDataList(songs);

                if(!songs.isEmpty())
                {
                    setThumbnailForPlaylist(songs.get(0).getImage());
                    Log.i("ANH PL","CAP NHAT");
                }
                else {
                    setThumbnailForPlaylist("");
                    Log.i("ANH PL","CAP NHAT RONG");
                }
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
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getIsDeleteSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getSongs().removeObservers(this);
    }
 }

