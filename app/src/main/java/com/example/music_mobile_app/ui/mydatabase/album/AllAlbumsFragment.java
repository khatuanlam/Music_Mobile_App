package com.example.music_mobile_app.ui.mydatabase.album;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.mainFragment.TopAlbumPopularAdapter;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.album.TopPopularAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllAlbumsFragment extends Fragment {

    private TopPopularAlbumViewModel topPopularSongViewModel;

    private ImageView imageViewBack;
    private FragmentManager manager;

    private TopAlbumPopularAdapter topAlbumPopularAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    private long userId;
    public AllAlbumsFragment(long userId){
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
        View view = inflater.inflate(R.layout.mydb_fragment_all_albums, container, false);
        imageViewBack = view.findViewById(R.id.mydb_all_albums_fragment_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });

        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_all_albums_fragment_recyclerView);
        GridLayoutManager topsong_layoutManager = new GridLayoutManager(getContext(), 3);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        topPopularSongViewModel = new ViewModelProvider(this).get(TopPopularAlbumViewModel.class);


        topAlbumPopularAdapter = new TopAlbumPopularAdapter(getContext(), this, manager, new ArrayList<>(), userId);
        songOfAlbumRecyclerView.setAdapter(topAlbumPopularAdapter);

        topPopularSongViewModel.getAlbums().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> albums) {
                topAlbumPopularAdapter.setmDataList(albums);
                Log.i("ALL ALBUMS","CAP NHAT");
                Log.i("ALL ALBUMS",String.valueOf(albums.size()));
            }
        });

        topPopularSongViewModel.loadAlbum();


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topPopularSongViewModel.getAlbums().removeObservers(this);
    }
 }

