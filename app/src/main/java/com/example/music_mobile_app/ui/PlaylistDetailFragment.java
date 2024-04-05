package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.DetailPlaylistAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistDetailFragment extends Fragment {
    private static final String TAG = PlaylistDetailFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvName;
    private Button btnBack;
    private ImageView playlistImage;
    private DetailPlaylistAdapter adapter;
    private List<PlaylistTrack> playlistTracks;
    private String playlistId;
    SpotifyService spotifyService = MainActivity.spotifyService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_detail, container, false);

        playlistImage = view.findViewById(R.id.song_playlist_avt);
        tvName = view.findViewById(R.id.textSongName);
        btnBack = view.findViewById(R.id.BtnBack);
        recyclerView = view.findViewById(R.id.song_playlist_recyclerView);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            playlistId = bundle.getString("playlistId");
            String playlistName = bundle.getString("playlistName");
            String playlistImg = bundle.getString("playlistImage");

            tvName.setText(playlistName);
            Glide.with(this).load(playlistImg).into(playlistImage);
        }

        btnBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        prepareData();
        setPlaylist(playlistId);

        return view;
    }

    private void prepareData() {
        playlistTracks = new ArrayList<>();
        adapter = new DetailPlaylistAdapter(playlistTracks, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Đặt trình lắng nghe sự kiện long click cho adapter
        adapter.setOnItemLongClickListener(trackUri -> {
            // Hiển thị dialog xóa nhạc
            showConfirmationDialog(playlistId, trackUri);
        });
    }

    private void showConfirmationDialog(String playlistId, String trackUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa bài hát này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi phương thức để xóa bài hát
            removeTrackFromPlaylist(playlistId, trackUri);
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void setPlaylist(String playlistId) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");

        // Gọi service để lấy danh sách các track trong playlist
        MainActivity.spotifyService.getPlaylistTracks(USER_ID, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                Log.d(TAG, "Get playlist tracks success");
                playlistTracks.clear();
                playlistTracks.addAll(playlistTrackPager.items);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Get playlist tracks failed: " + error.getMessage());
            }
        });
    }

    private void removeTrackFromPlaylist(String playlistId, String trackUri) {
        Log.e(TAG, "trackUri: " + trackUri);
        // Tạo một instance của TracksToRemove và thêm các TrackToRemove vào danh sách
        TracksToRemove tracksToRemove = new TracksToRemove();
        tracksToRemove.tracks = new ArrayList<>();

        // Tạo một TrackToRemove và thêm trackId vào URI
        TrackToRemove trackToRemove = new TrackToRemove();
        trackToRemove.uri = trackUri;
        tracksToRemove.tracks.add(trackToRemove);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");

        // Gọi service để xóa bài hát khỏi playlist
        spotifyService.removeTracksFromPlaylist(USER_ID, playlistId, tracksToRemove, new Callback<SnapshotId>() {
            @Override
            public void success(SnapshotId snapshotId, Response response) {
                Log.d(TAG, "Remove track from playlist success");
                setPlaylist(playlistId); // Cập nhật lại danh sách track sau khi xóa
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
