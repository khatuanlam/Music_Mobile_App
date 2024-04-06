package com.example.music_mobile_app.ui;


import static com.example.music_mobile_app.ui.HomeFragment.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.AlbumAdapter;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.Service.RetrofitClient;
import com.example.music_mobile_app.manager.Service.SpotifyApiService;
import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Page;
import com.example.music_mobile_app.model.SavedAlbum;
import com.example.music_mobile_app.model.SavedTrack;
import com.example.music_mobile_app.model.Track;

import java.util.ArrayList;
import java.util.List;

import com.example.music_mobile_app.adapter.TrackAdapter;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment implements ListenerManager.TrackAdapterListener, ListenerManager.AlbumAdapterListener {

    private SpotifyApiService spotifyApiService;
    private SpotifyService spotifyService = MainActivity.spotifyService;

    private FragmentManager manager;
    private RecyclerView recyclerView , albumRecyclerView;
    private TrackAdapter trackAdapter;
    private AlbumAdapter albumAdapter;
    private SpotifyApi spotifyApi;
    private TextView Quantity;
    private Button albumBtn, trackBtn;


    public List<Track> trackList = new ArrayList<Track>();
    public List<Album> albumList = new ArrayList<Album>();
    private RetrofitClient retrofitClient;

    public FavoriteFragment() {

    }

    public FavoriteFragment newInstance() {
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
        Quantity = view.findViewById(R.id.quantity);
        albumBtn = view.findViewById(R.id.switch_album_favourite);
        trackBtn = view.findViewById(R.id.switch_track_favourite);

        retrofitClient = new RetrofitClient();
        spotifyApiService = retrofitClient.getClient();

        recyclerView = view.findViewById(R.id.recyclerMusicViewLiked);
        albumRecyclerView = view.findViewById(R.id.recyclerAlbumViewLiked);


        trackAdapter = new TrackAdapter(trackList, getContext());
        trackAdapter.setListener(this);
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        albumAdapter = new AlbumAdapter(albumList, getContext());
        albumAdapter.setListener(this);
        albumRecyclerView.setAdapter(albumAdapter);
        albumRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        albumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlbumRecyclerView();
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrackRecyclerView();
            }
        });
//        showTrackRecyclerView();

        showAlbumRecyclerView();

        return view;
    }

    private void showAlbumRecyclerView(){
        recyclerView.setVisibility(View.GONE);
        albumRecyclerView.setVisibility(View.VISIBLE);
        albumList.clear();
        getMyAlbum();
    }
    private  void showTrackRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        albumRecyclerView.setVisibility(View.GONE);
        trackList.clear();
        getMyTrack();
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

    public void getMyTrack(){
        Call<Page<SavedTrack>> call = spotifyApiService.getMyTrack(50,0);

        call.enqueue(new Callback<Page<SavedTrack>>() {
            @Override
            public void onResponse(Call<Page<SavedTrack>> call, Response<Page<SavedTrack>> response) {
                if (response.isSuccessful()) {
                    Page<SavedTrack> trackPage = response.body();
                    List<SavedTrack> savedTracks = trackPage.getItems();
                    Quantity.setText(String.valueOf(trackPage.total) + " bài hát");
                    for (SavedTrack savedTrack : savedTracks) {
                        Track track1 = savedTrack.getTrack();
                        if (track1 != null) {
                            trackList.add(track1);
                        } else {
                            Log.e(TAG, "Track object is null");
                        }

                    }

                    Track newtrack = trackPage.getItems().get(0).getTrack();
                    Log.d(TAG, "onResponse() returned: " + newtrack.getName());
                    trackAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Page<SavedTrack>> call, Throwable t) {
                Log.i("get api", "Unsuccessfull");
            }
        });

    }
    public void deleteTrackfromFavorite(String id){
        Call<Void> call = spotifyApiService.deleteTrack(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("ok", "successfull");
                    Toast.makeText(getContext(), " xoá bài hát thành công",Toast.LENGTH_SHORT).show();
                    trackAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(getContext(), " xoá bài hát không thành công",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("failure", "Unsuccessfull" + t.getMessage());
            }
        });
    }


    public void getMyAlbum(){
        Call<Page<SavedAlbum>> call = spotifyApiService.getMyAlbum(5,0);
        call.enqueue(new Callback<Page<SavedAlbum>>() {
            @Override
            public void onResponse(Call<Page<SavedAlbum>> call, Response<Page<SavedAlbum>> response) {
                if (response.isSuccessful()) {
                    Page<SavedAlbum> AlbumPage = response.body();
                    List<SavedAlbum> savedAlbums = AlbumPage.getItems();
                    Quantity.setText(String.valueOf(AlbumPage.total) + " album");

                    for (SavedAlbum savedAlbum : savedAlbums) {
                        Album album = savedAlbum.getAlbum();
                        if (album != null) {
                            albumList.add(album);
                        } else {
                            Log.e(TAG, "album object is null");
                        }
                    }
                    albumAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<Page<SavedAlbum>> call, Throwable t) {
                    Log.i("get api", "Unsuccessfull" + t.getMessage());

            }
        });
    }

    public void deleteAlbumfromFavorite(String albumID){
        Call<Void> call = spotifyApiService.removeFromMyAlbums(albumID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("ok", "successfull");
                    Toast.makeText(getContext(), " xoá album thành công",Toast.LENGTH_SHORT).show();
                    albumAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(getContext(), " xoá album không thành công",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("failure", "Unsuccessfull" + t.getMessage());

            }
        });
    }

    @Override
    public void onDeleteTrackClicked(String trackId) {
        int position = findTrackPositionById(trackId);
        if (position != -1) {
            trackList.remove(position); // Xoá track khỏi danh sách
            trackAdapter.notifyItemRemoved(position); // Cập nhật RecyclerView
        }
        deleteTrackfromFavorite(trackId);
    }

    @Override
    public int findTrackPositionById(String trackId) {
        for (int i = 0; i < trackList.size(); i++) {
            Track track = trackList.get(i);
            if (track.getId().equals(trackId)) {
                return i; // Trả về vị trí nếu tìm thấy track có id tương ứng
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    @Override
    public void onDeleteAlbumClicked(String albumId) {
        int position = findAlbumPositionById(albumId);
        if (position != -1) {
            albumList.remove(position); // Xoá track khỏi danh sách
            albumAdapter.notifyItemRemoved(position); // Cập nhật RecyclerView
        }
        deleteAlbumfromFavorite(albumId);
    }

    @Override
    public int findAlbumPositionById(String albumID) {
        for (int i = 0; i < albumList.size(); i++) {
            Album album = albumList.get(i);
            if (album.getId().equals(albumID)) {
                return i; // Trả về vị trí nếu tìm thấy album có id tương ứng
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}

