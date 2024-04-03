package com.example.music_mobile_app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.spotify.protocol.types.Track;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;

public class PlaylistFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private ImageView playlistImage;
    private TextView playlistName;
    private TextView playlistOwner;
    private RecyclerView recyclerView;
    private PlaylistSimple playlistDetail;

    public PlaylistFragment() {
        // Get detail playlist
        Bundle bundle = getArguments();
        if (bundle != null) {
            PlaylistSimple playlistSimple = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
        } else {
            Log.e(TAG, "Cannot get playlist detail");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        //Hide header
        RelativeLayout header = getParentFragment().getView().findViewById(R.id.header);
        header.setVisibility(View.GONE);

        return view;

    }


    private void preparedData(View view) {
        playlistImage = view.findViewById(R.id.playlistImage);
        playlistName = view.findViewById(R.id.playlistName);
        playlistOwner = view.findViewById(R.id.playlistOwner);
    }
}
