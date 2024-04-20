package com.example.music_mobile_app;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.manager.PlaybackManager;
import com.example.music_mobile_app.ui.PlaylistFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Repeat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class PlayTrackActivity extends FragmentActivity {

    private static final String TAG = PlayTrackActivity.class.getSimpleName();
    private ImageView btn_back, btn_prev, btn_next, btn_play, btn_replay, btn_shuffle, btn_add_to_playlist, btn_add_to_favorite, btn_queue;
    private TextView tv_track_name, tv_track_arist, tv_track_album, tv_currentTime, tv_endTime, tv_playerField;
    private Button btnFollow;
    private ConstraintLayout play_back_layout;
    private ShapeableImageView track_img;
    private AppCompatSeekBar mSeekBar;
    private TrackProgressBar mTrackProgressBar;
    private Track detailTrack;
    private Album detailAlbum;
    private Artist detailArtist;
    private PlaybackManager playbackManager;
    private String playing_URI;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private static boolean isFollowing = false;
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");

    private List<Track> queuePlayTrack = new ArrayList<>();

    private int currentSongIndex = 0;

    private FragmentManager manager;

    @Override
    protected void onStart() {
        manager = getSupportFragmentManager();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        // Prepared resources
        prepareData();
        Intent intent = getIntent();
        String action = intent.getAction();
        // Get track detail
        switch (action) {
            case "Play Track":
                detailTrack = intent.getParcelableExtra("Track");
                Toast.makeText(this, "Playing Track", Toast.LENGTH_SHORT).show();
                break;
            case "Play Album":
                queuePlayTrack.clear();
                List<Track> albumTracks = intent.getParcelableArrayListExtra("ListTrack");
                queuePlayTrack.addAll(albumTracks);
                detailTrack = queuePlayTrack.get(currentSongIndex);
                Toast.makeText(this, "Playing Album", Toast.LENGTH_SHORT).show();
                break;
            case "Play Artist":
                detailArtist = intent.getParcelableExtra("Artist");
                queuePlayTrack.clear();
                List<Track> artistTracks = intent.getParcelableArrayListExtra("ListTrack");
                queuePlayTrack.addAll(artistTracks);
                detailTrack = queuePlayTrack.get(currentSongIndex);
                Toast.makeText(this, "Playing Artist", Toast.LENGTH_SHORT).show();
                break;
            case "Play Favorite":
                break;
            default:
                Log.e(TAG, "No action found!!!");
                break;
        }

        detailAlbum = getIntent().getParcelableExtra("Album");
        setData(detailTrack);

        initPlayer(true);


        // Player
        btn_next.setOnClickListener(v -> {
            playNextSong(true);
        });
        btn_prev.setOnClickListener(v -> {
            playNextSong(false);
        });

        if (!queuePlayTrack.isEmpty()) {
//            btn_queue.setOnClickListener(v -> {
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("ListTrack", new ArrayList<>(queuePlayTrack));
//                QueueFragment queueFragment = new QueueFragment();
//                queueFragment.setArguments(bundle);
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.addToBackStack(null);
//                transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out_left, R.anim.slide_out_left, R.anim.slide_in);
//                manager.beginTransaction().replace(R.id.play_back_layout, new QueueFragment()).commit();
//            });
        } else {
            btn_queue.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }

        // Onclick Back
        btn_back.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            play_back_layout.startAnimation(animation);
            onBackPressed();
            overridePendingTransition(0, 0);
        });

        btn_add_to_playlist.setOnClickListener(v -> {
            Log.d(TAG, "Track ID: " + detailTrack.id);
            MethodsManager.getInstance().showAddToPlaylist(this, detailTrack);
        });

        MethodsManager.getInstance().checkContains(detailTrack.id, new ListenerManager.OnGetCompleteListener() {
            @Override
            public void onComplete(boolean type) {
                // Not contain
                if (type == true) {
                    btn_add_to_favorite.setOnClickListener(v -> {
                        btn_add_to_favorite.setImageResource(R.drawable.ic_like_green);
                        MethodsManager.getInstance().addToFavorite(detailTrack.id, new ListenerManager.OnGetCompleteListener() {
                            @Override
                            public void onComplete(boolean type) {
                                // Reload favorite fragment
                                MethodsManager.getInstance().getUserFavorite(true);
                                btn_add_to_favorite.setTag("Add");
                            }

                            @Override
                            public void onError(Throwable error) {
                                Log.e(TAG, "Adding false" + error);
                            }
                        });

                    });
                } else {
                    btn_add_to_favorite.setImageResource(R.drawable.ic_like_green);
                    btn_add_to_favorite.setOnClickListener(v -> {
                        btn_add_to_favorite.setImageResource(R.drawable.ic_like_black_24dp);
                        MethodsManager.getInstance().removeFromFavorite(detailTrack.id, new ListenerManager.OnGetCompleteListener() {
                            @Override
                            public void onComplete(boolean type) {
                                Log.d(TAG, "Remove complete ");
                            }

                            @Override
                            public void onError(Throwable error) {
                                Log.e(TAG, "onError: " + error);
                            }
                        });
                        // Reload favorite fragment
                        MethodsManager.getInstance().getUserFavorite(true);
                        btn_add_to_favorite.setTag("Remove");
                    });
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });

        if (btnFollow != null) {
            // Initial primal follow status
            initializeFollowButtonState(detailTrack.artists.get(0).id);

            btnFollow.setOnClickListener(v -> {
                if (isFollowing == false) {
                    // Nếu chưa theo dõi, thực hiện hành động theo dõi
                    followArtist(detailTrack.artists.get(0).id); // Chỉ lấy id của nghệ sĩ đầu tiên
                    setFollowButtonState(btnFollow, true);
                } else {
                    // Nếu đang theo dõi, thực hiện hành động bỏ theo dõi
                    unfollowArtists(detailTrack.artists.get(0).id); // Chỉ lấy id của nghệ sĩ đầu tiên
                    // Cập nhật trạng thái và văn bản của nút
                    setFollowButtonState(btnFollow, false);
                }
            });
            // Reload follow artist
            ListManager.getInstance().setFollowArtists(null);
        } else {
            // Nếu btnFollow là null, hiển thị thông báo lỗi
            Log.e(TAG, "Button btnFollow is null");
        }


    }


    @Override
    protected void onStop() {
        super.onStop();
        playbackManager.Disconnect();
    }

    //     Follow artist
    private void initializeFollowButtonState(String artistId) {
        // Check status of following artists
        spotifyService.isFollowingArtists(artistId, new SpotifyCallback<boolean[]>() {
            @Override
            public void failure(SpotifyError spotifyError) {
            }

            @Override
            public void success(boolean[] booleans, Response response) {
                // Followed
                if (booleans[0] == true) {
                    setFollowButtonState(btnFollow, true);
                } else {
                    // Not Followed
                    setFollowButtonState(btnFollow, false);
                }
            }
        });
    }

    private void setFollowButtonState(Button button, boolean status) {
        // Thay đổi văn bản của nút tùy thuộc vào trạng thái theo dõi
        isFollowing = status;
        if (isFollowing) {
            button.setText("Bỏ theo dõi");
        } else {
            button.setText("Theo dõi");
        }
    }

    private void unfollowArtists(String artistId) {
        Log.e(TAG, "artistId : " + artistId);
        spotifyService.unfollowArtists(artistId, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {
                Log.d(TAG, "UnFollow artist success");
                Toast.makeText(PlayTrackActivity.this, "Đã bỏ theo dõi nghệ sĩ!", Toast.LENGTH_SHORT).show();
                // Cập nhật trạng thái và văn bản của nút
                setFollowButtonState(btnFollow, false);
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
                // Cập nhật trạng thái và văn bản của nút
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error following artist on Spotify: " + error.getMessage());
                Toast.makeText(PlayTrackActivity.this, "Đã xảy ra lỗi khi theo dõi nghệ sĩ!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("ResourceType") // Ignore all illegal resources
    private void prepareData() {

        btn_back = findViewById(R.id.btn_back);
        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);
        btn_play = findViewById(R.id.btn_play);
        btn_shuffle = findViewById(R.id.btn_shuffle);
        btn_replay = findViewById(R.id.btn_replay);
        btn_queue = findViewById(R.id.track_queue);
        btn_add_to_playlist = findViewById(R.id.btn_add_to_playlist);
        btn_add_to_favorite = findViewById(R.id.btn_add_to_fav);
        btnFollow = findViewById(R.id.buttonFollow);

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

    }

    private void setData(Track detailTrack) {

        tv_track_name.setText(detailTrack.name);
        tv_track_album.setText((detailTrack.album.name != null) ? detailTrack.album.name : detailAlbum.name);
        tv_track_arist.setText(detailTrack.artists.get(0).name);

        // Get Track's Album
        Album album = getIntent().getParcelableExtra("Track's Album");
        Glide.with(this).load((detailTrack.album.images.get(0).url == null) ? album.images.get(0).url : detailTrack.album.images.get(0).url).override(Target.SIZE_ORIGINAL).into(track_img);

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

    private Subscription.EventCallback<PlayerState> mPlayerStateCallback = new Subscription.EventCallback<PlayerState>() {
        @Override
        public void onEvent(PlayerState playerState) {
            if (!playerState.track.name.equalsIgnoreCase(detailTrack.name) || playerState.track == null) {
                playbackManager.play(playing_URI, new ListenerManager.OnGetCompleteListener() {
                    @Override
                    public void onComplete(boolean type) {
                        playbackManager.addToQueue(playing_URI);
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });

            }
            mSeekBar.setMax((int) playerState.track.duration);
            tv_endTime.setText(formatter.format(playerState.track.duration));


            btn_play.setOnClickListener(v -> {
                playbackManager.onPlayPauseButtonClicked();
            });


            btn_replay.setOnClickListener(v -> {
                btn_replay.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorGreen), PorterDuff.Mode.SRC_IN);
                playbackManager.onRepeatMode();
            });

            btn_shuffle.setOnClickListener(v -> {
                btn_shuffle.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorGreen), PorterDuff.Mode.SRC_IN);
                playbackManager.onToggleShuffleButtonClicked();
            });


            // Player
            if (playerState.isPaused) {
                btn_play.setImageResource(R.drawable.ic_play_white_48dp);
                mTrackProgressBar.pause();
                mediaPlayer.pause();
            } else {
                btn_play.setImageResource(R.drawable.ic_pause_white_24dp);
                mediaPlayer.start();
                mTrackProgressBar.unpause();
            }

            // Repeat mode
            if (playerState.playbackOptions.repeatMode == Repeat.ALL) {
                DrawableCompat.setTint(btn_replay.getDrawable(), getResources().getColor(R.color.colorGreen, getTheme()));

            } else if (playerState.playbackOptions.repeatMode == Repeat.ONE) {
//                btn_replay.setImageResource(R.drawable.mediaservice_repeat_one);
                DrawableCompat.setTint(btn_replay.getDrawable(), getResources().getColor(R.color.colorGreen, getTheme()));
            } else {
                DrawableCompat.setTint(btn_replay.getDrawable(), Color.WHITE);
            }

            // Shuffle mode
            if (playerState.playbackOptions.isShuffling) {
                // Get random index
                currentSongIndex = new Random().nextInt(queuePlayTrack.size() - 1);
            }

            // Invalidate seekbar length and position
            mSeekBar.setMax((int) playerState.track.duration);
            mTrackProgressBar.setDuration(playerState.track.duration);
            mTrackProgressBar.update(playerState.playbackPosition);
        }

    };

    // TrackProcessBar Class
    private class TrackProgressBar {
        private static final int LOOP_DURATION = 100;
        private final AppCompatSeekBar mSeekBar;

        // Lớp Handler để thực hiện tương tác hoặc cập nhật UI từ các luồng khác nhau
        private final Handler mHandler;

        private Runnable mSeekRunnable = new Runnable() {
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
                mTrackProgressBar.update(progress);
                tv_currentTime.setText(formatter.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
                mTrackProgressBar.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playbackManager.getPlayerApi().seekTo(seekBar.getProgress()).setErrorCallback(throwable -> {
                    // // Xử lý lỗi khi seekTo() gặp vấn đề
                    Toast.makeText(PlayTrackActivity.this, "Error while seeking: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    // // Ví dụ: log lỗi
                    Log.e("SeekBarChangeListener", "Error while seeking", throwable);
                });
                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();
                mTrackProgressBar.unpause();
            }
        };
    }

    public void initPlayer(boolean reload) {
        // Setting player UI
        playbackManager = PlaybackManager.getInstance(this, mPlayerStateCallback, reload);
        mediaPlayer.start();
        mTrackProgressBar.unpause();
    }

    private void checkEnablePrevBtn() {
        // Truy cập đến vector drawable từ resources
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_prev_white_36dp);
        if (currentSongIndex == 0 || currentSongIndex == -1) {
            btn_prev.setEnabled(false);// vô hiệu hóa button prev
            // Đặt màu cho vector drawable thành colorNavIcon
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorGrayLight), PorterDuff.Mode.SRC_IN);
        } else {
            // bỏ vô hiệu hóa button prev
            btn_prev.setEnabled(true); // bỏ vô hiệu hóa button prev
            // Đặt màu cho vector drawable thành #FFFFFF
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }

        // Gán vector drawable đã chỉnh sửa cho ImageView của button prev
        btn_prev.setImageDrawable(drawable);
    }

    private void playNextSong(boolean next) {
        if (next) {
            currentSongIndex++;
        } else {
            currentSongIndex--;
        }
        if (currentSongIndex > queuePlayTrack.size() || currentSongIndex < 0) {
            // Phát bài hát tiếp theo trong danh sách
            // Nếu không còn bài hát nào để phát trong danh sách
            // Bạn có thể thực hiện một hành động nào đó, ví dụ: dừng hoặc quay lại bài hát đầu tiên
            currentSongIndex = 0; // Quay lại bài hát đầu tiên
            // trở về bài đầu tiên
            // Phát lại bài hát đầu tiên
        }
        detailTrack = queuePlayTrack.get(currentSongIndex);
        setData(detailTrack);
        playing_URI = detailTrack.uri;
        initPlayer(true);
        // check vô hiệu hóa btn prev
        checkEnablePrevBtn();
    }


}