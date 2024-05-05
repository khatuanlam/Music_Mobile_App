package com.example.music_mobile_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.adapter.mydatabase.OnPlaylistClickListener;
import com.example.music_mobile_app.adapter.mydatabase.OpenPlaylistAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.model.sqlite.LiteSong;
import com.example.music_mobile_app.network.mSpotifyAPI;
import com.example.music_mobile_app.viewmodel.mydatabase.SongViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.album.SongsOfAlbumViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.sqlite.DownloadSongListViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import kaaes.spotify.webapi.android.SpotifyService;


public class ExtensionPlayerActivity extends FragmentActivity implements OnPlaylistClickListener {

    private DownloadSongListViewModel downloadSongListViewModel;
    private AllPlaylistViewModel allPlaylistViewModel;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;
    private SongsOfAlbumViewModel songsOfAlbumViewModel;
    private FavoriteSongsViewModel favoriteSongsViewModel;
    private SongViewModel songViewModel;
    private static final String TAG = ExtensionPlayerActivity.class.getSimpleName();

    private ImageButton btn_prev;
    private ImageView btn_back, btn_next, btn_play, btn_replay, btn_shuffle, btn_track_options, btn_add_to_playlist, btn_add_to_fav;
    private TextView tv_track_name, tv_track_arist, tv_currentTime, tv_durationTime, tv_trackAlbum, tv_lyric;
    private RecyclerView recyclerView;
    private Button buttonAddPlaylist, buttonAddSong, btnFollow;
    private ConstraintLayout play_back_layout;
    private ShapeableImageView track_img;
    private AppCompatSeekBar mSeekBar;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private mSpotifyAPI mSpotifyAPI = MainActivity.mSpotifyAPI;
    private String selectedPlaylistId;
    private MediaPlayer mediaPlayer;
    private TrackProgressBar mTrackProgressBar;
    private boolean isPlaying = false;
    // Khởi tạo một biến boolean để theo dõi trạng thái của nguồn dữ liệu của MediaPlayer
    private boolean isMediaPlayerInitialized = false;

    public long userIdMydb;
    List<Song> songList;


    public Song playSong = new Song();

    public Song playSongDownload = new Song();
    public List<Song> songAlbum = new ArrayList<>();
    public List<Song> songPlaylist = new ArrayList<>();
    public List<Song> songDownload = new ArrayList<>();
    public List<Song> songFavorite = new ArrayList<>();

    private int currentSongIndex = -1; // Biến này để theo dõi vị trí của bài hát hiện tại đang được phát

    private Boolean currentFavorite, isShuffle = false;

    private AlertDialog alertDialog;
    private RecyclerView playlistRecyclerView;

    private OpenPlaylistAdapter openPlaylistAdapter;

    private Playlist selectedPlaylist;

    private Song selectedSong;

    private String actionIntent;

    @Override
    public void onPlaylistClick(Playlist playlist) {

        selectedPlaylist = playlist;
        alertDialog.dismiss();
        selectedSong = songList.get(currentSongIndex);
        songsOfPlaylistViewModel.postSongToPlaylist(selectedSong.getId(), playlist.getId());
    }

