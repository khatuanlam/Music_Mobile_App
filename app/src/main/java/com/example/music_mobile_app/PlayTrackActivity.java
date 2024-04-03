package com.example.music_mobile_app;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.manager.PlaybackManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.PlayerState;

public class PlayTrackActivity extends FragmentActivity {

    private static final String TAG = PlayTrackActivity.class.getSimpleName();

    private ImageView btn_back, btn_prev, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options, btn_add_to_playlist;
    private TextView tv_track_name, tv_track_arist, tv_track_album, tv_currentTime, tv_endTime, tv_playerField;
    private ConstraintLayout play_back_layout;
    private ShapeableImageView track_img;
    private AppCompatSeekBar mSeekBar;
    private TrackProgressBar mTrackProgressBar;
    private Track detailTrack;
    private Album detailAlbum;

    private static PlayerState playerState;

    private static PlaybackManager playbackManager;

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

        btn_play.setOnClickListener(v -> {
            playbackManager.play(detailTrack.uri);
            btn_play.setImageResource(R.drawable.ic_pause_white_24dp);
        });


        // Onclick Back
        btn_back.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            play_back_layout.startAnimation(animation);
            onBackPressed();
            overridePendingTransition(0, 0);
//            tv_playerField.setVisibility(View.VISIBLE);
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
        tv_currentTime = findViewById(R.id.tv_currentTime);
        tv_endTime = findViewById(R.id.tv_durationTime);
        tv_playerField = findViewById(R.id.playerField);

        play_back_layout = findViewById(R.id.play_back_layout);
        track_img = findViewById(R.id.track_img);

        mSeekBar = findViewById(R.id.seek_bar);
        mSeekBar.setEnabled(false);
        mSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mSeekBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        // Setting player UI
        playbackManager = PlaybackManager.getInstance(this);
        mTrackProgressBar = new TrackProgressBar(mSeekBar);
//        mSeekBar.setMax((int) playerState.track.duration);
    }

    private void setData(Track detailTrack) {

        tv_track_name.setText(detailTrack.name);
        tv_track_album.setText((detailTrack.album.name != null) ? detailTrack.album.name : detailAlbum.name);
        tv_track_arist.setText(detailTrack.artists.get(0).name);

        // Get Track's Album
        Album album = getIntent().getParcelableExtra("Track's Album");
        Glide.with(this)
                .load((detailTrack.album.images.get(0).url == null) ? album.images.get(0).url : detailTrack.album.images.get(0).url)
                .override(Target.SIZE_ORIGINAL).into(track_img);
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
                playbackManager.getPlayerApi().seekTo(seekBar.getProgress());
            }
        };
    }


}
