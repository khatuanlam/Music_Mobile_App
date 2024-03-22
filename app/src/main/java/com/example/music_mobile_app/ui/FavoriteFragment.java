package com.example.music_mobile_app.ui;

import static com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable.ACCESS_TOKEN;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music_mobile_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import Adapter.TrackAdapter;


import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;
import com.example.music_mobile_app.manager.Service.RetrofitClient;
import com.example.music_mobile_app.manager.Service.SpotifyApiService;
import com.example.music_mobile_app.model.Song;
import com.example.music_mobile_app.model.Track;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

//    private static final String ACCESS_TOKEN = ConstantVariable.ACCESS_TOKEN;

    private SpotifyApiService spotifyApiService;
    private FragmentManager manager;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private SpotifyApi spotifyApi;


    public List<Track> trackList = new ArrayList<>();
    private RetrofitClient retrofitClient;
    public FavoriteFragment() {

    }


    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        retrofitClient = new RetrofitClient();
        spotifyApiService = retrofitClient.getClient();

        recyclerView = view.findViewById(R.id.recyclerMusicViewLiked);
        trackAdapter = new TrackAdapter(trackList, getContext());
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getTrackData( "1J3SmWwlYAG68LGKr86MVH?si=f01feeb87adb4273");
        return view;
    }


    public void getTrackData(String trackId) {
        Call<Track> call = spotifyApiService.getTrack(trackId);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(response.isSuccessful()){
                    Track track = response.body();
                    String a = track.getArtists().get(0).getName();
                    String b = track.getAlbum().getRelease_date();
                    trackList.add(track);
                    trackAdapter.notifyDataSetChanged();
                    Log.i("ok", a+b);
                }
                else {
                    Log.i("ko 0 ok", "ko ok");

                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.i("ko 1 ok", t.getMessage());
            }
        });

    }


}

