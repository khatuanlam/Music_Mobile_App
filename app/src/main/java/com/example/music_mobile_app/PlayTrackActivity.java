package com.example.music_mobile_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.adapter.PlayTrackAdapter;
import com.example.music_mobile_app.adapter.PlayTrackAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.ui.PlaylistDetailFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayTrackActivity extends FragmentActivity implements PlayTrackAdapter.OnPlaylistClickListener {

    private static final String CONNECTION_PARAMS_KEY = "connectionParamsKey";
    private static final String TAG = PlayTrackActivity.class.getSimpleName();

    ImageView btn_back, btn_prev, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options, btn_add_to_playlist;
    TextView tv_track_name, tv_track_arist, tv_track_album;
    ConstraintLayout play_back_layout;
    ShapeableImageView track_img;
    AppCompatSeekBar mSeekBar;
    RecyclerView recyclerView;
    Button buttonAddPlaylist, buttonAddSong, buttonBack;
    TrackProgressBar mTrackProgressBar;
    ListManager listManager = MainActivity.listManager;
    SpotifyService spotifyService = MainActivity.spotifyService;
    Track detailTrack;
    SpotifyAppRemote mSpotifyAppRemote;
    private String selectedPlaylistId, trackID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);

        // Prepared resources
        prepareData();
        detailTrack = getIntent().getParcelableExtra("Track");
        setData(detailTrack);

        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("AUTH_TOKEN", "");

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(AuthLoginActivity.CLIENT_ID)
                        .setRedirectUri(AuthLoginActivity.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        // Play a playlist
                        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });


        btn_back.setOnClickListener(v ->
        {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            play_back_layout.startAnimation(animation);
            onBackPressed();
            overridePendingTransition(0, 0);
        });

        btn_add_to_playlist.setOnClickListener(v -> {
            // Lấy ra TrackID
            trackID = detailTrack.id;
            Log.d(TAG, "Track ID: " + trackID);
            showPlaylistDialog();
        });
    }

    @Override
    public void onPlaylistClick(String playlistId) {
        Log.d(TAG, "Clicked playlistId: " + playlistId);
        selectedPlaylistId = playlistId; // Lưu playlistId được chọn
    }

    private void showPlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_playlist, null);

        builder.setView(addView);


        // Tạo adapter và gán cho RecyclerView
        recyclerView = addView.findViewById(R.id.playlist_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlayTrackAdapter adapter = new PlayTrackAdapter(new ArrayList<>(), new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        buttonAddPlaylist = addView.findViewById(R.id.buttonAddPlaylist);
        buttonAddSong = addView.findViewById(R.id.buttonAddSong);
//        buttonBack = addView.findViewById(R.id.BtnBackPlaylist);

        AlertDialog alertDialog = builder.create();
        setPlaylist(); // Gọi hàm này sau khi gán adapter cho RecyclerView

        // Create playlist
        buttonAddPlaylist.setOnClickListener(v -> {
            Log.e("add playlist", "click");
            showAddDialog();
        });

//        buttonBack.setOnClickListener(v -> {
//            alertDialog.dismiss();
//        });
        alertDialog.show();

        buttonAddSong.setOnClickListener(v -> {
            if (selectedPlaylistId != null) { // Kiểm tra xem đã chọn playlist chưa
                Log.d(TAG, "Success!!!!");
                Log.d(TAG, "PlaylistID:" + selectedPlaylistId);
                Log.d(TAG, "Track ID: " + trackID);

                createSongPlaylistOnSpotify(selectedPlaylistId, trackID);
                Toast.makeText(PlayTrackActivity.this, "Add song playlist successful!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "fail");
                // Thông báo cho người dùng rằng họ cần chọn một playlist trước khi thêm bài hát
                Toast.makeText(PlayTrackActivity.this, "Please select a playlist first", Toast.LENGTH_SHORT).show();
            }
            alertDialog.dismiss();
        });
    }

    private void setupRecyclerView(List<PlaylistSimple> playlistList) {
        PlayTrackAdapter adapter = new PlayTrackAdapter(new ArrayList<>(), playlistList, this);
        adapter.setOnPlaylistClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void setPlaylist() {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 6);
        spotifyService.getMyPlaylists(options, new SpotifyCallback<Pager<PlaylistSimple>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.e(TAG, "failure: " + spotifyError.getErrorDetails());
            }

            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                Log.d(TAG, "Get playlist success: ");
                List<PlaylistSimple> mList = playlistSimplePager.items;
                setupRecyclerView(mList);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @SuppressLint("ResourceType") // Ignore all illegal resources
    private void prepareData() {

        btn_back = findViewById(R.id.btn_back);
        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);
        btn_play = findViewById(R.id.btn_play);
        btn_shuffle = findViewById(R.id.btn_shuffle);
        btn_replay = findViewById(R.id.btn_replay);
        btn_track_options = findViewById(R.id.track_options);
        btn_add_to_playlist = findViewById(R.id.btn_add_to_playlist);

        tv_track_name = findViewById(R.id.track_name);
        tv_track_album = findViewById(R.id.track_album);
        tv_track_arist = findViewById(R.id.track_artist);

        play_back_layout = findViewById(R.id.play_back_layout);
        track_img = findViewById(R.id.track_img);

        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setEnabled(false);
        mSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mSeekBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        // Get connectionParams
        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", MODE_PRIVATE);
        Gson gson = new Gson();
    }

    private void setData(Track detailTrack) {
        tv_track_name.setText(detailTrack.name);
        tv_track_album.setText(detailTrack.album.name);
        tv_track_arist.setText(detailTrack.artists.get(0).name);
        Glide.with(this).load(detailTrack.album.images.get(0).url).override(Target.SIZE_ORIGINAL).into(track_img);
    }

    // TrackProcessBar Class
    private class TrackProgressBar {
        private static final int LOOP_DURATION = 500;
        private final AppCompatSeekBar mSeekBar;

        // Lớp Handler để thực hiện tương tác hoặc cập nhật UI từ các luồng khác nhau
        private final Handler mHandler;

        private final Runnable mSeekRunnable = new Runnable() {
            @Override
            public void run() {
                int progress = mSeekBar.getProgress();
                mSeekBar.setProgress(progress + LOOP_DURATION);
                mHandler.postDelayed(mSeekRunnable, LOOP_DURATION);
            }
        };


        private TrackProgressBar(AppCompatSeekBar mSeekBar) {
            this.mSeekBar = mSeekBar;
            mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
            mHandler = new Handler();
        }

        private final SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
    }

    private void logMessage(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
        Log.d(TAG, msg);
    }

    private void logError(Throwable throwable) {
        Toast.makeText(this, "logError", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "", throwable);
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_new_playlist, null);

        builder.setView(addView);

        EditText editText = addView.findViewById(R.id.name_playlist);
        Button btnCreate = addView.findViewById(R.id.btnCreate);
        Button btnExit = addView.findViewById(R.id.btnExit);

        AlertDialog alertDialog = builder.create();

        // Set click listener
        btnCreate.setOnClickListener(v -> {
            // Lấy văn bản từ EditText
            String playlistName = editText.getText().toString();
            createPlaylistOnSpotify(playlistName);
            alertDialog.dismiss();
        });

        btnExit.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void createPlaylistOnSpotify(String playlistName) {

        // Tạo yêu cầu tạo playlist mới
        Map<String, Object> options = new HashMap<>();
        options.put("name", playlistName);
        options.put("public", true);

        // Get user information
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        spotifyService.createPlaylist(USER_ID, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                // Lấy ID của playlist mới được tạo
                String playlistID = playlist.id;
                Log.d(TAG, "Create playlist success");
                // Reload playlist
                setPlaylist();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating playlist on Spotify: " + error.getMessage());
            }
        });
    }

    private void createSongPlaylistOnSpotify(String selectedPlaylistId, String trackID) {
        // Tạo tham số cho yêu cầu POST
        Map<String, Object> queryParams = new HashMap<>();

        Map<String, Object> bodyParams = new HashMap<>();
        List<String> uris = new ArrayList<>();
        uris.add("spotify:track:" + trackID); // Thêm URI của bài hát vào danh sách
        bodyParams.put("uris", uris);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
//         Gọi API để thêm bài hát vào playlist
        spotifyService.addTracksToPlaylist(USER_ID, selectedPlaylistId, queryParams, bodyParams, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                Log.d(TAG, "Create song playlist success");
            }

            @Override
            public void failure(RetrofitError error) {
                // Xử lý khi gặp lỗi
                Log.e(TAG, "Error creating song playlist on Spotify: " + error.getMessage());
            }
        });
    }
}