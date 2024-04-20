package com.example.music_mobile_app;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.network.mSpotifyAPI;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;


public class ExtensionPlayerActivity extends FragmentActivity {

    private static final String TAG = ExtensionPlayerActivity.class.getSimpleName();

    private ImageButton btn_prev;
    private ImageView btn_back, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options, btn_add_to_playlist;
    private TextView tv_track_name, tv_track_arist, tv_currentTime, tv_durationTime;
    private RecyclerView recyclerView;
    private Button buttonAddPlaylist, buttonAddSong, btnFollow;
    private ConstraintLayout play_back_layout;
    private ShapeableImageView track_img;
    private AppCompatSeekBar mSeekBar;
    private mSpotifyAPI mSpotifyAPI = MainActivity.mSpotifyAPI;
    private String selectedPlaylistId;
    private MediaPlayer mediaPlayer;
    private TrackProgressBar mTrackProgressBar;
    private boolean isPlaying = false;
    // Khởi tạo một biến boolean để theo dõi trạng thái của nguồn dữ liệu của MediaPlayer
    private boolean isMediaPlayerInitialized = false;

    List<Song> songList;

    private int currentSongIndex = -1; // Biến này để theo dõi vị trí của bài hát hiện tại đang được phát


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);


        // Prepared resources
        prepareData();

        // Onclick Back
        btn_back.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
            play_back_layout.startAnimation(animation);
            onBackPressed();
            overridePendingTransition(0, 0);
        });

        // Bắt sự kiện click cho nút play để phát hoặc tạm dừng bài hát hiện tại
        btn_play.setOnClickListener(v -> {
            if (isPlaying) {
                pauseAudio();
            } else {
                mediaPlayer.start(); // Khởi động hoặc tiếp tục phát bài hát nếu đã tạm dừng
                isPlaying = true;
                btn_play.setImageResource(R.drawable.ic_pause_white_24dp);
                mTrackProgressBar.unpause();
            }
        });

        // Tạo một đối tượng MediaPlayer mới cho bài hát
        generalMediaPlayer();
        // Chơi bài hát đầu tiên khi mở Activity
        if (currentSongIndex == -1) {
            // check vô hiệu hóa btn prev
            checkEnablePrevBtn();

            currentSongIndex = 0;
            Song firstSong = songList.get(currentSongIndex);
            setData(firstSong);
//            generalMediaPlayer();
            playAudio(firstSong.urlSong);
        }


        // Bắt sự kiện click cho nút phát bài kế tiếp
        btn_next.setOnClickListener(v -> {
            playNextSong(true);
        });
        // Bắt sự kiện click cho nút phát bài trước đó
        btn_prev.setOnClickListener(v -> {
            playNextSong(false);
        });
    }


    @SuppressLint("ResourceType") // Ignore all illegal resources
    private void prepareData() {
        songList = new ArrayList<>();

        // Thêm các đối tượng Song vào danh sách
        songList.add(new Song("Từng là", "Vũ Cát Tường", "https://i.scdn.co/image/ab67616d0000b27373fd9df745f312dbbe04bf49", "https://i.scdn.co/image/ab6761610000e5ebc9b5ba5524e01567b9f4f481", "https://archive.org/download/song_20240413/TungLa-VuCatTuong-13962415.mp3"));
        songList.add(new Song("Chúng ta của hiện tại", "Sơn Tùng MTP", "https://i.scdn.co/image/ab67616d0000b2735888c34015bebbf123957f6d", "https://i.scdn.co/image/ab6761610000e5eb7afc6ecdb9102abd1e10d338", "https://archive.org/download/song_20240413/ChungTaCuaHienTai-SonTungMTP-6892340.mp3"));
        songList.add(new Song("Người bình thường", "Vũ Cát Tường", "https://i.scdn.co/image/ab67616d0000b2734b261902f2f94e876adf9181", "https://i.scdn.co/image/ab6761610000e5ebc9b5ba5524e01567b9f4f481", "https://archive.org/download/song_20240413/NguoiBinhThuong-VuCatTuong-12694464.mp3"));


        btn_back = findViewById(R.id.btn_back);
        btn_prev = findViewById(R.id.btn_prev);
        btn_next = findViewById(R.id.btn_next);
        btn_play = findViewById(R.id.btn_play);
        btn_shuffle = findViewById(R.id.btn_shuffle);
        btn_replay = findViewById(R.id.btn_replay);
        btn_track_options = findViewById(R.id.track_queue);
        btn_add_to_playlist = findViewById(R.id.btn_add_to_playlist);
        btnFollow = findViewById(R.id.buttonFollow);

        tv_track_name = findViewById(R.id.track_name);
        tv_track_arist = findViewById(R.id.track_artist);
        tv_currentTime = findViewById(R.id.tv_currentTime);
        tv_durationTime = findViewById(R.id.tv_durationTime);
        mSeekBar = findViewById(R.id.seek_bar);

        play_back_layout = findViewById(R.id.play_back_layout);
        track_img = findViewById(R.id.track_img);

        // Create TrackProgressBar object
        mTrackProgressBar = new TrackProgressBar(mSeekBar);
    }

    private void setData(Song song) {

        tv_track_name.setText(song.name);
        tv_track_arist.setText(song.artist);

        Glide.with(this).load(song.image).override(Target.SIZE_ORIGINAL).into(track_img);

        TextView artistNameTextView = findViewById(R.id.artist_item_name);
        artistNameTextView.setText(song.artist);

        ImageView artistImageView = findViewById(R.id.artist_item_image);
        Glide.with(ExtensionPlayerActivity.this).load(song.imageArtist).into(artistImageView);


    }

    private void playAudio(String url) {
        try {
            // Stop and reset MediaPlayer if it's already playing
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            } else {
                mediaPlayer.reset();
            }

            // Set data source
            mediaPlayer.setDataSource(url);

            // Set a prepared listener
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Update UI and start playback
//                    mSeekBar.setProgress(0);
//                    tv_currentTime.setText(milliSecondsToTimer(0));
                    mp.start();
                    isPlaying = true;
                    btn_play.setImageResource(R.drawable.ic_pause_white_24dp);
                    mTrackProgressBar.setDuration(mp.getDuration());
                    tv_durationTime.setText(milliSecondsToTimer(mp.getDuration()));
                    mTrackProgressBar.unpause();

                    // Set completion listener
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp1) {
                            System.out.println("FINISH");
//                            isPlaying = false;
//                            btn_play.setImageResource(R.drawable.ic_play_white_48dp);
                            mTrackProgressBar.pause();
                            mSeekBar.setProgress(0);
                            tv_currentTime.setText(milliSecondsToTimer(0));

                            // Khi bài hát kết thúc, chuyển sang bài hát tiếp theo
                            playNextSong(true);
                        }
                    });
                }
            });

            // Prepare the MediaPlayer asynchronously
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generalMediaPlayer() {
        mediaPlayer = new MediaPlayer();

        // Set audio attributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btn_play.setImageResource(R.drawable.ic_play_white_48dp);
            mTrackProgressBar.pause();
        } else {
            mediaPlayer.start();
            isPlaying = true;
            btn_play.setImageResource(R.drawable.ic_pause_white_24dp);
            mTrackProgressBar.unpause();
        }
    }

    // Phương thức chuyển đổi milliseconds thành timer định dạng (mm:ss)
    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Tạm dừng cập nhật tiến độ của SeekBar
        mTrackProgressBar.pause();
    }


    private void playNextSong(boolean next) {
        if (next) {
            currentSongIndex++;
        } else {
            currentSongIndex--;
        }
        mTrackProgressBar.pause();
        mSeekBar.setProgress(0);
        tv_currentTime.setText(milliSecondsToTimer(0));


        if (currentSongIndex < songList.size()) {

            Song nextSong = songList.get(currentSongIndex);
            setData(nextSong);
            playAudio(nextSong.urlSong); // Phát bài hát tiếp theo trong danh sách
        } else {
            // Nếu không còn bài hát nào để phát trong danh sách
            // Bạn có thể thực hiện một hành động nào đó, ví dụ: dừng hoặc quay lại bài hát đầu tiên
            currentSongIndex = 0; // Quay lại bài hát đầu tiên

            // trở về bài đầu tiên

            Song firstSong = songList.get(currentSongIndex);
            setData(firstSong);
            playAudio(firstSong.urlSong); // Phát lại bài hát đầu tiên
        }

        // check vô hiệu hóa btn prev
        checkEnablePrevBtn();
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


//        // Kiểm tra nếu button prev được vô hiệu hóa
//        if (!btn_prev.isEnabled()) {
//
//        } else {
//
//        }

        // Gán vector drawable đã chỉnh sửa cho ImageView của button prev
        btn_prev.setImageDrawable(drawable);
    }

    // TrackProcessBar Class
    private class TrackProgressBar {
        private static final int LOOP_DURATION = 100;
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
                mTrackProgressBar.update(progress); // Cập nhật tiến độ mới của SeekBar
                tv_currentTime.setText(milliSecondsToTimer(progress)); // Cập nhật hiển thị thời gian hiện tại
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Pause MediaPlayer and stop updating SeekBar progress while tracking
                mediaPlayer.pause();
                mTrackProgressBar.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Resume MediaPlayer and set its position to the new SeekBar progress
                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();
                mTrackProgressBar.unpause();
            }
        };
    }
}