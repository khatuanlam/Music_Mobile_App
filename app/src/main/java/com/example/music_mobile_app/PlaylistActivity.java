package com.example.music_mobile_app;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.music_mobile_app.adapter.PlaylistAdapter;
//import com.example.music_mobile_app.adapter.SongPlaylistAdapter;
import com.example.music_mobile_app.model.PlaylistItem;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
public class PlaylistActivity extends AppCompatActivity implements PlaylistAdapter.OnPlaylistItemClickListener {
    private ListView listView;
    private PlaylistAdapter adapter;
    private ArrayList<PlaylistItem> playlistItems = new ArrayList<>();
    private int selectedItem = -1; // Biến lưu trữ vị trí item được chọn, -1 là giá trị mặc định không có item nào được chọn
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        listView = findViewById(R.id.listAddPlaylist);
        adapter = new PlaylistAdapter(this,playlistItems);
        adapter.setOnPlaylistItemClickListener(this);
        listView.setAdapter(adapter);
        loadSpotifyPlaylists();
        Button buttonAddPlaylist = findViewById(R.id.buttonAddPlaylist);
        buttonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlaylistActivity.this, NewPlaylistActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });

    }

    @Override
    public void onPlaylistItemClick(PlaylistItem playlistItem) {
        Log.e("Item", "Click");
        // Xử lý sự kiện khi một item được nhấn trong Activity
        String playlistId = playlistItem.getId();

        Button buttonOK = findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trackName = getIntent().getStringExtra("trackName");
                String trackID = getIntent().getStringExtra("trackID");

                Intent intent = new Intent(PlaylistActivity.this, AddSongPlaylistActivity.class);
                intent.putExtra("playlistId", playlistId);
                intent.putExtra("playlistName", playlistItem.getName());
                intent.putExtra("trackID", trackID);
                intent.putExtra("trackName", trackName);

                // Kiểm tra xem Intent có chứa dữ liệu hình ảnh của bài hát hay không
                if (getIntent().hasExtra("trackImage")) {
                    String trackImageUrl = getIntent().getStringExtra("trackImage");
                    Log.d("PlaylistActivity", "Received track image URL: " + trackImageUrl);
                    intent.putExtra("trackImage", trackImageUrl);
                } else {
                    Log.d("PlaylistActivity", "No track image URL received");
                }

                Log.e("Button", "Click");


                if (!playlistItem.getImages().isEmpty()) {
                    intent.putExtra("imageUrl", playlistItem.getImages().get(0).url);
                }
                startActivity(intent);
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
            }
        }
    }
    // Load danh sách playlist từ Spotify
    private void loadSpotifyPlaylists() {
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(MainActivity.authToken);
        SpotifyService spotifyService = spotifyApi.getService();
        // Gọi API để lấy danh sách playlist
        spotifyService.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                // Xử lý kết quả thành công
                // Lấy danh sách playlist từ kết quả và hiển thị lên ListView
                ArrayList<PlaylistItem> spotifyPlaylists = new ArrayList<>();
                for (PlaylistSimple playlist : playlistSimplePager.items) {
                    // Tạo đối tượng PlaylistItem từ dữ liệu playlist
                    List<Image> images = playlist.images;
                    String id = playlist.id;
                    String name = playlist.name;
                    PlaylistItem item = new PlaylistItem(id, images, name);
                    spotifyPlaylists.add(item);
                }
                // Hiển thị danh sách playlist lên ListView
                playlistItems.addAll(spotifyPlaylists);
                // Xác định danh sách playlist đã được chọn
                listView.setItemChecked(selectedItem, true);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void failure(RetrofitError error) {
                // Xử lý khi gặp lỗi
                Log.e("AddToPlaylistActivity", "Error loading playlists from Spotify: " + error.getMessage());
                Toast.makeText(PlaylistActivity.this, "Failed to load playlists from Spotify", Toast.LENGTH_SHORT).show();
            }
        });
    }
}