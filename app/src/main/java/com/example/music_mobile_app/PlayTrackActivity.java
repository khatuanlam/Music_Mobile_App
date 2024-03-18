package com.example.music_mobile_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import kaaes.spotify.webapi.android.models.Track;

public class PlayTrackActivity extends AppCompatActivity {

    ImageView btn_back;

    TextView track_name, track_arist, track_album;

    private ConstraintLayout play_back_layout;

    private Track detailTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_play_track);

        // Prepared resources
        prepareData();

        detailTrack = getIntent().getParcelableExtra("Track");
        setData(detailTrack);

        // Set listeners
        btn_back.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            play_back_layout.startAnimation(animation);
            onBackPressed();
            overridePendingTransition(0, 0);
        });
    }

    @SuppressLint("ResourceType") // Ignore all illegal resources
    private void prepareData() {
        // Button
        btn_back = findViewById(R.id.btn_back);

        //TextView
        track_name = findViewById(R.id.track_name);
        track_album = findViewById(R.id.track_album);
        track_arist = findViewById(R.id.track_artist);

        // Layout
        play_back_layout = findViewById(R.id.play_back_layout);

    }

    private void setData(Track detailTrack) {
        track_name.setText(detailTrack.name);
        track_album.setText(detailTrack.album.name);
        track_arist.setText(detailTrack.artists.get(0).name);
    }

}