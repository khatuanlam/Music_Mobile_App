package com.example.music_mobile_app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.extension.TopAlbumPopularAdapter;
import com.example.music_mobile_app.adapter.extension.TopSongPopularAdapter;
import com.example.music_mobile_app.adapter.extension.YourPlaylistsAdapter;
import com.example.music_mobile_app.adapter.extension.favorite.AllDownloadSongsFragment;
import com.example.music_mobile_app.adapter.extension.favorite.AllFavoriteSongsFragment;
import com.example.music_mobile_app.adapter.extension.playlist.AllPlaylistsFragment;
import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.extension.Playlist;
import com.example.music_mobile_app.model.extension.Song;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;

import com.example.music_mobile_app.ui.extension.SubSearchFragment;
import com.example.music_mobile_app.ui.extension.album.AllAlbumsFragment;
import com.example.music_mobile_app.ui.extension.popular.AllPopularSongsFragment;
import com.example.music_mobile_app.viewmodel.TopPopularSongViewModel;
import com.example.music_mobile_app.viewmodel.album.TopPopularAlbumViewModel;
import com.example.music_mobile_app.viewmodel.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.playlist.AllPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExtensionFragment extends Fragment {
    public static long userId = 1; //SET BIẾN NÀY THÀNH ID CỦA USER ĐƯỢC TRẢ VỀ KHI GỌI /SpotifyLogin/{idSpotify}

    public final String TAG = this.getClass().getSimpleName();
    private EditText editText;
    private Button btnViewAllFavoriteSongs, btnViewAllAlbums, btnViewAllPlaylists, btnViewAllPopularSongs, btnViewAllDownload;
    private FragmentManager manager;

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private TopPopularSongViewModel topPopularSongViewModel;
    private TopPopularAlbumViewModel topPopularAlbumViewModel;

    private AllPlaylistViewModel allPlaylistViewModel;

    private TopSongPopularAdapter mSongAdapter;

    private TopSongPopularAdapter mFavoriteSongAdapter;
    private TopAlbumPopularAdapter mAlbumAdapter;

    private YourPlaylistsAdapter mYourPlaylistsAdapter;

    private RecyclerView topPopularSongsRecyclerView;
    private RecyclerView favoriteSongsRecyclerView;
    private RecyclerView topPopularAlbumsRecyclerView;

    private RecyclerView yourPlaylistsRecyclerView;

    private LiteSongRepository liteSongRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
        liteSongRepository = new LiteSongRepository(MainActivity.musicDatabaseHelper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_search, container, false);
        editText = view.findViewById(R.id.mydb_search_searchTextBox);

        prepareData(view);
        btnViewAllFavoriteSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllFavoriteSongsFragment fragment = new AllFavoriteSongsFragment(userId, liteSongRepository);
                manager.beginTransaction().replace(R.id.fragment, fragment).commit();
            }
        });

        btnViewAllAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllAlbumsFragment allAlbumsFragment = new AllAlbumsFragment(userId, liteSongRepository);
                manager.beginTransaction().replace(R.id.fragment, allAlbumsFragment).commit();
            }
        });

        btnViewAllPopularSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPopularSongsFragment allPopularSongsFragment = new AllPopularSongsFragment(userId, liteSongRepository);
                manager.beginTransaction().replace(R.id.fragment, allPopularSongsFragment).commit();
            }
        });
        btnViewAllPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllPlaylistsFragment allPlaylistsFragment = new AllPlaylistsFragment(userId, liteSongRepository);
                manager.beginTransaction().replace(R.id.fragment, allPlaylistsFragment).commit();
            }
        });
        btnViewAllDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllDownloadSongsFragment allDownloadSongsFragment = new AllDownloadSongsFragment(userId, liteSongRepository);
                manager.beginTransaction().replace(R.id.fragment, allDownloadSongsFragment).commit();
            }
        });

        favoriteSongsViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mFavoriteSongAdapter.setmDataList(songs);
                Log.i(TAG, "CAP NHAT FAVORITE SONG");
            }
        });
        allPlaylistViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> songs) {
                mYourPlaylistsAdapter.setmDataList(songs);
                Log.i(TAG, "CAP NHAT ALL PLAYLIST");
            }
        });
        topPopularAlbumViewModel.getAlbums().observe(getViewLifecycleOwner(), new Observer<List<Album>>() {
            @Override
            public void onChanged(List<Album> songs) {
                mAlbumAdapter.setmDataList(songs);
                Log.i(TAG, "CAP NHAT POPULAR ALBUM");
            }
        });
        topPopularSongViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mSongAdapter.setmDataList(songs);
                Log.i(TAG, "CAP NHAT POPULAR SONG");
            }
        });

        favoriteSongsViewModel.getAllFavoriteSongsByUserId(userId);
        allPlaylistViewModel.getAllPlaylistsByIdUser(userId);
        topPopularAlbumViewModel.loadAlbum();
        topPopularSongViewModel.loadSong();


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    manager.beginTransaction().replace(R.id.fragment, new SubSearchFragment(userId, liteSongRepository)).commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteSongsViewModel.getSongs().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
        topPopularSongViewModel.getSongs().removeObservers(this);
        topPopularAlbumViewModel.getAlbums().removeObservers(this);
    }

    public void prepareData(View view) {

        btnViewAllFavoriteSongs = view.findViewById(R.id.mydb_search_viewAllFavoriteSongs_btn);
        btnViewAllAlbums = view.findViewById(R.id.mydb_search_viewTopPopularAlbums_btn);
        btnViewAllPopularSongs = view.findViewById(R.id.mydb_search_viewTopPopularSongs_btn);
        btnViewAllPlaylists = view.findViewById(R.id.mydb_search_viewAllPlaylists_btn);
        btnViewAllDownload = view.findViewById(R.id.mydb_search_viewAllDownload_btn);


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

        // Setting adapter
        mSongAdapter = new TopSongPopularAdapter(getActivity(), this, new ArrayList<Song>());
        topPopularSongsRecyclerView.setAdapter(mSongAdapter);
        mAlbumAdapter = new TopAlbumPopularAdapter(getActivity(), this, manager, new ArrayList<>(), userId, liteSongRepository);
        topPopularAlbumsRecyclerView.setAdapter(mAlbumAdapter);
        mFavoriteSongAdapter = new TopSongPopularAdapter(getActivity(), this, new ArrayList<Song>());
        favoriteSongsRecyclerView.setAdapter(mFavoriteSongAdapter);
        mYourPlaylistsAdapter = new YourPlaylistsAdapter(getActivity(), this, manager, new ArrayList<Playlist>(), userId, liteSongRepository);
        yourPlaylistsRecyclerView.setAdapter(mYourPlaylistsAdapter);

        // View models
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);
        topPopularAlbumViewModel = new ViewModelProvider(this).get(TopPopularAlbumViewModel.class);
        topPopularSongViewModel = new ViewModelProvider(this).get(TopPopularSongViewModel.class);

    }

}

