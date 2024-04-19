package com.example.music_mobile_app.ui.extension.song;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.ListSongAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.MainFragment;
import com.example.music_mobile_app.viewmodel.TopPopularSongViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class AllPopularSongsFragment extends Fragment {

    private InterstitialAd mInterstitialAd;
    public static String TAG = "Mobile Ads";
    private TopPopularSongViewModel topPopularSongViewModel;
    private AllPlaylistViewModel allPlaylistViewModel;

    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private TextView textView;
    private ImageView imageViewBack;
    private FragmentManager manager;

    private ListSongAdapter listSongAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    private long userId;
    public LiteSongRepository liteSongRepository;

    public AllPopularSongsFragment(long id, LiteSongRepository liteSongRepository) {
        this.userId = id;
        this.liteSongRepository = liteSongRepository;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_all_popular_songs, container, false);
        imageViewBack = view.findViewById(R.id.mydb_all_popular_songs_fragment_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });
        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_all_popular_songs_fragment_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        topPopularSongViewModel = new ViewModelProvider(this).get(TopPopularSongViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);
        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);

        listSongAdapter = new ListSongAdapter(getActivity(), this, manager, new ArrayList<Song>(), favoriteSongsViewModel, userId, songsOfPlaylistViewModel, "Popular Song", null, liteSongRepository);
        songOfAlbumRecyclerView.setAdapter(listSongAdapter);

        topPopularSongViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                listSongAdapter.setmDataList(songs);
                Log.i("FAVORITE", "CAP NHAT");
                Log.i("FAVORITE", String.valueOf(songs.size()));
            }
        });
        favoriteSongsViewModel.getIsPostSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleteSuccess) {
                if (isDeleteSuccess == null)
                    return;
                if (isDeleteSuccess) {
                    Toast.makeText(getContext(), "ĐÃ THÊM VÀO DANH SÁCH YÊU THÍCH", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        songsOfPlaylistViewModel.getIsPostSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPostSuccess) {
                if (isPostSuccess == null)
                    return;
                if (isPostSuccess) {
                    Toast.makeText(getContext(), "ĐÃ THÊM VÀO PLAYLIST", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        allPlaylistViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                listSongAdapter.setOpenPlaylists(playlists);
            }
        });

        topPopularSongViewModel.loadSong();
        allPlaylistViewModel.getAllPlaylistsByIdUser(userId);


        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show((Activity) requireContext());
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad dismissed fullscreen content.");
//                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        topPopularSongViewModel.getSongs().removeObservers(this);
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
        songsOfPlaylistViewModel.getIsPostSuccess().removeObservers(this);
    }
}

