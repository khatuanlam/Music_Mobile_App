package com.example.music_mobile_app.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.Service.RetrofitClient;
import com.example.music_mobile_app.manager.Service.SpotifyApiService;
import com.example.music_mobile_app.model.Page;
import com.example.music_mobile_app.model.Playlist;
import com.example.music_mobile_app.model.Track;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.TrackAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlaylistFragment extends Fragment {

    public List<Track> trackList = new ArrayList<Track>();
    private RetrofitClient retrofitClient;
    private TrackAdapter trackAdapter;
    private SpotifyApiService spotifyApiService;
    private FragmentManager manager;
    private RecyclerView recyclerView;
    ImageView playlistImage;
    TextView playlistName;
    TextView playlistOwner;
    Button likebutton;
    Button playbtn;

    public PlaylistFragment() {

    }


    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager manager = getParentFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlistImage = view.findViewById(R.id.playlistImage);
        playlistName = view.findViewById(R.id.playlistName);
        playlistOwner = view.findViewById(R.id.playlistOwnerName);
        likebutton = view.findViewById(R.id.playlistLikebtn);
        playbtn = view.findViewById(R.id.playlistPlaybtn);



        retrofitClient = new RetrofitClient();
        spotifyApiService = retrofitClient.getClient();


        getPlaylistInfo("id","field");
        getPlaylistTrack("id","field");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerPlaylistView);
        trackAdapter = new TrackAdapter(trackList, getContext());
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    public void getPlaylistTrack(String id, String field){
        Call<Page<Track>> trackcall = spotifyApiService.getPlaylistTracks(id, field);
        trackcall.enqueue(new Callback<Page<Track>>() {
            @Override
            public void onResponse(Call<Page<Track>> call, Response<Page<Track>> response) {
                if(response.isSuccessful()){
                    Page<Track> trackPage = response.body();
                    trackList.addAll(trackPage.items);
                    trackAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Page<Track>> call, Throwable t) {
                Log.i("get api", "Unsuccessfull");

            }
        });
    };

    public void getPlaylistInfo(String id, String field){

        Call<Playlist> playlistcall = spotifyApiService.getPlaylist(id,field);
        playlistcall.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()){
                    Playlist playlist = response.body();
                    String imageUrl = playlist.getImages().get(0).getUrl();
                    Picasso.get().load(imageUrl).into(playlistImage);
                    playlistName.setText(playlist.getName());

                    Log.i("get API", "getSuccess ");

                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.i("get API", "getUnSuccess " + t.getMessage());

            }
        });

    }
}