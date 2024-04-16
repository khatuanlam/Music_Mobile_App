package com.example.music_mobile_app.ui;


import androidx.fragment.app.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.SearchGenreAdapter;

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

public class SearchFragment extends Fragment {

    private SpotifyApi spotifyApi;
    private LinearLayout container_search;
    private RecyclerView recyclerView;
    private SearchGenreAdapter mAdapter;
    private FragmentManager manager;

    private SpotifyService spotify = MainActivity.spotifyService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        container_search = view.findViewById(R.id.container_search);


        recyclerView = view.findViewById(R.id.search_recyclerViewGenres);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SearchGenreAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        Log.i("vao get 0", "GET THANH CONG");
        getAlbum("k-pop");

        container_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction()
                        .replace(R.id.fragment, new SubSearchFragment())
                        .commit();
            }
        });


        return view;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public void getAlbum(String q) {
        spotify.searchAlbums(q, new Callback<AlbumsPager>() {
            @Override
            public void success(AlbumsPager albumsPager, Response response) {
                Log.i("GET DATA", "GET THANH CONG");
                Pager<AlbumSimple> albumSimplePager = albumsPager.albums;
                List<AlbumSimple> albumSimpleList = albumSimplePager.items;
                mAdapter.setmDataList(albumSimpleList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("GET DATA LOI", "GET KHONG THANH CONG");
                Log.i("GET DATA LOI", Objects.requireNonNull(error.getMessage()));
            }
        });

    }

}
