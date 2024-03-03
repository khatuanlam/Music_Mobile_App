package com.example.music_mobile_app.ui;

import static com.example.music_mobile_app.ui.vanthong.constant.ConstantVariable.ACCESS_TOKEN;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.ui.vanthong.SubSearchFragment;
import com.example.music_mobile_app.ui.vanthong.adapter.SearchAlbumAdapter;

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
    private EditText editText;
    private RecyclerView recyclerView;
    private SearchAlbumAdapter mAdapter;
    private FragmentManager manager;
    public SearchFragment(FragmentManager manager) {
        this.manager = manager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        editText = view.findViewById(R.id.search_searchTextBox);


        recyclerView = view.findViewById(R.id.search_recyclerViewGenres);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new SearchAlbumAdapter(getActivity(),this, new ArrayList<AlbumSimple>());
        recyclerView.setAdapter(mAdapter);
        getAlbum("k-pop");
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!manager.isDestroyed()) {
                        SubSearchFragment subSearchFragment = new SubSearchFragment(manager);
                        manager.beginTransaction()
                                .replace(R.id.fragment, subSearchFragment)
                                .commit();
                    }

                }
            }
        });
        return view;
    }
    public void getAlbum(String q)
    {
        spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(ACCESS_TOKEN);
        SpotifyService spotify = spotifyApi.getService();
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
