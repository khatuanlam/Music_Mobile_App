package com.example.music_mobile_app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.RecentlyTracksAdapter;
import com.example.music_mobile_app.adapter.RecommendAdapter;
import com.example.music_mobile_app.adapter.TopTracksAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.network.mSpotifyService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
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
    private MethodsManager methodsManager;

    final ListManager listManager = ListManager.getInstance();
    private static RecommendAdapter recommendAdapter;
    private static TopTracksAdapter topTracksAdapter;

    private static RecentlyTracksAdapter recentlyTracksAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager manager = getParentFragmentManager();
        methodsManager = new MethodsManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recentlyTracksRecyclerView = view.findViewById(R.id.recentlyTracks);
        recommendationsRecyclerView = view.findViewById(R.id.recommendation);
        topTracksRecyclerView = view.findViewById(R.id.top_tracks);

        LinearLayoutManager recentlyTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager recommendTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager topTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recentlyTracksRecyclerView.setLayoutManager(recentlyTracks_layout);
        recommendationsRecyclerView.setLayoutManager(recommendTracks_layout);
        topTracksRecyclerView.setLayoutManager(topTracks_layout);

        setRecommendations();
        setTopTracks();
        setRecentlyTracks();

        return view;
    }

    private void setRecentlyTracks() {

        mSpotifyService.getRecentlyTracks(new Callback<Pager<Track>>() {

            @Override
            public void onResponse(Call<Pager<Track>> call, retrofit2.Response<Pager<Track>> response) {
                if (response.isSuccessful()) {
                    List<Track> mList = response.body().items;
                    RecentlyTracksAdapter recentlyTracksAdapter = new RecentlyTracksAdapter(mList, getParentFragment());
                    recentlyTracksRecyclerView.setAdapter(recentlyTracksAdapter);
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
                    Log.e(TAG, "Cannot get recommend " + spotifyError.getErrorDetails());
                }
            });
            Log.d(TAG, "setRecommendations: " + listTracks.size());
        }
        recommendAdapter = new RecommendAdapter(listTracks, getParentFragment());
        recommendAdapter.notifyDataSetChanged();

        recommendationsRecyclerView.setAdapter(recommendAdapter);
    }

    private void setTopTracks() {

        List<Track> listTracks = listManager.getTopTracks();

        if (listTracks.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 10);
            spotifyService.getTopTracks(options, new SpotifyCallback<Pager<Track>>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, spotifyError.getErrorDetails().message);
                }

                @Override
                public void success(Pager<Track> trackPager, Response response) {
                    Log.d(TAG, "Get top tracks");
                    List<Track> mList = trackPager.items;
                    listManager.setTopTracks(mList);
                    setTopTracks();
                }
            });
        }
        topTracksAdapter = new TopTracksAdapter(listTracks, getParentFragment());
        topTracksAdapter.notifyDataSetChanged();

        topTracksRecyclerView.setAdapter(topTracksAdapter);
    }

}