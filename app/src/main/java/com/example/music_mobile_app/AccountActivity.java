package com.example.music_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.ui.AlbumFragment;
import com.spotify.sdk.android.auth.AuthorizationClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountActivity extends AppCompatActivity {

    public final String TAG = this.getClass().getSimpleName();
    private CircleImageView imageAvt;
    private RecyclerView recyclerView;
    private TextView tvName;
    private Button btnEditAccount;
    private Button btnLogout;
    private ImageView btnCreatePlaylist;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private ListManager listManager = MainActivity.listManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Căn giữa tiêu đề
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.purple_50)));


        imageAvt = findViewById(R.id.imageAvt);
        tvName = findViewById(R.id.textViewName);
        recyclerView = findViewById(R.id.playlist_recyclerview);
        btnEditAccount = findViewById(R.id.buttonEditAccount);
        btnLogout = findViewById(R.id.buttonLogout);
        btnCreatePlaylist = findViewById(R.id.btn_create_playlist);

        //Onclick RegisterFree
        btnEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, EditAccountActivity.class);
            startActivity(intent);
        });

        // Log out
        btnLogout.setOnClickListener(v -> {
            AuthorizationClient.clearCookies(getBaseContext());
            Log.d(TAG, "Logging out...");
            Intent intent = new Intent(this, AuthLoginActivity.class);
            startActivity(intent);

        });

        // Create playlist
        btnCreatePlaylist.setOnClickListener(v -> {
            showAddDialog();
        });

        prepareData();

        setPlaylist();

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

    private void goToDetailPlaylist() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_account, new AlbumFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void prepareData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String image = sharedPreferences.getString("imageUrl", "None");
        String name = sharedPreferences.getString("displayName", "None");
        tvName.setText(name);
        Glide.with(this).load(image).into(imageAvt);
        LinearLayoutManager playList_layout = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(playList_layout);
    }

    private void showAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_new_playlist, null);

        builder.setView(addView);

        EditText editText = addView.findViewById(R.id.name_playlist);
        Button btnCreate = addView.findViewById(R.id.btnCreate);
        Button btnExit = addView.findViewById(R.id.btnExit);

        AlertDialog alertDialog = builder.create();

        // Set click listener
        btnCreate.setOnClickListener(v -> {
            // Lấy văn bản từ EditText
            String playlistName = editText.getText().toString();
            createPlaylistOnSpotify(playlistName);
            alertDialog.dismiss();
        });


        btnExit.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }


    private void createPlaylistOnSpotify(String playlistName) {

        // Tạo yêu cầu tạo playlist mới
        Map<String, Object> options = new HashMap<>();
        options.put("name", playlistName);
        options.put("public", true);

        // Get user information
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        spotifyService.createPlaylist(USER_ID, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                // Lấy ID của playlist mới được tạo
                String playlistID = playlist.id;
                Log.d(TAG, "Create playlist success");
                // Reload playlist
                setPlaylist();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating playlist on Spotify: " + error.getMessage());
            }
        });
    }

    private void setPlaylist() {
//        List<PlaylistSimple> playlistsList = listManager.getPlaylistList();
//
//        if (playlistsList.isEmpty()) {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 20);
        spotifyService.getMyPlaylists(options, new SpotifyCallback<Pager<PlaylistSimple>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.e(TAG, "failure: " + spotifyError.getErrorDetails());
            }

            @Override
            public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                Log.d(TAG, "Get playlist success: ");
                List<PlaylistSimple> mList = playlistSimplePager.items;
                listManager.setPlaylistList(mList);
                ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), mList, getBaseContext());
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
            }
        });
//        }

    }

}
