package com.example.music_mobile_app.ui;


import static com.example.music_mobile_app.ui.HomeFragment.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.Service.RetrofitClient;
import com.example.music_mobile_app.manager.Service.SpotifyApiService;
import com.example.music_mobile_app.model.Page;
import com.example.music_mobile_app.model.SavedTrack;
import com.example.music_mobile_app.model.Track;

import java.util.ArrayList;
import java.util.List;

import com.example.music_mobile_app.adapter.TrackAdapter;

import kaaes.spotify.webapi.android.SpotifyApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment implements ListenerManager.TrackAdapterListener {

    private SpotifyApiService spotifyApiService;
    private FragmentManager manager;
    private RecyclerView recyclerView;
    private TrackAdapter trackAdapter;
    private SpotifyApi spotifyApi;
    private TextView Quantity;


    public List<Track> trackList = new ArrayList<Track>();
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
        retrofitClient = new RetrofitClient();
        spotifyApiService = retrofitClient.getClient();

        recyclerView = view.findViewById(R.id.recyclerMusicViewLiked);
        trackAdapter = new TrackAdapter(trackList, getContext());
        trackAdapter.setListener(this);
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        getTrackData( "1J3SmWwlYAG68LGKr86MVH?si=f01feeb87adb4273");
        getMyTrack();

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


//                    trackList.add((Track) trackPage.items.get(0).getTrack().getTracks());
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
                    getMyTrack();
                }
                else {
                    Toast.makeText(getContext(), " xoá bài hát không thành công",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("failure", "Unsuccessfull");
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
}

