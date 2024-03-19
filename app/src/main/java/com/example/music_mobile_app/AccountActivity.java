package com.example.music_mobile_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;
import com.example.music_mobile_app.model.PlaylistItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapter.AccountAdapter;
import Adapter.PlaylistAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountActivity extends AppCompatActivity {

    CircleImageView imageAvt;
    RecyclerView listView;
    ArrayList<String> arrItem;
    PlaylistAdapter adapter;
    Button buttonEditAccount;
    ArrayList<PlaylistItem> playlistItems = new ArrayList<>();
    private static final String ACCESS_TOKEN = ConstantVariable.ACCESS_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        // Căn giữa tiêu đề
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.purple_50)));

        //View Logo
        imageAvt = (CircleImageView) findViewById(R.id.imageAvt);

        loadSpotifyPlaylists();

        //Inner List Item
        listView = (RecyclerView) findViewById(R.id.listViewPlaylist);
        adapter = new PlaylistAdapter(playlistItems);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);

        buttonEditAccount = (Button) findViewById(R.id.buttonEditAccount);
        //Onclick RegisterFree
        buttonEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
                startActivity(intent);
            }
        });

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
                Toast.makeText(AccountActivity.this, "Failed to load playlists from Spotify", Toast.LENGTH_SHORT).show();
            }
        });
    }




    // Xử lý sự kiện khi nút quay lại được nhấn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}