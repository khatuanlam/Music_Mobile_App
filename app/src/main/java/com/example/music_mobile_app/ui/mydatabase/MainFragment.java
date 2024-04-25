package com.example.music_mobile_app.ui.mydatabase;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.ListDownloadSongAdapter;
import com.example.music_mobile_app.adapter.mydatabase.mainFragment.TopAlbumPopularAdapter;
//import com.example.music_mobile_app.adapter.mydatabase.main.TopFavoriteSongAdapter;
import com.example.music_mobile_app.adapter.mydatabase.mainFragment.TopSongPopularAdapter;
import com.example.music_mobile_app.adapter.mydatabase.mainFragment.YourPlaylistsAdapter;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.model.sqlite.LiteSong;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.repository.sqlite.MusicDatabaseHelper;
import com.example.music_mobile_app.ui.mydatabase.album.AllAlbumsFragment;
import com.example.music_mobile_app.ui.mydatabase.favorite.AllDownloadSongsFragment;
import com.example.music_mobile_app.ui.mydatabase.favorite.AllFavoriteSongsFragment;
import com.example.music_mobile_app.ui.mydatabase.playlist.AllPlaylistsFragment;
import com.example.music_mobile_app.ui.mydatabase.popular.song.AllPopularSongsFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.TopPopularSongViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.album.TopPopularAlbumViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.sqlite.DownloadSongListViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainFragment extends Fragment {


    public static long userId = 1;

    private Button btnViewAllFavoriteSongs, btnViewAllAlbums, btnViewAllPlaylists, btnViewAllPopularSongs, btnViewAllDownload;
    private FragmentManager manager;

    private LinearLayout container_search, favorite_l, download_l;


    private DownloadSongListViewModel downloadSongListViewModel;
    private FavoriteSongsViewModel favoriteSongsViewModel;
    private TopPopularSongViewModel topPopularSongViewModel;
    private TopPopularAlbumViewModel topPopularAlbumViewModel;

    private AllPlaylistViewModel allPlaylistViewModel;

    private TopSongPopularAdapter mSongAdapter;

    private TopSongPopularAdapter mFavoriteSongAdapter;
    private TopAlbumPopularAdapter mAlbumAdapter;

    private YourPlaylistsAdapter mYourPlaylistsAdapter;

    private TopSongPopularAdapter mListDownloadSongAdapter;

    private RecyclerView topPopularSongsRecyclerView;
    private RecyclerView favoriteSongsRecyclerView;
    private RecyclerView topPopularAlbumsRecyclerView;

    private RecyclerView yourPlaylistsRecyclerView;

    private RecyclerView downloadRecyclerView;


    private LiteSongRepository liteSongRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
        liteSongRepository = new LiteSongRepository(MainActivity.musicDatabaseHelper);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserIdInMyDatabase", Context.MODE_PRIVATE);
        MainFragment.userId = sharedPreferences.getLong("userIdMyDb", 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_search, container, false);
        container_search = view.findViewById(R.id.mydb_search_container_search);

        btnViewAllFavoriteSongs = view.findViewById(R.id.mydb_search_viewAllFavoriteSongs_btn);
        btnViewAllAlbums = view.findViewById(R.id.mydb_search_viewTopPopularAlbums_btn);
        btnViewAllPopularSongs = view.findViewById(R.id.mydb_search_viewTopPopularSongs_btn);
        btnViewAllPlaylists = view.findViewById(R.id.mydb_search_viewAllPlaylists_btn);
        btnViewAllDownload = view.findViewById(R.id.mydb_search_viewAllDownload_btn);


        favorite_l = view.findViewById(R.id.mydb_search_favorite_l);
        download_l = view.findViewById(R.id.mydb_search_download_l);
        btnViewAllFavoriteSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllFavoriteSongsFragment fragment = new AllFavoriteSongsFragment(userId, liteSongRepository);
                manager.beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .commit();
            }
        });

        btnViewAllAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllAlbumsFragment allAlbumsFragment = new AllAlbumsFragment(userId, liteSongRepository);
                manager.beginTransaction()
                        .replace(R.id.fragment, allAlbumsFragment)
                        .commit();
            }
        });

        btnViewAllPopularSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPopularSongsFragment allPopularSongsFragment = new AllPopularSongsFragment(userId, liteSongRepository);
                manager.beginTransaction()
                        .replace(R.id.fragment, allPopularSongsFragment)
                        .commit();
            }
        });
        btnViewAllPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPlaylistsFragment allPlaylistsFragment = new AllPlaylistsFragment(userId, liteSongRepository);
                manager.beginTransaction()
                        .replace(R.id.fragment, allPlaylistsFragment)
                        .commit();
            }
        });
        btnViewAllDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllDownloadSongsFragment allDownloadSongsFragment = new AllDownloadSongsFragment(userId, liteSongRepository);
                manager.beginTransaction()
                        .replace(R.id.fragment, allDownloadSongsFragment)
                        .commit();
            }
        });

        downloadRecyclerView = view.findViewById(R.id.mydb_main_download_recyclerView);
        topPopularSongsRecyclerView = view.findViewById(R.id.mydb_main_top_popular_songs_recyclerView);
        topPopularAlbumsRecyclerView = view.findViewById(R.id.mydb_main_top_popular_albums_recyclerView);
        favoriteSongsRecyclerView = view.findViewById(R.id.mydb_main_favorite_songs_recyclerView);
        yourPlaylistsRecyclerView = view.findViewById(R.id.mydb_main_playlists_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topPopularSongsRecyclerView.setLayoutManager(topsong_layoutManager);
        LinearLayoutManager favoritesong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        favoriteSongsRecyclerView.setLayoutManager(favoritesong_layoutManager);
        LinearLayoutManager topalbum_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topPopularAlbumsRecyclerView.setLayoutManager(topalbum_layoutManager);
        LinearLayoutManager yourplaylist_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        yourPlaylistsRecyclerView.setLayoutManager(yourplaylist_layoutManager);
        LinearLayoutManager download_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        downloadRecyclerView.setLayoutManager(download_layoutManager);


        mListDownloadSongAdapter = new TopSongPopularAdapter(getActivity(), this, new ArrayList<Song>());
        downloadRecyclerView.setAdapter(mListDownloadSongAdapter);

        mSongAdapter = new TopSongPopularAdapter(getActivity(), this, new ArrayList<Song>());
        topPopularSongsRecyclerView.setAdapter(mSongAdapter);
        mAlbumAdapter = new TopAlbumPopularAdapter(getActivity(), this, manager, new ArrayList<Album>(), userId, liteSongRepository);
        topPopularAlbumsRecyclerView.setAdapter(mAlbumAdapter);
        mFavoriteSongAdapter = new TopSongPopularAdapter(getActivity(), this, new ArrayList<>());
        favoriteSongsRecyclerView.setAdapter(mFavoriteSongAdapter);
        mYourPlaylistsAdapter = new YourPlaylistsAdapter(getActivity(), this, manager, new ArrayList<>(), userId, liteSongRepository);
        yourPlaylistsRecyclerView.setAdapter(mYourPlaylistsAdapter);

        downloadSongListViewModel = new ViewModelProvider(this).get(DownloadSongListViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        allPlaylistViewModel =  new ViewModelProvider(this).get(AllPlaylistViewModel.class);
        topPopularAlbumViewModel =  new ViewModelProvider(this).get(TopPopularAlbumViewModel.class);
        topPopularSongViewModel =  new ViewModelProvider(this).get(TopPopularSongViewModel.class);

        downloadSongListViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<LiteSong>>() {
            @Override
            public void onChanged(List<LiteSong> liteSongs) {
                List<Song> songs = liteSongs.stream()
                        .map(s -> {
                            Song song = new Song();
                            song.name = s.getName();
                            song.image = s.getImage();
                            return song;
                        })
                        .collect(Collectors.toList());
                mListDownloadSongAdapter.setmDataList(songs);
            }
        });

        favoriteSongsViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mFavoriteSongAdapter.setmDataList(songs);
                if(songs.isEmpty())
                {
                    favorite_l.setVisibility(View.GONE);
                }
                else {
                    favorite_l.setVisibility(View.VISIBLE);
                }
                Log.i("MAIN FRAGMENT","CAP NHAT FAVORITE SONG");
            }
        });
        allPlaylistViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> songs) {
                mYourPlaylistsAdapter.setmDataList(songs);
                Log.i("MAIN FRAGMENT","CAP NHAT ALL PLAYLIST");
            }
        });
        topPopularAlbumViewModel.getAlbums().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> songs) {
                mAlbumAdapter.setmDataList(songs);
                Log.i("MAIN FRAGMENT","CAP NHAT POPULAR ALBUM");
            }
        });
        topPopularSongViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mSongAdapter.setmDataList(songs);
                Log.i("MAIN FRAGMENT","CAP NHAT POPULAR SONG");
            }
        });

        favoriteSongsViewModel.getAllFavoriteSongsByUserId(userId);
        allPlaylistViewModel.getAllPlaylistsByIdUser(userId);
        topPopularAlbumViewModel.loadAlbum();
        topPopularSongViewModel.loadSong();
        downloadSongListViewModel.loadSong();

        container_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    manager.beginTransaction()
                            .replace(R.id.fragment, new SubSearchFragment(userId, liteSongRepository))
                            .commit();
            }
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        downloadSongListViewModel.getSongs().removeObservers(this);
        favoriteSongsViewModel.getSongs().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
        topPopularSongViewModel.getSongs().removeObservers(this);
        topPopularAlbumViewModel.getAlbums().removeObservers(this);
    }
 }

