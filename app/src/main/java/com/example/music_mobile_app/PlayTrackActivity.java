package com.example.music_mobile_app;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;
//import com.spotify.android.appremote.api.AppRemote;


//import com.spotify.protocol.types.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayTrackActivity extends FragmentActivity {

    private static final String CONNECTION_PARAMS_KEY = "connectionParamsKey";

    private static final String TAG = PlayTrackActivity.class.getSimpleName();

    ImageView btn_back, btn_prev, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options, btn_add_to_playlist;
    TextView tv_track_name, tv_track_arist, tv_track_album;
    ConstraintLayout play_back_layout;
    ShapeableImageView track_img;
    AppCompatSeekBar mSeekBar;

    TrackProgressBar mTrackProgressBar;

    private Track detailTrack;

    SpotifyAppRemote mSpotifyAppRemote;


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


//        if (mPlayer == null) {
//            Config playerConfig = new Config(this, authToken, LoginActivity.CLIENT_ID);
//
//            Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
//
//                @Override
//                public void onInitialized(SpotifyPlayer spotifyPlayer) {
//                    Log.d(TAG, "-- Player initialized --");
//                    mPlayer = spotifyPlayer;
//                    mPlayer.addConnectionStateCallback(PlayTrackActivity.this);
//                    btn_play.setOnClickListener(v -> {
//                        mPlayer.playUri(new Player.OperationCallback() {
//                            @Override
//                            public void onSuccess() {
//                                logMessage("onSuccess: ", 10);
//                            }
//
//                            @Override
//                            public void onError(Error error) {
//                                logMessage(error + "", 10);
//                            }
//                        }, detailTrack.uri, 0, 0);
//                    });
//
//                    logMessage("pppp", 10);
//
//                    Log.d(TAG, "AccessToken: " + authToken);
//                    // Set API
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    Log.e(TAG, "Could not initialize player: " + throwable.getMessage());
//                }
//            });
//        } else {
//            mPlayer.login(authToken);
//        }

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
            // Kiểm tra và xử lý URL hình ảnh trước khi truyền qua intent
            if (detailTrack != null && detailTrack.album != null && !detailTrack.album.images.isEmpty()) {
                String imageUrl = detailTrack.album.images.get(0).url;
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    String trackId = detailTrack.id;
                    // Log ID của bài hát để kiểm tra
                    Log.d(TAG, "Track ID: " + trackId);
                    Log.e(TAG, "OK!!");
                    // Giờ bạn có URL hình ảnh hợp lệ, bạn có thể truyền nó qua intent
                    Intent intent = new Intent(PlayTrackActivity.this, PlaylistActivity.class);
                    intent.putExtra("trackID", detailTrack.id);
                    intent.putExtra("trackName", tv_track_name.getText().toString());
                    intent.putExtra("trackImage", imageUrl);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Image URL is null or empty");
                }
            } else {
                Log.e(TAG, "Detail track or album is null or empty");
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

//        // Get connectionParams
//        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", MODE_PRIVATE);
//        Gson gson = new Gson();
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


//        private final ErrorCallback mErrorCallback = this :: logError;

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
}
