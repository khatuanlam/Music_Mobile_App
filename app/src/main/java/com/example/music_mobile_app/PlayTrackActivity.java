package com.example.music_mobile_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import kaaes.spotify.webapi.android.models.Album;
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
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.PlaybackManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayTrackActivity extends FragmentActivity {

    private static final String TAG = PlayTrackActivity.class.getSimpleName();

    private ImageView btn_back, btn_prev, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options,
            btn_add_to_playlist;
    private TextView tv_track_name, tv_track_arist, tv_track_album, tv_currentTime, tv_endTime, tv_playerField;
    private RecyclerView recyclerView;
    private Button buttonAddPlaylist, buttonAddSong;
    private ConstraintLayout play_back_layout;
    private ShapeableImageView track_img;
    private AppCompatSeekBar mSeekBar;
    private TrackProgressBar mTrackProgressBar;
    private Track detailTrack;
    private Album detailAlbum;
    private PlaybackManager playbackManager;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private String selectedPlaylistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        // Prepared resources
        prepareData();

        // Get track detail
        detailTrack = getIntent().getParcelableExtra("Track");
        detailAlbum = getIntent().getParcelableExtra("Track's Album");
        setData(detailTrack);

        // Setting player UI
        playbackManager = PlaybackManager.getInstance(this, mPlayerStateCallback);

        // Onclick Back
        btn_back.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            play_back_layout.startAnimation(animation);
            onBackPressed();
            overridePendingTransition(0, 0);
        });

        btn_add_to_playlist.setOnClickListener(v -> {
            // Lấy ra TrackID
            String trackID = detailTrack.id;
            Log.d(TAG, "Track ID: " + trackID);
            showAddToPlaylist();
        });
    }

    private void showAddToPlaylist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_playlist, null);

        builder.setView(addView);

        // Tạo adapter và gán cho RecyclerView
        recyclerView = addView.findViewById(R.id.playlist_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
        buttonAddPlaylist = addView.findViewById(R.id.buttonAddPlaylist);
        buttonAddSong = addView.findViewById(R.id.buttonAddSong);

        AlertDialog alertDialog = builder.create();
        setPlaylist(ListManager.getInstance().getPlaylistList());


        // Create playlist
        buttonAddPlaylist.setOnClickListener(v -> {
            Log.e("add playlist", "click");
            showAddDialog();
        });

        alertDialog.show();

        buttonAddSong.setOnClickListener(v -> {
            if (selectedPlaylistId != null) { // Kiểm tra xem đã chọn playlist chưa
                Log.d(TAG, "Success!!!!");
                addToPlaylist(selectedPlaylistId, detailTrack);
                Toast.makeText(this, "Add song playlist successful!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "fail");
                // Thông báo cho người dùng rằng họ cần chọn một playlist trước khi thêm bài hát
                Toast.makeText(PlayTrackActivity.this, "Please select a playlist first", Toast.LENGTH_SHORT).show();
            }
            alertDialog.dismiss();
        });
    }

    private void setPlaylist(List<PlaylistSimple> playlistList) {
        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), null, playlistList, this, null);
        adapter.setSend(true);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    // Xử lý dữ liệu được truyền từ Adapter
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(PlaylistSimple playlistSimple) {
        selectedPlaylistId = playlistSimple.id;
        String name = playlistSimple.name;
        Toast.makeText(this, "Clicked item: " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        playbackManager.Disconnect();
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
        tv_currentTime = findViewById(R.id.tv_currentTime);
        tv_endTime = findViewById(R.id.tv_durationTime);
        tv_playerField = findViewById(R.id.playerField);
        mSeekBar = findViewById(R.id.seek_bar);

        play_back_layout = findViewById(R.id.play_back_layout);
        track_img = findViewById(R.id.track_img);
        // tv_endTime.setText();

        mTrackProgressBar = new TrackProgressBar(mSeekBar);
        // mSeekBar.setMax((int) playerState.track.duration)

    }

    private void setData(Track detailTrack) {

        tv_track_name.setText(detailTrack.name);
        tv_track_album.setText((detailTrack.album.name != null) ? detailTrack.album.name : detailAlbum.name);
        tv_track_arist.setText(detailTrack.artists.get(0).name);

        // Get Track's Album
        Album album = getIntent().getParcelableExtra("Track's Album");
        Glide.with(this).load((detailTrack.album.images.get(0).url == null) ? album.images.get(0).url
                : detailTrack.album.images.get(0).url).override(Target.SIZE_ORIGINAL).into(track_img);
    }

    private Subscription.EventCallback<PlayerState> mPlayerStateCallback = new Subscription.EventCallback<PlayerState>() {
        @Override
        public void onEvent(PlayerState playerState) {

            if (playerState.track.name != detailTrack.name || playerState.track == null) {
                playbackManager.play(detailTrack.uri);
            }

            btn_play.setOnClickListener(v -> {
                playbackManager.onPlayPauseButtonClicked();
            });

            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            tv_endTime.setText(formatter.format(playerState.track.duration));
            tv_currentTime.setText(formatter.format(playerState.playbackPosition));

            // Invalidate seekbar length and position
            mSeekBar.setMax((int) playerState.track.duration);
            mTrackProgressBar.setDuration(playerState.track.duration);
            mTrackProgressBar.update(playerState.playbackPosition);
            if (playerState.isPaused) {
                btn_play.setImageResource(R.drawable.ic_play_white_48dp);
            } else {
                btn_play.setImageResource(R.drawable.ic_pause_white_24dp);
            }

        }
    };

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

        private void setDuration(long duration) {
            mSeekBar.setMax((int) duration);
        }

        private void update(long progress) {
            mSeekBar.setProgress((int) progress);
        }

        private void pause() {
            mHandler.removeCallbacks(mSeekRunnable);
        }

        private void unpause() {
            mHandler.removeCallbacks(mSeekRunnable);
            mHandler.postDelayed(mSeekRunnable, LOOP_DURATION);
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
                playbackManager.getPlayerApi().seekTo(seekBar.getProgress()).setErrorCallback(throwable -> {
                    // // Xử lý lỗi khi seekTo() gặp vấn đề
                    Toast.makeText(PlayTrackActivity.this, "Error while seeking: " + throwable.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    // // Ví dụ: log lỗi
                    Log.e("SeekBarChangeListener", "Error while seeking", throwable);
                });
            }
        };
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
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating playlist on Spotify: " + error.getMessage());

            }
        });
    }

    private void addToPlaylist(String selectedPlaylistId, Track mTrack) {

        Map<String, Object> options = new HashMap<>();
        Map<String, Object> bodyParams = new HashMap<>();
        List<String> uris = new ArrayList<>();
        uris.add(mTrack.uri); // Thêm URI của bài hát vào danh sách
        bodyParams.put("uris", uris);
//        bodyParams.put("position", ListManager.getInstance().getPlaylistList().size() + 1);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        // Gọi API để thêm bài hát vào playlist
        spotifyService.addTracksToPlaylist(USER_ID, selectedPlaylistId, options, bodyParams,
                new SpotifyCallback<Pager<PlaylistTrack>>() {
                    @Override
                    public void failure(SpotifyError spotifyError) {
                        Log.e(TAG, "Error creating song playlist on Spotify: " + spotifyError.getMessage());
                    }

                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        Log.d(TAG, "Create song playlist success");

                    }
                });
    }
}