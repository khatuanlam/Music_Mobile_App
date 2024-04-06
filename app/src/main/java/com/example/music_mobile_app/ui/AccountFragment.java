
package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.AuthLoginActivity;
import com.example.music_mobile_app.EditAccountActivity;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.FollowingAdapter;
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
import kaaes.spotify.webapi.android.models.Artist;

import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class AccountFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();
    private CircleImageView imageAvt;
    private RecyclerView recyclerView;
    private TextView tvName, textViewFollow;
    private Button btnEditAccount, btnBack;
    private Button btnLogout;
    private ImageView btnCreatePlaylist;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private ListManager listManager = MainActivity.listManager;
    String artist, artistId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Bundle bundle = getArguments();

        imageAvt = view.findViewById(R.id.imageAvt);
        tvName = view.findViewById(R.id.textViewName);
        recyclerView = view.findViewById(R.id.playlist_recyclerview);
        btnEditAccount = view.findViewById(R.id.buttonEditAccount);
        btnLogout = view.findViewById(R.id.buttonLogout);
        btnBack = view.findViewById(R.id.back);
        btnCreatePlaylist = view.findViewById(R.id.btn_create_playlist);
        textViewFollow = view.findViewById(R.id.textViewFollow);


//        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), new ArrayList<>(), getContext());
//        recyclerView.setAdapter(adapter);
//
//        // Thiết lập trình quản lý bố cục cho RecyclerView
//        LinearLayoutManager playList_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(playList_layout);

        //Onclick back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        //Onclick RegisterFree
        btnEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditAccountActivity.class);
            startActivity(intent);
        });

        // Log out
        btnLogout.setOnClickListener(v -> {
            AuthorizationClient.clearCookies(getContext());
            Log.d(TAG, "Logging out...");
            Intent intent = new Intent(getActivity(), AuthLoginActivity.class);
            startActivity(intent);
        });

        // Create playlist
        btnCreatePlaylist.setOnClickListener(v -> {
            showAddDialog();
        });

        textViewFollow.setOnClickListener(v -> {
            showFollowerDialog();
        });

        prepareData();
        setPlaylist();
        return view;

    }

    private void showFollowerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_followartist, null);

        builder.setView(addView);

        // Tạo adapter và gán cho RecyclerView
        recyclerView = addView.findViewById(R.id.follower_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        Button buttonBack = addView.findViewById(R.id.back_dialog_follow);

        AlertDialog alertDialog = builder.create();
        setFollower(); // Gọi hàm này sau khi gán adapter cho RecyclerView

        alertDialog.show();

        buttonBack.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String image = sharedPreferences.getString("imageUrl", "None");
        String name = sharedPreferences.getString("displayName", "None");
        tvName.setText(name);
        Glide.with(this).load(image).into(imageAvt);
        LinearLayoutManager playList_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
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

    private void setFollower() {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 20); // Số lượng nghệ sĩ tối đa bạn muốn lấy
        options.put("type", "artist"); // Đặt loại để chỉ lấy nghệ sĩ

        spotifyService.getFollowedArtists(options, new SpotifyCallback<ArtistsCursorPager>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                if (spotifyError.hasErrorDetails()) {
                    Log.e(TAG, "Error code: " + spotifyError.getErrorDetails().status + ", Message: " + spotifyError.getErrorDetails().message);
                } else {
                    Log.e(TAG, "An error occurred: " + spotifyError.getMessage());
                }
            }


            @Override
            public void success(ArtistsCursorPager artistsCursorPager, Response response) {
                Log.d(TAG, "Get followed artists success");
                List<Artist> followedArtists = artistsCursorPager.artists.items;
                // Tạo adapter mới và cập nhật RecyclerView
                FollowingAdapter adapter = new FollowingAdapter(followedArtists, getContext());
                recyclerView.setAdapter(adapter);
            }
        });
    }



    private void setPlaylist() {
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
                ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), mList, getContext());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        });
    }

//    private void getFollowedArtists() {
//        Map<String, Object> options = new HashMap<>();
//        options.put(SpotifyService.LIMIT, 50); // Set the limit as needed
//
//        spotifyService.getFollowedArtists(options, new SpotifyCallback<ArtistsCursorPager>() {
//            @Override
//            public void success(ArtistsCursorPager artistsCursorPager, Response response) {
//                // Xử lý danh sách nghệ sĩ đã được follow ở đây
//                List<Artist> followedArtists = artistsCursorPager.artists.items;
//                // Bạn có thể làm bất cứ điều gì bạn muốn với danh sách này
//            }
//
//            @Override
//            public void failure(SpotifyError spotifyError) {
//                Log.e(TAG, "failure: " + spotifyError.getErrorDetails());
//            }
//        });
//    }
//
//    // Phương thức để hiển thị hoặc ẩn danh sách playlist
//    private void togglePlaylistContainerVisibility() {
//        LinearLayout playlistContainer = getView().findViewById(R.id.playlist_container);
//        if (playlistContainer.getVisibility() == View.VISIBLE) {
//            playlistContainer.setVisibility(View.GONE); // Nếu đang hiển thị, ẩn đi
//        } else {
//            playlistContainer.setVisibility(View.VISIBLE); // Nếu đang ẩn, hiển thị lên
//        }
//    }
}
