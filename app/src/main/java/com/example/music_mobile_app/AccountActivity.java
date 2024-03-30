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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.music_mobile_app.ui.AlbumFragment;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    CircleImageView imageAvt;
    ListView listView;
    ArrayList<PlaylistItem> playlistItems;
    AccountAdapter adapter;
    Button buttonEditAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

//        Toolbar toolbar = findViewById(R.id.app_bar);
////        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

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
        listView = (ListView) findViewById(R.id.listViewPlaylist);
        playlistItems = new ArrayList<>();
        adapter = new AccountAdapter(this, playlistItems);
        listView.setAdapter(adapter);

        // Bộ lắng nghe sự kiện cho ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Gọi phương thức goToAlbumFragment() khi item được chọn
                goToAlbumFragment();
            }
        });

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
    private void goToAlbumFragment() {
        // Tạo đối tượng FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Bắt đầu một giao dịch Fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thay thế Fragment hiện tại bằng Fragment đích
        fragmentTransaction.replace(R.id.layout_account, new AlbumFragment());

        // Thêm transaction vào ngăn xếp để có thể quay lại Fragment trước đó (nếu cần)
        fragmentTransaction.addToBackStack(null);

        // Thực hiện giao dịch
        fragmentTransaction.commit();
    }

    private void prepareData() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

    }

}
