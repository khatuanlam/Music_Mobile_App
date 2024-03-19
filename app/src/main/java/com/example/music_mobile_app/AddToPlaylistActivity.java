package com.example.music_mobile_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.PlaylistAdapter;

import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;
import com.example.music_mobile_app.model.PlaylistItem;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddToPlaylistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private ArrayList<PlaylistItem> playlistItems = new ArrayList<>();
    private static final String ACCESS_TOKEN = ConstantVariable.ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_playlist);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.listAddPlaylist);
        adapter = new PlaylistAdapter(playlistItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadSpotifyPlaylists();

        Button buttonAddPlaylist = findViewById(R.id.buttonAddPlaylist);
        buttonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddToPlaylistActivity.this, NewPlaylistActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện các xử lý khi nhấn nút OK
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String playlistID = data.getStringExtra("playlistID");
            String playlistName = data.getStringExtra("playlistName");
            if (playlistName != null && playlistID != null) {
                PlaylistItem item = new PlaylistItem(playlistID, null, playlistName);
                playlistItems.add(item);
                adapter.notifyDataSetChanged();

                // Add new playlist to Spotify
//                addPlaylistToSpotify(playlistName);
            }
        }
    }

    // Load danh sách playlist từ Spotify
    private void loadSpotifyPlaylists() {
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(ACCESS_TOKEN);
        SpotifyService spotifyService = spotifyApi.getService();

        // Gọi API để lấy danh sách playlist
        spotifyService.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                // Xử lý kết quả thành công
                // Lấy danh sách playlist từ kết quả và hiển thị lên RecyclerView hoặc ListView
                ArrayList<PlaylistItem> spotifyPlaylists = new ArrayList<>();
                for (PlaylistSimple playlist : playlistSimplePager.items) {
                    // Tạo đối tượng PlaylistItem từ dữ liệu playlist
                    List<Image> images = playlist.images;
                    String id = playlist.id;
                    String name = playlist.name;
                    PlaylistItem item = new PlaylistItem(id, images, name);
                    spotifyPlaylists.add(item);
                }

                // Hiển thị danh sách playlist lên RecyclerView
                playlistItems.addAll(spotifyPlaylists);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                // Xử lý khi gặp lỗi
                Log.e("AddToPlaylistActivity", "Error loading playlists from Spotify: " + error.getMessage());
                Toast.makeText(AddToPlaylistActivity.this, "Failed to load playlists from Spotify", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
