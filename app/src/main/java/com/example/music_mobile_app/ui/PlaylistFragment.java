package com.example.music_mobile_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        return view;

    }
}
