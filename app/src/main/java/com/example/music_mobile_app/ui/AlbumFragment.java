package com.example.music_mobile_app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.Service.RetrofitClient;
import com.example.music_mobile_app.manager.Service.SpotifyApiService;
import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Page;
import com.example.music_mobile_app.model.Track;
import com.example.music_mobile_app.model.Tracks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Adapter.TrackAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragment extends Fragment {



    private SpotifyApiService spotifyApiService;
    private FragmentManager manager;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private SpotifyApi spotifyApi;
    public List<Track> trackList = new ArrayList<Track>();
    private RetrofitClient retrofitClient;

    private ImageView albumImage;
    private TextView albumName;
    private TextView albumArtistname;

    private TextView release_date;
    public AlbumFragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance() {
        AlbumFragment fragment = new AlbumFragment();
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

        View view = inflater.inflate(R.layout.fragment_album, container, false);

        albumImage = view.findViewById(R.id.albumImage);
        albumArtistname = view.findViewById(R.id.albumArtistname);
        albumName = view.findViewById(R.id.albumName);
        release_date = view.findViewById(R.id.albumRelease_date);



        retrofitClient = new RetrofitClient();
        spotifyApiService = retrofitClient.getClient();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerAlbumView);
        trackAdapter = new TrackAdapter(trackList, getContext());
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAlbumInfo("5jDZKqgoVRbob6A3omYTG5");
        getAlbumTracks("5jDZKqgoVRbob6A3omYTG5");


        return view;
    }

    public void getAlbumInfo(String ALbumID){
        Call<Album> call = spotifyApiService.getAlbum(ALbumID);
        call.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.isSuccessful()){
                    Log.i("get api", "successfull");
                    albumArtistname.setText(response.body().getArtists().get(0).getName());
                    albumName.setText(response.body().getName());
                    release_date.setText(response.body().getRelease_date());
                    String imageUrl = response.body().getImageUrl().get(0).getUrl();
                    Picasso.get().load(imageUrl).into(albumImage);
//                    List<Track> tracks = response.body().getTracks().getItems();
//                    trackList.addAll(tracks);
//                    trackAdapter.notifyDataSetChanged();
//                    Log.i("Track " ,   " " + String.valueOf(tracks.size()) );


                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Log.i("get api", "Unsuccessfull");

            }
        });
    }

    public void getAlbumTracks(String albumID){
        Call <Page<Track>> call = spotifyApiService.getTrackFromAlbum(albumID);
        call.enqueue(new Callback<Page<Track>>() {
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
    }
}