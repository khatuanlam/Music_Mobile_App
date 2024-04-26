package com.example.music_mobile_app.ui.mydatabase.favorite;


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

import com.example.music_mobile_app.ExtensionPlayerActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.ListDownloadSongAdapter;
import com.example.music_mobile_app.adapter.mydatabase.ListSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.model.sqlite.LiteSong;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.sqlite.DownloadSongListViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllDownloadSongsFragment extends Fragment {

    private FavoriteSongsViewModel favoriteSongsViewModel;

    private DownloadSongListViewModel downloadSongListViewModel;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;
    private AllPlaylistViewModel allPlaylistViewModel;
    private TextView textView;
    private CircleImageView avt;
    private ImageView imageViewBack;
    private ImageButton btn_play;
    private FragmentManager manager;

    private ListDownloadSongAdapter mDownloadSongsAdapter;

    private RecyclerView downloadSongsRecyclerView;

    private long id;
    private LiteSongRepository liteSongRepository;
    public AllDownloadSongsFragment(long id, LiteSongRepository liteSongRepository)
    {
        this.id = id;
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
        View view = inflater.inflate(R.layout.mydb_fragment_downloaded_songs, container, false);
        imageViewBack = view.findViewById(R.id.mydb_download_songs_fragment_back);
        btn_play = view.findViewById(R.id.mydb_download_songs_btn_play);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ExtensionPlayerActivity.class);
                intent.setAction("Play Download");
                intent.putExtra("userIdMyDb", id );
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
        downloadSongsRecyclerView = view.findViewById(R.id.mydb_download_songs_fragment_songs_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        downloadSongsRecyclerView.setLayoutManager(topsong_layoutManager);

        downloadSongListViewModel = new ViewModelProvider(this).get(DownloadSongListViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);
        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);

        mDownloadSongsAdapter = new ListDownloadSongAdapter(getContext(),
                this,
                manager,
                new ArrayList<LiteSong>(),
                favoriteSongsViewModel,
                id,
                songsOfPlaylistViewModel);
        downloadSongsRecyclerView.setAdapter(mDownloadSongsAdapter);

        downloadSongListViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<LiteSong>>() {
            @Override
            public void onChanged(List<LiteSong> liteSongs) {
                mDownloadSongsAdapter.setmDataList(liteSongs);
                if (liteSongs.isEmpty())
                    btn_play.setVisibility(View.INVISIBLE);
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
                mDownloadSongsAdapter.setOpenPlaylists(playlists);
            }
        });
        downloadSongListViewModel.loadSong();
        allPlaylistViewModel.getAllPlaylistsByIdUser(id);
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
        downloadSongListViewModel.getSongs().removeObservers(this);
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getIsPostSuccess().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
    }
 }

