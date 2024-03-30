package com.example.music_mobile_app;

import static com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable.USER_ID;
import static com.example.music_mobile_app.ui.HomeFragment.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_mobile_app.adapter.AddSongPlaylistAdapter;
import com.example.music_mobile_app.model.PlaylistItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddSongPlaylistActivity extends AppCompatActivity {
    String trackName, trackID, playlistId;
    List<Image> trackImages = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_playlist);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String playlistName = intent.getStringExtra("playlistName");
        String imageUrl = intent.getStringExtra("imageUrl");
        playlistId = intent.getStringExtra("playlistId"); // Thêm dòng này để nhận playlistId từ Intent

        trackID = intent.getStringExtra("trackID");
        trackName = intent.getStringExtra("trackName");
        String trackImageUrl = intent.getStringExtra("trackImage");


        if (imageUrl != null && !imageUrl.isEmpty()) {
            trackImages = new ArrayList<>();
            Image image = new Image();
            image.url = trackImageUrl; // Gán URL cho hình ảnh
            trackImages.add(image);
        }


        // Hiển thị dữ liệu trên giao diện
        TextView textSongName = findViewById(R.id.textSongName);
        ImageView songPlaylistAvt = findViewById(R.id.song_playlist_avt);
        textSongName.setText(playlistName);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(songPlaylistAvt);
        }

        // Gọi phương thức để thêm bài hát vào playlist khi cần
        // Kiểm tra xem playlistId và trackID có dữ liệu hay không trước khi gọi phương thức addTrack
        if (playlistId != null && trackID != null) {
            Log.d(TAG, "Track ID: " + trackID);
            Log.d(TAG, "playlist ID: " + playlistId);
            addTrack(USER_ID, playlistId, trackID);
        } else {
            // Xử lý khi một trong số các giá trị là null
            Log.e("AddSongPlaylistActivity", "Playlist ID or Track ID is null");
            Toast.makeText(this, "Playlist ID or Track ID is null", Toast.LENGTH_SHORT).show();
        }


        ImageView buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Kiểm tra xem playlistId có giá trị null hay không trước khi gọi loadSpotifyPlaylistTracks
        if (playlistId != null) {
            loadSpotifyPlaylistTracks(playlistId);

        } else {
            Log.e("AddSongPlaylistActivity", "Playlist ID is null");
            Toast.makeText(this, "Playlist ID is null", Toast.LENGTH_SHORT).show();
        }
    }
    // Phương thức để lấy danh sách nhạc từ Spotify
    private void loadSpotifyPlaylistTracks(String playlistId) {
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(MainActivity.authToken);
        SpotifyService spotifyService = spotifyApi.getService();
        // Gọi API để lấy danh sách nhạc từ playlist có playlistId
        spotifyService.getPlaylistTracks(USER_ID, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                // Xử lý kết quả thành công
                // Lấy danh sách nhạc từ kết quả và hiển thị lên giao diện
                ArrayList<PlaylistItem> playlistTracks = new ArrayList<>();
                for (PlaylistTrack playlistTrack : playlistTrackPager.items) {
                    // Tạo đối tượng SongPlaylist từ dữ liệu playlistTrack
                    String id = playlistTrack.track.id;
                    String trackName = playlistTrack.track.name;
                    List<Image> imageUrl = playlistTrack.track.album.images; // Lấy URL hình ảnh từ album của track
                    PlaylistItem song = new PlaylistItem(id, imageUrl, trackName);
                    playlistTracks.add(song);
                }

                // Add the saved trackName and trackImages to the first item
                if (!playlistTracks.isEmpty() && trackImages != null && !trackImages.isEmpty()) {
                    for (PlaylistItem song : playlistTracks) {
                        // Kiểm tra xem trackName có trùng với tên nhạc trong danh sách không
                        if (song.getName().equals(trackName)) {
                            // Nếu tên trùng khớp, không thêm nhạc mới vào danh sách
                            return;
                        }
                    }
                    // Nếu không có nhạc có tên trùng khớp, thêm nhạc mới vào cuối danh sách
                    PlaylistItem newSong = new PlaylistItem(null, trackImages, trackName);
                    playlistTracks.add(newSong);
                } else {
                    Log.e("AddSongPlaylistActivity", "Track images are null or empty");
                    // Nếu không có nhạc có tên trùng khớp, thêm nhạc mới vào cuối danh sách
                    PlaylistItem newSong = new PlaylistItem(null, null, trackName);
                    playlistTracks.add(newSong);
                }

                // Hiển thị danh sách nhạc lên giao diện
                ListView listView = findViewById(R.id.listview_song_playlist);
                AddSongPlaylistAdapter adapter = new AddSongPlaylistAdapter(AddSongPlaylistActivity.this, playlistTracks);
                listView.setAdapter(adapter);

            }
            @Override
            public void failure(RetrofitError error) {
                // Xử lý khi gặp lỗi
                Log.e("AddSongPlaylistActivity", "Error loading playlist tracks from Spotify: " + error.getMessage());
                Toast.makeText(AddSongPlaylistActivity.this, "Failed to load playlist tracks from Spotify", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Thêm phương thức để thêm bài hát vào Spotify playlist
    private void addTrack(String userId, String playlistId, String trackId) {
        SpotifyApi spotifyApi = new SpotifyApi();
        //Chỉnh sửa lại Token chỗ này nha
        spotifyApi.setAccessToken("BQDpszuJvW89rjGadwcxr32Z9iWj9jxB3qPfXZEIPGJMKg3s2Zyxvz97nbIglJiprvVK6aZSm93umlLZSj4F0F10NgrOTLdrgNmtLd6i5npdJqv9A96E5eAQWMvmNe7qP44qJY4th0GHdM4r5RFosxfCoAn5F6W0geL_8HHm40i4S18kq9g_YWdZJDamQgZW3d1xXc4u4lJ00NQhqzNZhf2B3RDTY0DaSIIPNHTrJMSfkelfokUpomF2Ukh0hEERMmRUH2WQj3OV-pIAkxg7yOZpDQ");
        SpotifyService spotifyService = spotifyApi.getService();

        // Tạo tham số cho yêu cầu POST
        Map<String, Object> queryParams = new HashMap<>();

        Map<String, Object> bodyParams = new HashMap<>();
        List<String> uris = new ArrayList<>();
        uris.add("spotify:track:" + trackId); // Thêm URI của bài hát vào danh sách
        bodyParams.put("uris", uris);

        // Gọi API để thêm bài hát vào playlist
        spotifyService.addTracksToPlaylist(USER_ID, playlistId, queryParams, bodyParams, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                // Xử lý kết quả thành công
                Toast.makeText(AddSongPlaylistActivity.this, "Track added to playlist successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                // Xử lý khi gặp lỗi
                Log.e("AddSongPlaylistActivity", "Error adding track to playlist: " + error.getMessage());
                Toast.makeText(AddSongPlaylistActivity.this, "Failed to add track to playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