    public void setOpenPlaylists(List<Playlist> playlists) {
        openPlaylistAdapter.setmDataList(playlists);
    }
    public void showPlaylistListDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        layoutParams.width = width;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(layoutParams);
        alertDialog.setCanceledOnTouchOutside(true);

        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track_mydb);
        prepareData();

        downloadSongListViewModel = new ViewModelProvider(this).get(DownloadSongListViewModel.class);
        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);
        songsOfAlbumViewModel = new ViewModelProvider(this).get(SongsOfAlbumViewModel.class);
        songsOfPlaylistViewModel = new ViewModelProvider(this).get(SongsOfPlaylistViewModel.class);
        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);
        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.mydb_dialog_open_playlist, null);
        playlistRecyclerView = dialogView.findViewById(R.id.mydb_dialog_open_playlist_recyclerView);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        openPlaylistAdapter = new OpenPlaylistAdapter(this, null, null, new ArrayList<>());
        openPlaylistAdapter.setOnPlaylistClickListener(this);
        playlistRecyclerView.setAdapter(openPlaylistAdapter);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedPlaylist != null) {
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn playlist trước khi thêm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(dialogView);
        alertDialog = builder.create();

        Intent intent = getIntent();
        String action = intent.getAction();
        actionIntent = action;
        userIdMydb = intent.getLongExtra("userIdMyDb", 1);
        if (action.equals("Play Song")) {
            tv_trackAlbum.setText("Phát nhạc");

            long songId = intent.getLongExtra("songId", 1);
            songViewModel.loadSong(Math.toIntExact(songId));

        } else if (action.equals("Play Playlist")) {
            tv_trackAlbum.setText("Phát Playlist");
            long playlistId = intent.getLongExtra("playlistId", 1);
            songsOfPlaylistViewModel.getAllSongsByPlaylistID(playlistId);
        } else if (action.equals("Play Album")) {
            tv_trackAlbum.setText("Phát Album");
            long albumId = intent.getLongExtra("albumId", 1);
            songsOfAlbumViewModel.getAllSongsByAlbum(albumId);
        }
        else if (action.equals("Play Favorite")) {
            tv_trackAlbum.setText("Yêu thích");
            favoriteSongsViewModel.getAllFavoriteSongsByUserId(userIdMydb);
        }
        else if (action.equals("Play Download")) {
            tv_trackAlbum.setText("Tải xuống");
            downloadSongListViewModel.loadSong();
        }
        else if (action.equals("Play Download Single")) {
            tv_trackAlbum.setText("Tải xuống");
            String path = intent.getStringExtra("DLSongPath");
            downloadSongListViewModel.loadSongById(path);
        }

        btn_replay.setImageResource(R.drawable.ic_restart_green_36dp);


        allPlaylistViewModel.getAllPlaylistsByIdUser(userIdMydb);

        allPlaylistViewModel.getPlaylists().observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                setOpenPlaylists(playlists);
            }
        });
        downloadSongListViewModel.getSongs().observe(this, new Observer<List<LiteSong>>() {
            @Override
            public void onChanged(List<LiteSong> liteSongs) {
                if(!action.equals("Play Download"))
                    return;
                List<Song> songs1 = liteSongs.stream().map(s -> {
                    Song song = new Song();
                    song.name = s.getName();
                    song.image = s.getImage();
                    song.id = Long.valueOf(s.getId_mydb());
                    song.urlSong = "file:///storage/emulated/0/Download/" +s.getId_mydb()+".mp3";
                    song.urlLyric = "";
                    return song;
                }).collect(Collectors.toList());
                songDownload = songs1;
                songList.clear();
                songList.addAll(songDownload);
                callPlayAfterLoadData();
            }
        });
        downloadSongListViewModel.getSong().observe(this, new Observer<LiteSong>() {
            @Override
            public void onChanged(LiteSong s) {
                if(!action.equals("Play Download Single"))
                    return;
                Song song = new Song();
                song.name = s.getName();
                song.image = s.getImage();
                song.id = Long.valueOf(s.getId_mydb());
                song.urlSong = "file:///storage/emulated/0/Download/" +s.getId_mydb()+".mp3";
                song.urlLyric = "";
                playSongDownload = song;
                songList.clear();
                songList.add(playSongDownload);
                callPlayAfterLoadData();
            }
        });
        favoriteSongsViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                songFavorite.addAll(songs);
                songList.clear();
                songList.addAll(songFavorite);
                callPlayAfterLoadData();
            }
        });
        songViewModel.getSong().observe(this, new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                playSong = song;
                songList.clear();
                songList.add(playSong);
                callPlayAfterLoadData();
            }
        });

        songViewModel.getIsFavorite().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if (result) {
                    btn_add_to_fav.setImageResource(R.drawable.ic_like_green);

                } else {
                    btn_add_to_fav.setImageResource(R.drawable.ic_like_black_24dp);
                }
                currentFavorite = result;
            }
        });
        songsOfPlaylistViewModel.getIsPostSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPostSuccess) {
                if(isPostSuccess == null)
                    return;
                if (isPostSuccess) {
                    Toast.makeText(getApplicationContext(), "ĐÃ THÊM VÀO PLAYLIST", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        songsOfPlaylistViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                songPlaylist.addAll(songs);
                songList.clear();
                songList.addAll(songPlaylist);
                callPlayAfterLoadData();
            }
        });
        songsOfAlbumViewModel.getSongs().observe(this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                songAlbum.addAll(songs);
                songList.clear();
                songList.addAll(songAlbum);
                callPlayAfterLoadData();
            }
        });
        favoriteSongsViewModel.getIsPostSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess == null)
                    return;
                if (isSuccess) {
                    Toast.makeText(getApplicationContext(), "ĐÃ THÊM VÀO DANH SÁCH YÊU THÍCH", Toast.LENGTH_SHORT).show();
                    btn_add_to_fav.setImageResource(R.drawable.ic_like_green);
                    currentFavorite = true;
                } else {
                    Toast.makeText(getApplicationContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });
        favoriteSongsViewModel.getIsDeleteSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleteSuccess) {
                if (isDeleteSuccess == null)
                    return;
                if (isDeleteSuccess) {
                    Toast.makeText(getApplicationContext(), "ĐÃ XÓA KHỎI DANH SÁCH YÊU THÍCH", Toast.LENGTH_SHORT).show();
                    btn_add_to_fav.setImageResource(R.drawable.ic_like_black_24dp);
                    currentFavorite = false;
                } else {
                    Toast.makeText(getApplicationContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });


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


        generalMediaPlayer();


//


        btn_next.setOnClickListener(v -> {
            playNextSong(true);
        });

        btn_prev.setOnClickListener(v -> {
            playNextSong(false);
        });
    }

    public void callPlayAfterLoadData() {
        if (currentSongIndex == -1) {
            checkEnablePrevBtn();

            currentSongIndex = 0;
            Song firstSong = songList.get(currentSongIndex);
            setData(firstSong);
            songViewModel.checkFavorite(firstSong.getId(), userIdMydb);
//            generalMediaPlayer();
            playAudio(firstSong.urlSong);
        }
    }

    @SuppressLint("ResourceType") // Ignore all illegal resources
    private void prepareData() {
        songList = new ArrayList<>();

        // Thêm các đối tượng Song vào danh sách
//        songList.add(new Song("Từng là", "Vũ Cát Tường", "https://i.scdn.co/image/ab67616d0000b27373fd9df745f312dbbe04bf49", "https://i.scdn.co/image/ab6761610000e5ebc9b5ba5524e01567b9f4f481", "https://ia800307.us.archive.org/15/items/hoa-quen-nang-yul-lee/Hoa%20qu%C3%AAn%20n%E1%BA%AFng%20Yul%20Lee.mp3"));
//        songList.add(new Song("Chúng ta của hiện tại", "Sơn Tùng MTP", "https://i.scdn.co/image/ab67616d0000b2735888c34015bebbf123957f6d", "https://i.scdn.co/image/ab6761610000e5eb7afc6ecdb9102abd1e10d338", "https://ia800307.us.archive.org/15/items/hoa-quen-nang-yul-lee/Hoa%20qu%C3%AAn%20n%E1%BA%AFng%20Yul%20Lee.mp3"));
//        songList.add(new Song("Người bình thường", "Vũ Cát Tường", "https://i.scdn.co/image/ab67616d0000b2734b261902f2f94e876adf9181", "https://i.scdn.co/image/ab6761610000e5ebc9b5ba5524e01567b9f4f481", "https://ia800307.us.archive.org/15/items/hoa-quen-nang-yul-lee/Hoa%20qu%C3%AAn%20n%E1%BA%AFng%20Yul%20Lee.mp3"));


        btn_back = findViewById(R.id.mydb_btn_back);
        btn_prev = findViewById(R.id.mydb_btn_prev);
        btn_next = findViewById(R.id.mydb_btn_next);
        btn_play = findViewById(R.id.mydb_btn_play);
        btn_shuffle = findViewById(R.id.mydb_btn_shuffle);
        btn_replay = findViewById(R.id.mydb_btn_replay);
        btn_track_options = findViewById(R.id.mydb_track_options);
        btn_add_to_playlist = findViewById(R.id.mydb_btn_add_to_playlist);
//        btnFollow = findViewById(R.id.mydb_buttonFollow);
        btn_add_to_fav = findViewById(R.id.mydb_btn_add_to_fav);

        tv_track_name = findViewById(R.id.mydb_track_name);
        tv_trackAlbum = findViewById(R.id.mydb_track_album);
        tv_track_arist = findViewById(R.id.mydb_track_artist);
        tv_currentTime = findViewById(R.id.mydb_tv_currentTime);
        tv_durationTime = findViewById(R.id.mydb_tv_durationTime);
        tv_lyric = findViewById(R.id.mydb_play_activity_lyric);


        mSeekBar = findViewById(R.id.mydb_seek_bar);

        play_back_layout = findViewById(R.id.mydb_play_back_layout);
        track_img = findViewById(R.id.mydb_track_img);

        // Create TrackProgressBar object
        mTrackProgressBar = new TrackProgressBar(mSeekBar);

        btn_add_to_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaylistListDialog();
            }
        });

        btn_add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentFavorite) {
                    favoriteSongsViewModel.postFavoriteSongToUser(songList.get(currentSongIndex).getId(), userIdMydb);
                } else {
                    favoriteSongsViewModel.deleteFavoriteSongByIdUser(songList.get(currentSongIndex).getId(), userIdMydb);
                }
            }
        });
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShuffle) {
                    btn_shuffle.setImageResource(R.drawable.ic_random_green_36dp);
                    btn_replay.setImageResource(R.drawable.ic_restart_white_36dp);
                    btn_prev.setEnabled(false);
                    btn_next.setEnabled(false);
                    btn_prev.setImageResource(R.drawable.ic_prev_gray_36dp);
                    btn_next.setImageResource(R.drawable.ic_next_gray_36dp);
                    isShuffle = true;

                } else {
                    btn_shuffle.setImageResource(R.drawable.ic_random_white_36dp);
                    btn_replay.setImageResource(R.drawable.ic_restart_green_36dp);
                    btn_prev.setEnabled(true);
                    btn_next.setEnabled(true);
                    btn_prev.setImageResource(R.drawable.ic_prev_white_36dp);
                    btn_next.setImageResource(R.drawable.ic_next_white_36dp);
                    isShuffle = false;
                    checkEnablePrevBtn();
                }
            }
        });
    }

    private void setData(Song song) {

        tv_track_name.setText(song.name);
        tv_track_arist.setText(song.artist);
        if(!song.getUrlLyric().isEmpty())
            new GetLyricTask(tv_lyric).execute(song.getUrlLyric());
        Glide.with(this).load(song.image).override(Target.SIZE_ORIGINAL).into(track_img);

//        TextView artistNameTextView = findViewById(R.id.mydb_artist_item_name);
//        artistNameTextView.setText(song.artist);

//        ImageView artistImageView = findViewById(R.id.mydb_artist_item_image);
//        Glide.with(ExtensionPlayerActivity.this).load(song.imageArtist).into(artistImageView);


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


                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp1) {
                            System.out.println("FINISH");
//                            isPlaying = false;
//                            btn_play.setImageResource(R.drawable.ic_play_white_48dp);
                            mTrackProgressBar.pause();
                            mSeekBar.setProgress(0);
                            tv_currentTime.setText(milliSecondsToTimer(0));

                            if (!isShuffle) {
                                playNextSong(true);
                            } else {
                                playNextSongShuffle();
                            }
                        }
                    });
                }
            });

            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generalMediaPlayer() {
        mediaPlayer = new MediaPlayer();

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

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";


        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = minutes + ":" + secondsString;

        return finalTimerString;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mTrackProgressBar.pause();

        downloadSongListViewModel.getSongs().removeObservers(this);
        favoriteSongsViewModel.getSongs().removeObservers(this);
        allPlaylistViewModel.getPlaylists().removeObservers(this);
        songsOfAlbumViewModel.getSongs().removeObservers(this);
        songsOfPlaylistViewModel.getSongs().removeObservers(this);
        songViewModel.getSong().removeObservers(this);
        songViewModel.getIsFavorite().removeObservers(this);
        favoriteSongsViewModel.getIsDeleteSuccess().removeObservers(this);
        favoriteSongsViewModel.getIsPostSuccess().removeObservers(this);
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
            songViewModel.checkFavorite(nextSong.getId(), userIdMydb);
            setData(nextSong);
            playAudio(nextSong.urlSong);
        } else {

            currentSongIndex = 0;

            Song firstSong = songList.get(currentSongIndex);
            songViewModel.checkFavorite(firstSong.getId(), userIdMydb);
            setData(firstSong);
            playAudio(firstSong.urlSong);
        }


        checkEnablePrevBtn();


    }

    private void playNextSongShuffle() {

        Random random = new Random();
        int randomNumber = random.nextInt(songList.size());
        mTrackProgressBar.pause();
        mSeekBar.setProgress(0);
        tv_currentTime.setText(milliSecondsToTimer(0));

        if (randomNumber < songList.size()) {

            Song nextSong = songList.get(randomNumber);
            songViewModel.checkFavorite(nextSong.getId(), userIdMydb);
            setData(nextSong);
            playAudio(nextSong.urlSong);
        }
    }

    private void checkEnablePrevBtn() {

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_prev_white_36dp);
        if (currentSongIndex == 0 || currentSongIndex == -1) {
            btn_prev.setEnabled(false);
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorGrayLight), PorterDuff.Mode.SRC_IN);
        } else {
            btn_prev.setEnabled(true);
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

    public class GetLyricTask extends AsyncTask<String, Void, String> {
        private TextView textView;

        public GetLyricTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);
        }
    }
}