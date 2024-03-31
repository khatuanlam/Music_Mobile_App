package com.example.music_mobile_app.ui;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;

import kaaes.spotify.webapi.android.SpotifyService;

public class PlaylistFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private SpotifyService spotifyService = MainActivity.spotifyService;

    private ImageView playlistImage;
    private TextView playlistName;
    private TextView playlistOwner;
    private RecyclerView recyclerView;

    public PlaylistFragment() {
    }

}
