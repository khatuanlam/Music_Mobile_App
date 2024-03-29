package com.example.music_mobile_app.ui.mydatabase;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.SearchAlbumAdapter;
import com.example.music_mobile_app.adapter.mydatabase.TopSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.repository.mydatabase.impl.SongRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.AlbumsPager;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainFragment extends Fragment {

    private EditText editText;
    private RecyclerView recyclerView;
    private TopSongAdapter mAdapter;
    private FragmentManager manager;
    private SongRepository songRepository;
    private LiveData<List<Song>> songListLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
        songRepository = new SongRepositoryImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_search, container, false);
        editText = view.findViewById(R.id.mydb_search_searchTextBox);


        recyclerView = view.findViewById(R.id.mydb_search_recyclerViewSongs);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);


        mAdapter = new TopSongAdapter(getActivity(), this, new ArrayList<Song>());
        recyclerView.setAdapter(mAdapter);

        songListLiveData = songRepository.getAllSongs();
        songListLiveData.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                if (songs != null) {
                    mAdapter.setmDataList(songs);
                    Log.i("Cap nhat list songs","cap nhat");
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    manager.beginTransaction()
                            .replace(R.id.fragment, new SubSearchFragment())
                            .commit();
                }
            }
        });
        return view;
    }
 }

