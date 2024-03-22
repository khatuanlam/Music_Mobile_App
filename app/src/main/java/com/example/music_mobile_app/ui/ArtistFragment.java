package com.example.music_mobile_app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.Service.RetrofitClient;
import com.example.music_mobile_app.manager.Service.SpotifyApiService;
import com.example.music_mobile_app.model.Artist;
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

public class ArtistFragment extends Fragment {

    private TextView artistName;
    private TextView listeners;
    private ImageView artistImage;
    private Button followButton;
    private Button overflowMenu;
    private Button playMusic;

    private SpotifyApiService spotifyApiService;
    private FragmentManager manager;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private SpotifyApi spotifyApi;


    public List<Track> trackList = new ArrayList<Track>();
    private RetrofitClient retrofitClient;

    public ArtistFragment() {

    }



    public static ArtistFragment newInstance() {
        ArtistFragment fragment = new ArtistFragment();
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

        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        artistName = view.findViewById(R.id.textArtistName);
        artistImage = view.findViewById(R.id.artistAvatar);
        listeners = view.findViewById(R.id.followerNumber);
//        overflowMenu = view.findViewById(R.id.overflowArtistButton);
        playMusic = view.findViewById(R.id.playAristButton);



        retrofitClient = new RetrofitClient();
        spotifyApiService = retrofitClient.getClient();

        getArtistInfo("0ZbgKh0FgPYeFP38nVaEGp?si=JaIwBfW-SEO6kGvwP4bPzQ");
        getArtistTopTrack("5dfZ5uSmzR7VQK0udbAVpf");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerArtistMusicView);
        trackAdapter = new TrackAdapter(trackList, getContext());
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;

    }

        public void getArtistInfo(String artistID){
            Call<Artist> artistCall = spotifyApiService.getArtist(artistID);
            artistCall.enqueue(new Callback<Artist>() {
                @Override
                public void onResponse(Call<Artist> call, Response<Artist> response) {
                    if(response.isSuccessful()){
                        Artist artist = response.body();
                        Log.i("get API", "getSuccess " + artist.getName());
                        artistName.setText(artist.getName());
                        String imageUrl = artist.getImages().get(0).getUrl();
                        Picasso.get().load(imageUrl).into(artistImage);
                        listeners.setText(String.valueOf(artist.getFollowers().getTotal()) + " người theo dõi ");


                    }
                }

                @Override
                public void onFailure(Call<Artist> call, Throwable t) {
                    Log.i("get API", "getUnSuccess " + t.getMessage());
                }
            });

        }

        public void getArtistTopTrack(String artistID){
         Call<Tracks> topTrack = spotifyApiService.getArtistTopTrack(artistID);
            topTrack.enqueue(new Callback<Tracks>() {
                @Override
                public void onResponse(Call<Tracks> call, Response<Tracks> response) {
                    if (response.isSuccessful()){
                        Tracks trackResponse = response.body();
                        List <Track> trackList1 = trackResponse.getTracks() ;
                        Log.i("get api success ",trackResponse.getTracks().get(0).getArtists().get(0).getName());
                        trackList.addAll(trackList1);
                        trackAdapter.notifyDataSetChanged();
                    }
                    else {
                        Log.i("get api un success", "error");

                    }
                }

                @Override
                public void onFailure(Call<Tracks> call, Throwable t) {
                    Log.i("fail to call api", t.getMessage());

                }
            });



        }
}