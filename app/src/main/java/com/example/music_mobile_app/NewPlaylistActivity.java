package com.example.music_mobile_app;

import static com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable.USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewPlaylistActivity extends AppCompatActivity {

        private EditText editText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_playlist);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            editText = findViewById(R.id.name_playlist);
            Button buttonCreate = findViewById(R.id.buttonCreate);
            buttonCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPlaylist();
                }
            });

            Button buttonExit = findViewById(R.id.buttonExit);
            buttonExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Kết thúc Activity và quay lại màn hình trước đó
                }
            });
        }

    private void createPlaylist() {
        String playlistName = editText.getText().toString().trim();

        if (!playlistName.isEmpty()) {
            // Gửi yêu cầu tạo playlist mới lên Spotify
            createPlaylistOnSpotify(playlistName);
        } else {
            editText.setError("Vui lòng nhập tên playlist");
        }
    }

    private void createPlaylistOnSpotify(String playlistName) {
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(ConstantVariable.ACCESS_TOKEN);
        SpotifyService spotifyService = spotifyApi.getService();

        // Tạo yêu cầu tạo playlist mới
        Map<String, Object> options = new HashMap<>();
        options.put("name", playlistName);
        spotifyService.createPlaylist(USER_ID, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                // Lấy ID của playlist mới được tạo
                String playlistID = playlist.id;
                // Trả về kết quả cho Activity trước và kết thúc Activity hiện tại
                Intent intent = new Intent();
                intent.putExtra("playlistID", playlistID);
                intent.putExtra("playlistName", playlistName);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("NewPlaylistActivity", "Error creating playlist on Spotify: " + error.getMessage());
                Toast.makeText(NewPlaylistActivity.this, "Failed to create playlist on Spotify", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish(); // Kết thúc Activity và quay lại màn hình trước đó
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }
