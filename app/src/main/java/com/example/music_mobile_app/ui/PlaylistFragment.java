package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private ImageView playlistImage, btnBack;
    private TextView playlistName, playlistOwner;

    private RecyclerView recyclerView;
    private FragmentManager manager;

    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    private PlaylistSimple playlistDetail;

    public PlaylistFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get detail playlist
        Bundle bundle = getArguments();
        if (bundle != null) {
            playlistDetail = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
        } else {
            Log.e(TAG, "Cannot get playlist detail");
        }

        manager = getParentFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // Hide header
        RelativeLayout header = getParentFragment().getView().findViewById(R.id.header);
        header.setVisibility(View.GONE);

        preparedData(view);

        // Onclick back
        btnBack.setOnClickListener(v -> {
            manager.popBackStack();
        });

        setPlaylist();

        return view;

    }

    private void preparedData(View view) {

        playlistImage = view.findViewById(R.id.playlistImage);
        playlistName = view.findViewById(R.id.playlistName);
        playlistOwner = view.findViewById(R.id.playlistOwner);
        recyclerView = view.findViewById(R.id.playlist_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        btnBack = view.findViewById(R.id.backButton);

        playlistName.setText(playlistDetail.name);
        playlistOwner.setText(playlistDetail.owner.display_name);
        Glide.with(this).load((playlistDetail.images.isEmpty()) ? baseImage : playlistDetail.images.get(0).url)
                .override(Target.SIZE_ORIGINAL).into(playlistImage);

    }

    private void setPlaylist() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            PlaylistSimple playlistDetail = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            List<Track> trackList = new ArrayList<>();
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof kaaes.spotify.webapi.android.models.Track) {
                    trackList.add((Track) parcelable);
                }
            }
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(),
                    getParentFragment());
            adapter.setSend(true);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "Cannot get album detail");
        }
    }

    private void removeTrackFromPlaylist(String playlistId, Track mTrack) {
        // Tạo một instance của TracksToRemove và thêm các TrackToRemove vào danh sách
        TracksToRemove tracksToRemove = new TracksToRemove();
        tracksToRemove.tracks = new ArrayList<>();

        // Tạo một TrackToRemove và thêm trackId vào URI
        TrackToRemove trackToRemove = new TrackToRemove();
        trackToRemove.uri = mTrack.uri;
        tracksToRemove.tracks.add(trackToRemove);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");

        // Gọi service để xóa bài hát khỏi playlist
        spotifyService.removeTracksFromPlaylist(USER_ID, playlistId, tracksToRemove, new Callback<SnapshotId>() {
            @Override
            public void success(SnapshotId snapshotId, Response response) {
                Log.d(TAG, "Remove track from playlist success");
                Toast.makeText(getContext(), "Đã xóa track thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Remove track from playlist failed: " + error.getMessage());
                Toast.makeText(getContext(), "Xóa track thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
