package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.AuthLoginActivity;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.util.HandleBackground;
import com.spotify.sdk.android.auth.AuthorizationClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();
    private CircleImageView imageAvt;
    private RecyclerView recyclerView;
    private TextView tvName;
    private Button btnBack;
    private Button btnLogout;
    private ImageView btnCreatePlaylist;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private FragmentManager manager;
    private LinearLayout layout_account;
    private Drawable backgroundDrawable;
    private ItemHorizontalAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
        EventBus.getDefault().register(this); // Đăng ký Fragment với EventBus

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Hide header
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        header.setVisibility(View.GONE);

        prepareData(view);

        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setUserPlaylist(true);
            }
        });

        // Onclick back
        btnBack.setOnClickListener(v -> {
            header.setVisibility(View.VISIBLE);
            manager.popBackStack();
        });

        // Log out
        btnLogout.setOnClickListener(v -> {
            // Clear data save
            ListManager.getInstance().clear();
            AuthorizationClient.clearCookies(getActivity());
            Log.d(TAG, "Logging out...");
            Intent intent = new Intent(getActivity(), AuthLoginActivity.class);
            startActivity(intent);
        });
        // Create playlist
        btnCreatePlaylist.setOnClickListener(v -> {
            add_new_Playlist();
        });

        setUserPlaylist(false);

        return view;
    }


    private void prepareData(View view) {

        imageAvt = view.findViewById(R.id.account_imageAvt);
        tvName = view.findViewById(R.id.textViewName);
        recyclerView = view.findViewById(R.id.playlist_recyclerview);
        btnLogout = view.findViewById(R.id.buttonLogout);
        btnBack = view.findViewById(R.id.back);
        btnCreatePlaylist = view.findViewById(R.id.btn_create_playlist);

        //Update: Bổ sung xử lý background thay đổi theo hình của artist
        //get background framelayout
        layout_account = view.findViewById(R.id.layout_account);
        backgroundDrawable = layout_account.getBackground();

        // Setting user profile
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String image = sharedPreferences.getString("imageUrl", "None");
        String name = sharedPreferences.getString("displayName", "None");
        tvName.setText(name);
        Glide.with(this).load(image).into(new ImageViewTarget<Drawable>(imageAvt) {
            @Override
            protected void setResource(@Nullable Drawable resource) {
                // Khi quá trình tải ảnh hoàn thành, resource sẽ chứa Drawable
                if (resource != null) {
                    // setImageDrawable cho artistImage
                    imageAvt.setImageDrawable(resource);

                    // Xử lý background => Đổi màu theo ảnh của artist
                    HandleBackground backgroundHandler = new HandleBackground();
                    backgroundHandler.handleBackground(imageAvt, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
                        @Override
                        public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                            // Set the updated Drawable as the background of your view
                            layout_account.setBackground(updatedDrawable);
                        }
                    });
                } else {
                    // Xử lý khi không thể tải được Drawable
                }
            }
        });
        LinearLayoutManager playList_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(playList_layout);
    }

    private void add_new_Playlist() {
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
            MethodsManager.getInstance().createPlaylistOnSpotify(this.getActivity(), playlistName, new ListenerManager.OnCreatePlaylistCompleteListener() {
                @Override
                public void onComplete(Playlist playlist) {
                    // Set back new playlist to adapter
                    setUserPlaylist(true);
                }

                @Override
                public void onError(Throwable error) {

                }
            });
            alertDialog.dismiss();
        });

        btnExit.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    public void setUserPlaylist(boolean permission) {
        List<PlaylistSimple> playlistsList = ListManager.getInstance().getPlaylistList();
        if (playlistsList.isEmpty() || permission == true) {
            // Nếu danh sách playlist chưa được lấy thì load lại để lấy
            MethodsManager.getInstance().getUserPlaylists(permission, new ListenerManager.OnGetPlaylistCompleteListener() {
                @Override
                public void onComplete(List<PlaylistSimple> playlistSimpleList) {
                    ListManager.getInstance().setPlaylistList(playlistSimpleList);
                    setUserPlaylist(false);
                }

                @Override
                public void onError(Throwable error) {

                }
            });
        }
        adapter = new ItemHorizontalAdapter(new ArrayList<>(), null, playlistsList, getContext(), this);
        recyclerView.setAdapter(adapter);
    }

    // Reload user's playlists
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(PlaylistSimple playlistSimpleList) {
        Toast.makeText(this.getActivity(), playlistSimpleList.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}


