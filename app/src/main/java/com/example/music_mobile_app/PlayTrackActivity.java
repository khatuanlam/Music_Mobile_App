package com.example.music_mobile_app;

import static com.example.music_mobile_app.MainActivity.listManager;
import static com.example.music_mobile_app.MainActivity.mSpotifyService;
import static com.example.music_mobile_app.MainActivity.spotifyService;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.ui.AccountFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;
//import com.spotify.android.appremote.api.AppRemote;


//import com.spotify.protocol.types.PlayerState;


public class PlayTrackActivity extends FragmentActivity {
    private boolean isFollowing = false;
    private static final String FOLLOW_PREF_KEY = "follow_pref_key";
    private static final String CONNECTION_PARAMS_KEY = "connectionParamsKey";

    private static final String TAG = PlayTrackActivity.class.getSimpleName();

    ImageView btn_back, btn_prev, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options, btn_add_to_playlist;
    TextView tv_track_name, tv_track_arist, tv_track_album;
    ConstraintLayout play_back_layout;
    ShapeableImageView track_img;
    AppCompatSeekBar mSeekBar;

    TrackProgressBar mTrackProgressBar;

    private Track detailTrack;
    String authToken;
    Button btnFollow;

    SpotifyAppRemote mSpotifyAppRemote;
    public static String artistId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);

        // Prepared resources
        prepareData();
        detailTrack = getIntent().getParcelableExtra("Track");
        setData(detailTrack);
        SharedPreferences sharedPreferences = getSharedPreferences("FollowState", MODE_PRIVATE);
        // Đặt trạng thái ban đầu cho nút theo dõi từ SharedPreferences
        isFollowing = sharedPreferences.getBoolean(FOLLOW_PREF_KEY, false);
//        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", "");



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

        btnFollow = findViewById(R.id.buttonFollow);
        btnFollow.setTag("follow"); // Đặt tag ban đầu cho nút button
        // Thiết lập người nghe cho sự kiện bấm nút
        if (btnFollow != null) {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy tag hiện tại của nút
                    String tag = (String) v.getTag();

                    if (!isFollowing) {
                        // Nếu chưa theo dõi, thực hiện hành động theo dõi
                        followArtist(detailTrack.artists.get(0).id); // Chỉ lấy id của nghệ sĩ đầu tiên
                        // Set state after following
                        setFollowButtonState(btnFollow, true);
                    } else {
                        // Nếu đang theo dõi, thực hiện hành động bỏ theo dõi
                        unfollowArtists(detailTrack.artists.get(0).id);
                        // Set state after unfollowing
                        setFollowButtonState(btnFollow, false);
                    }
                }
            });
        } else {
            // Nếu btnFollow là null, hiển thị thông báo lỗi
            Log.e(TAG, "Button btnFollow is null");
        }

        setFollowButtonState(btnFollow, isFollowing);
    }
    private void initializeFollowButtonState() {
        // Kiểm tra trạng thái ban đầu từ SharedPreferences và thiết lập nút theo dõi
        SharedPreferences sharedPreferences = getSharedPreferences("FollowState", MODE_PRIVATE);
        isFollowing = sharedPreferences.getBoolean(FOLLOW_PREF_KEY, false);
        setFollowButtonState(btnFollow, isFollowing);
    }

    private void setFollowButtonState(Button button, boolean isFollowing) {
        // Code của bạn ở setFollowButtonState...
        // Sau khi đặt trạng thái của nút, lưu trạng thái mới vào SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("FollowState", MODE_PRIVATE).edit();
        editor.putBoolean(FOLLOW_PREF_KEY, isFollowing);
        // Thay đổi văn bản của nút tùy thuộc vào trạng thái theo dõi
        if (isFollowing) {
            button.setText("Bỏ theo dõi");
        } else {
            button.setText("Theo dõi");
        }
        editor.apply();
    }

    private void unfollowArtists(String artistId) {
        Log.e(TAG, "artistId : " + artistId);
        spotifyService.unfollowArtists(artistId, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {
                Log.d(TAG, "UnFollow artist success");
                Toast.makeText(PlayTrackActivity.this, "Đã bỏ theo dõi nghệ sĩ!", Toast.LENGTH_SHORT).show();
                isFollowing = false;
                setFollowButtonState(btnFollow, isFollowing); // Cập nhật trạng thái và văn bản của nút
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error following artist on Spotify: " + error.getMessage());
                Toast.makeText(PlayTrackActivity.this, "Đã xảy ra lỗi khi bỏ theo dõi nghệ sĩ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void followArtist(String artistId) {
        spotifyService.followArtists(artistId, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {
                Log.d(TAG, "Follow artist success");
                Toast.makeText(PlayTrackActivity.this, "Đã bắt đầu theo dõi nghệ sĩ!", Toast.LENGTH_SHORT).show();
                isFollowing = true;
                setFollowButtonState(btnFollow, isFollowing); // Cập nhật trạng thái và văn bản của nút
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error following artist on Spotify: " + error.getMessage());
                Toast.makeText(PlayTrackActivity.this, "Đã xảy ra lỗi khi theo dõi nghệ sĩ!", Toast.LENGTH_SHORT).show();
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

        List<ArtistSimple> artists = detailTrack.artists; // Danh sách các nghệ sĩ
        StringBuilder artistNames = new StringBuilder();
        for (ArtistSimple artist : artists) {
            artistNames.append(artist.name).append(", "); // Lấy tên của mỗi nghệ sĩ và thêm vào chuỗi
        }
        // Loại bỏ dấu phẩy cuối cùng và gán vào TextView
        TextView artistNameTextView = findViewById(R.id.artist_item_name);
        artistNameTextView.setText(artistNames.toString().substring(0, artistNames.length() - 2));

        if (!artists.isEmpty()) {
            ArtistSimple firstArtist = artists.get(0);
            // Chuyển đổi ArtistSimple sang Artist
            String artistId = firstArtist.id;
            spotifyService.getArtist(artistId, new Callback<Artist>() {

                @Override
                public void success(Artist artist, retrofit.client.Response response) {
                    if (!artist.images.isEmpty()) {
                        Image artistImage = artist.images.get(0);
                        ImageView artistImageView = findViewById(R.id.artist_item_image);
                        Glide.with(PlayTrackActivity.this).load(artistImage.url).into(artistImageView);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    // Xử lý lỗi khi không thể lấy thông tin về nghệ sĩ
                }
            });
        }
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