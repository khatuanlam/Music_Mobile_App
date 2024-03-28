package com.example.music_mobile_app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;
import com.example.music_mobile_app.model.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

import com.example.music_mobile_app.adapter.AccountAdapter;
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
    ListView listView;
    ArrayList<PlaylistItem> playlistItems;
    AccountAdapter adapter;
    Button buttonEditAccount;
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

        listView = findViewById(R.id.listViewPlaylist);
        playlistItems = new ArrayList<>();
        adapter = new AccountAdapter(this, playlistItems);
        listView.setAdapter(adapter);

        // Set up buttonEditAccount onClickListener
        buttonEditAccount = findViewById(R.id.buttonEditAccount);
        buttonEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
                startActivity(intent);
            }
        });

        // Load Spotify playlists
        loadSpotifyPlaylists();
    }

    // Load danh sách playlist từ Spotify
    private void loadSpotifyPlaylists() {
        SpotifyApi spotifyApi = new SpotifyApi();
        spotifyApi.setAccessToken(ACCESS_TOKEN);
        SpotifyService spotifyService = spotifyApi.getService();

        spotifyService.getMyPlaylists(new Callback<Pager<PlaylistSimple>>() {
            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                List<PlaylistSimple> playlists = playlistSimplePager.items;
                for (PlaylistSimple playlist : playlists) {
                    List<Image> images = playlist.images;
                    String id = playlist.id;
                    String name = playlist.name;
                    PlaylistItem item = new PlaylistItem(id, images, name);
                    playlistItems.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("AccountActivity", "Error loading playlists from Spotify: " + error.getMessage());
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