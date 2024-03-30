package com.example.music_mobile_app.ui.mydatabase.album;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.SongAdapter;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.repository.mydatabase.impl.SongRepositoryImpl;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailFragment extends Fragment {

    private TextView textView;
    private ImageView imageView, imageViewBack;
    private Album album;
    private FragmentManager manager;
    private SongRepository songRepository;
    private LiveData<List<Song>> album_songListLiveData;

    private SongAdapter mSongAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    public AlbumDetailFragment(Album album)
    {
        this.album = album;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
        songRepository = new SongRepositoryImpl();
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

            }
        });
        textView.setText(album.getName());
        Glide.with(this)
                .load(album.getImage())
                .into(imageView);

        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_album_detail_fragment_songs_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        mSongAdapter = new SongAdapter(getActivity(), this, new ArrayList<Song>());
        songOfAlbumRecyclerView.setAdapter(mSongAdapter);

        album_songListLiveData = songRepository.getAllSongsFromAlbum(album.getId());

        album_songListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                if (songs != null) {
                    mSongAdapter.setmDataList(songs);
                    Log.i("Cap nhat list song of album" + album.getName(),"cap nhat");
                    songs.forEach(s -> System.out.println(s.toString()));
                }
            }
        });

        return view;
    }
 }

