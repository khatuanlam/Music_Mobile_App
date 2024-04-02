package com.example.music_mobile_app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.network.mSpotifyService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.NewReleases;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit.client.Response;

public class HomeFragment extends Fragment {
    public static final String TAG = "Spotify HomeFragment";
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private mSpotifyService mSpotifyService = MainActivity.mSpotifyService;
    private RecyclerView recentlyTracksRecyclerView;
    private RecyclerView recommendationsRecyclerView;
    private RecyclerView topTracksRecyclerView;
    private RecyclerView albumsRecycleView;

    public final ListManager listManager = MainActivity.listManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager manager = getParentFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recentlyTracksRecyclerView = view.findViewById(R.id.recentlyTracks);
        recommendationsRecyclerView = view.findViewById(R.id.recommendation);
        topTracksRecyclerView = view.findViewById(R.id.top_tracks);
        albumsRecycleView = view.findViewById(R.id.top_albums);


        LinearLayoutManager recentlyTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager recommendTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager topTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager albums_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        recentlyTracksRecyclerView.setLayoutManager(recentlyTracks_layout);
        recommendationsRecyclerView.setLayoutManager(recommendTracks_layout);
        topTracksRecyclerView.setLayoutManager(topTracks_layout);
        albumsRecycleView.setLayoutManager(albums_layout);

        updateUI();

        // Show header
        RelativeLayout header = getParentFragment().getView().findViewById(R.id.header);
        header.setVisibility(View.VISIBLE);

        return view;
    }

    private void updateUI() {
        setRecommendations();
        setTopTracks();
//        setRecentlyTracks();
        setAlbums();
    }


    private void setRecentlyTracks() {

        List<Track> listTracks = listManager.getRecentlyTracks();
        mSpotifyService.getRecentlyTracks(new Callback<Pager<Track>>() {
            @Override
            public void onResponse(Call<Pager<Track>> call, retrofit2.Response<Pager<Track>> response) {
                if (response.isSuccessful()) {
                    List<Track> mList = response.body().items;
                    ItemAdapter adapter = new ItemAdapter(mList, null, getParentFragment());
                    recentlyTracksRecyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<Pager<Track>> call, Throwable t) {
                Log.e(TAG, "Cannot get recentlyTracks: " + t.getMessage());
            }
        });
    }

    private void setRecommendations() {
        List<Track> listTracks = listManager.getRecommendTracks();
        if (listTracks.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 10);
            options.put("seed_artists", "35HU1GT1q797EwZsP8uduO");
            options.put("seed_tracks", "67wUiBqwSYzf5GVvswMJ6p");

            spotifyService.getRecommendations(options, new SpotifyCallback<Recommendations>() {
                @Override
                public void success(Recommendations recommendations, Response response) {
                    Log.d(TAG, "Get recommend success");
                    List<Track> mList = recommendations.tracks;
                    listManager.setRecommendTracks(mList);
                    setRecommendations();
                }

                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "Cannot get recommend " + spotifyError.getMessage());
                }
            });
            Log.d(TAG, "setRecommendations: " + listTracks.size());
        } else {
            ItemAdapter adapter = new ItemAdapter(listTracks, new ArrayList<>(), getParentFragment());
            adapter.notifyDataSetChanged();
            recommendationsRecyclerView.setAdapter(adapter);
        }
    }

    private void setTopTracks() {
        List<Track> listTracks = listManager.getTopTracks();
        if (listTracks.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 10);
            spotifyService.getTopTracks(options, new SpotifyCallback<Pager<Track>>() {
                @Override
                public void success(Pager<Track> trackPager, Response response) {
                    Log.d(TAG, "Get top tracks");
                    List<Track> mList = trackPager.items;
                    listManager.setTopTracks(mList);
                    setTopTracks();
                }
                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "Can't get top track" + spotifyError.getMessage());
                }
            });
        } else {
            ItemAdapter adapter = new ItemAdapter(listTracks, new ArrayList<>(), getParentFragment());
            adapter.notifyDataSetChanged();
            topTracksRecyclerView.setAdapter(adapter);
        }
    }
    private void setAlbums() {

        List<AlbumSimple> listAlbums = listManager.getFavoriteAlbums();
        if (listAlbums.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 12);
            spotifyService.getNewReleases(options, new SpotifyCallback<NewReleases>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "Can't get album " + spotifyError.getMessage());
                }

                @Override
                public void success(NewReleases newReleases, Response response) {
                    Log.d(TAG, "Get new album ");
                    List<AlbumSimple> mList = newReleases.albums.items;
                    listManager.setAlbums(mList);
                    setAlbums();
                }
            });
        } else {
            ItemAdapter adapter = new ItemAdapter(new ArrayList<>(), listAlbums, getParentFragment());
            adapter.notifyDataSetChanged();
            albumsRecycleView.setAdapter(adapter);
        }

    }
}
