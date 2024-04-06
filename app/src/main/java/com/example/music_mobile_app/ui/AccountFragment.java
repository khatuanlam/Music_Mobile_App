package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.AuthLoginActivity;
import com.example.music_mobile_app.EditAccountActivity;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
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

public class AccountFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();
    private CircleImageView imageAvt;
    private RecyclerView recyclerView;
    private TextView tvName;
    private Button btnEditAccount, btnBack;
    private Button btnLogout;
    private ImageView btnCreatePlaylist;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private ListManager listManager = MainActivity.listManager;
    private FragmentManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Hide header
        RelativeLayout header = getParentFragment().getView().findViewById(R.id.header);
        header.setVisibility(View.GONE);

        prepareData(view);

        // Onclick back
        btnBack.setOnClickListener(v -> {
            manager.popBackStack();
        });

        // Onclick RegisterFree
        btnEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditAccountActivity.class);
            startActivity(intent);
        });
        // Log out
        btnLogout.setOnClickListener(v -> {
            // Clear data save
            listManager.clear();
            AuthorizationClient.clearCookies(getContext());
            Log.d(TAG, "Logging out...");
            Intent intent = new Intent(getActivity(), AuthLoginActivity.class);
            startActivity(intent);
        });
        // Create playlist
        btnCreatePlaylist.setOnClickListener(v -> {
            showAddDialog();
        });

        setPlaylist(false);

        return view;
    }

    private void prepareData(View view) {

        imageAvt = view.findViewById(R.id.account_imageAvt);
        tvName = view.findViewById(R.id.textViewName);
        recyclerView = view.findViewById(R.id.playlist_recyclerview);
        btnEditAccount = view.findViewById(R.id.buttonEditAccount);
        btnLogout = view.findViewById(R.id.buttonLogout);
        btnBack = view.findViewById(R.id.back);
        btnCreatePlaylist = view.findViewById(R.id.btn_create_playlist);

        // Setting user profile
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String image = sharedPreferences.getString("imageUrl", "None");
        String name = sharedPreferences.getString("displayName", "None");
        tvName.setText(name);
        Glide.with(this).load(image).into(imageAvt);
        LinearLayoutManager playList_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(playList_layout);
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        spotifyService.createPlaylist(USER_ID, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                // Lấy ID của playlist mới được tạo
                String playlistID = playlist.id;
                Log.d(TAG, "Create playlist success");
                // Reload playlist
                setPlaylist(true);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error creating playlist on Spotify: " + error.getMessage());
            }
        });
    }

    private void setPlaylist(Boolean type) {
        List<PlaylistSimple> playlistsList = listManager.getPlaylistList();
        if (playlistsList.isEmpty() || type == true) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 20);
            spotifyService.getMyPlaylists(options, new SpotifyCallback<Pager<PlaylistSimple>>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "failure: " + spotifyError.getMessage());
                }

                @Override
                public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                    Log.d(TAG, "Get playlist success: ");
                    List<PlaylistSimple> mList = playlistSimplePager.items;
                    listManager.setPlaylistList(mList);
                    setPlaylist(false);
                }
            });
        }
        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), null, playlistsList, getContext(),
                getParentFragment());
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
    }
}
