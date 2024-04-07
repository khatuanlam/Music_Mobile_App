package com.example.music_mobile_app.ui.mydatabase;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.popular.song.PopularSongsAdapter;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.song.FilteredSongsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SubSearchFragment extends Fragment {
    private FilteredSongsViewModel filteredSongsViewModel;
    private EditText editText;
    private ImageView imageView;
    private FragmentManager manager;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;

    private FavoriteSongsViewModel favoriteSongsViewModel;

    private long userId;


    public RecyclerView foundSongRecyclerView;
    public PopularSongsAdapter popularSongsAdapter;
    public SubSearchFragment(long userId)
    {
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
        View view = inflater.inflate(R.layout.mydb_fragment_sub_search, container, false);
        editText = view.findViewById(R.id.mydb_search_subSearchTextBox);
        imageView = view.findViewById(R.id.mydb_search_subSearchBack);
        foundSongRecyclerView = view.findViewById(R.id.mydb_search_subSearchRecyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        foundSongRecyclerView.setLayoutManager(topsong_layoutManager);

        filteredSongsViewModel = new ViewModelProvider(this).get(FilteredSongsViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);


        popularSongsAdapter = new PopularSongsAdapter(getContext(), this, manager, new ArrayList<>(), favoriteSongsViewModel, userId, songsOfPlaylistViewModel);
        foundSongRecyclerView.setAdapter(popularSongsAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

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
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    filteredSongsViewModel.getFilteredSongsBySongName("");
                    filteredSongsViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
                        popularSongsAdapter.setmDataList(songs);
                    });
                } else {
                    filteredSongsViewModel.getFilteredSongsBySongName(editText.getText().toString());
                    filteredSongsViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
                        popularSongsAdapter.setmDataList(songs);
                    });
                }
            }


        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        songsOfPlaylistViewModel.getIsPostSuccess().removeObservers(this);

    }
}