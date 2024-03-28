package com.example.music_mobile_app;

import static com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable.USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_mobile_app.adapter.AddSongPlaylistAdapter;
import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;
import com.example.music_mobile_app.model.PlaylistItem;
import com.example.music_mobile_app.model.SongPlaylist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddSongPlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_playlist);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String playlistName = intent.getStringExtra("playlistName");
        String imageUrl = intent.getStringExtra("imageUrl");
        String playlistId = intent.getStringExtra("playlistId"); // Thêm dòng này để nhận playlistId từ Intent

        // Hiển thị dữ liệu trên giao diện
        TextView textSongName = findViewById(R.id.textSongName);
        ImageView songPlaylistAvt = findViewById(R.id.song_playlist_avt);
        textSongName.setText(playlistName);
        Picasso.get().load(imageUrl).into(songPlaylistAvt);

        ImageView buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Kiểm tra xem playlistId có giá trị null hay không trước khi gọi loadSpotifyPlaylistTracks
        if (playlistId != null) {
            // Gọi phương thức để lấy danh sách nhạc từ Spotify
            loadSpotifyPlaylistTracks(playlistId); // Thêm dòng này để gọi phương thức loadSpotifyPlaylistTracks với playlistId
        } else {
            Log.e("AddSongPlaylistActivity", "Playlist ID is null");
            Toast.makeText(this, "Playlist ID is null", Toast.LENGTH_SHORT).show();
        }
    }
    // Phương thức để lấy danh sách nhạc từ Spotify
    private void loadSpotifyPlaylistTracks(String playlistId) {
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(ConstantVariable.ACCESS_TOKEN);
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
}
