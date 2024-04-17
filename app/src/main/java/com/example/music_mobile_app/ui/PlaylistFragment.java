package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private ImageView playlistImage;
    private ImageButton btnBack;
    private TextView playlistName, playlistOwner;

    private RecyclerView recyclerView;
    private FragmentManager manager;
    private PlaylistSimple playlistDetail;
    private FrameLayout fragment_container;
    private Drawable backgroundDrawable;
    private String baseImage = VariableManager.getVariableManager().baseImage;

    public PlaylistFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get detail playlist
        Bundle bundle = getArguments();
        if (bundle != null) {
            playlistDetail = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
        } else {
            Log.e(TAG, "Cannot get playlist detail");
        }

        manager = getParentFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // Hide header
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        header.setVisibility(View.GONE);

        preparedData(view);


        // Onclick back
        btnBack.setOnClickListener(v -> {
            manager.popBackStack();
        });

        setPlaylist();

        return view;

    }

    private void preparedData(View view) {

        playlistImage = view.findViewById(R.id.playlistImage);
        playlistName = view.findViewById(R.id.playlistName);
        playlistOwner = view.findViewById(R.id.playlistOwner);
        recyclerView = view.findViewById(R.id.playlist_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        btnBack = view.findViewById(R.id.backButton);

        playlistName.setText(playlistDetail.name);
        playlistOwner.setText(playlistDetail.owner.display_name);

        //get background framelayout
        fragment_container = view.findViewById(R.id.content_container);
        backgroundDrawable = fragment_container.getBackground();

        Glide.with(this).load((playlistDetail.images == null) ? baseImage : playlistDetail.images.get(0).url)
                .override(Target.SIZE_ORIGINAL)
                .into(new ImageViewTarget<Drawable>(playlistImage) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        // Khi quá trình tải ảnh hoàn thành, resource sẽ chứa Drawable
                        if (resource != null) {
                            // setImageDrawable cho artistImage
                            playlistImage.setImageDrawable(resource);

                            // Xử lý background => Đổi màu theo ảnh của artist
                            HandleBackground backgroundHandler = new HandleBackground();
                            backgroundHandler.handleBackground(playlistImage, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
                                @Override
                                public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                                    // Set the updated Drawable as the background of your view
                                    fragment_container.setBackground(updatedDrawable);
                                }
                            });
                        } else {
                            // Xử lý khi không thể tải được Drawable
                        }
                    }
                });



    }

    private void setPlaylist() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            PlaylistSimple playlistDetail = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            List<Track> trackList = new ArrayList<>();
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof kaaes.spotify.webapi.android.models.Track) {
                    trackList.add((Track) parcelable);
                }
            }
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(),
                    getParentFragment());
            // Set send to detail
            adapter.setSend(false);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "Cannot get album detail");
        }
    }

    private void removeTrackFromPlaylist(String playlistId, Track mTrack) {
        // Tạo một instance của TracksToRemove và thêm các TrackToRemove vào danh sách
        TracksToRemove tracksToRemove = new TracksToRemove();
        tracksToRemove.tracks = new ArrayList<>();

        // Tạo một TrackToRemove và thêm trackId vào URI
        TrackToRemove trackToRemove = new TrackToRemove();
        trackToRemove.uri = mTrack.uri;
        tracksToRemove.tracks.add(trackToRemove);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");

        // Gọi service để xóa bài hát khỏi playlist
        spotifyService.removeTracksFromPlaylist(USER_ID, playlistId, tracksToRemove, new Callback<SnapshotId>() {
            @Override
            public void success(SnapshotId snapshotId, Response response) {
                Log.d(TAG, "Remove track from playlist success");
                Toast.makeText(getContext(), "Đã xóa track thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Remove track from playlist failed: " + error.getMessage());
                Toast.makeText(getContext(), "Xóa track thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
