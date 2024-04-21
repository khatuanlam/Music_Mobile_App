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
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.album.TopPopularAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllAlbumsFragment extends Fragment {

    private TopPopularAlbumViewModel topPopularSongViewModel;

    private ImageView imageViewBack;
    private CircleImageView avt;
    private FragmentManager manager;

    private TopAlbumPopularAdapter topAlbumPopularAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    private long userId;
    public LiteSongRepository liteSongRepository;
    public AllAlbumsFragment(long userId, LiteSongRepository liteSongRepository){
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
        View view = inflater.inflate(R.layout.mydb_fragment_all_albums, container, false);
        imageViewBack = view.findViewById(R.id.mydb_all_albums_fragment_back);
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

        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_all_albums_fragment_recyclerView);
        GridLayoutManager topsong_layoutManager = new GridLayoutManager(getContext(), 3);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        topPopularSongViewModel = new ViewModelProvider(this).get(TopPopularAlbumViewModel.class);


        topAlbumPopularAdapter = new TopAlbumPopularAdapter(getContext(), this, manager, new ArrayList<>(), userId, liteSongRepository);
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
        topPopularSongViewModel.getAlbums().removeObservers(this);
    }
 }

