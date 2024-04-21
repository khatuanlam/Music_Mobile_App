package com.example.music_mobile_app.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.FollowingAdapter;
import com.example.music_mobile_app.adapter.ItemAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsCursorPager;
import kaaes.spotify.webapi.android.models.NewReleases;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

public class HomeFragment extends Fragment {
    public final String TAG = this.getClass().getSimpleName();
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private RecyclerView recommendationsRecyclerView;
    private RecyclerView topTracksRecyclerView;
    private RecyclerView albumsRecycleView;
    private RecyclerView followRecycleView;
    private NestedScrollView homeView;
    private TextView title;
    private Drawable backgroundDrawable;
    public final ListManager listManager = MainActivity.listManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Show header
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        header.setVisibility(View.VISIBLE);

        prepareData(view);

        handleBackground();

        updateUI();

        return view;
    }

    private void handleBackground() {
        // Lấy giá trị màu integer từ tài nguyên màu
        int startColorInt = ContextCompat.getColor(getContext(), R.color.purple_100);
        // Chuyển đổi giá trị màu integer thành mã hex
        String startColorHex = String.format("#%06X", (0xFFFFFFFF & startColorInt)); // Bỏ đi hai ký tự đầu tiên (alpha
        // channel)

        // Xử lý background
        HandleBackground backgroundHandler = new HandleBackground();
        backgroundHandler.handleBackground(startColorHex, backgroundDrawable,
                new HandleBackground.OnPaletteGeneratedListener() {
                    @Override
                    public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                        // Set the updated Drawable as the background of your view
                        title.setBackground(updatedDrawable);
                    }
                });
    }

    private void prepareData(View view) {
        recommendationsRecyclerView = view.findViewById(R.id.recommendation);
        topTracksRecyclerView = view.findViewById(R.id.top_tracks);
        albumsRecycleView = view.findViewById(R.id.top_albums);
        followRecycleView = view.findViewById(R.id.follower_recyclerView);
        homeView = view.findViewById(R.id.scroll_view_home);

        LinearLayoutManager recommendTracks_layout = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager topTracks_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false);
        LinearLayoutManager albums_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false);
        LinearLayoutManager follow_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
                false);

        recommendationsRecyclerView.setLayoutManager(recommendTracks_layout);
        topTracksRecyclerView.setLayoutManager(topTracks_layout);
        albumsRecycleView.setLayoutManager(albums_layout);
        followRecycleView.setLayoutManager(follow_layout);

        // get background title
        title = view.findViewById(R.id.title);
        backgroundDrawable = title.getBackground();

        homeView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
                                       int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // Gọi sự kiện khi kéo trang lên hết cỡ
                    // Đặt mã hoặc phương thức bạn muốn gọi ở đây
                    reloadPage();
                }
            }
        });
    }

    private void updateUI() {
        // setRecentlyTracks();
        setRecommendations();
        setTopTracks();
        setAlbums();
        setFollowArtist();

        // Get user's playlists
        MethodsManager.getInstance().getUserPlaylists(false);
        // Get user's favorites
        MethodsManager.getInstance().getUserFavorite(false);
    }

    private void setRecommendations() {
        List<Track> listTracks = listManager.getRecommendTracks();
        if (listTracks.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 10);
            options.put("seed_artists", "35HU1GT1q797EwZsP8uduO");
            options.put("seed_tracks", "67wUiBqwSYzf5GVvswMJ6p");

            spotifyService.getRecommendations(options, new SpotifyCallback<Recommendations>() {
                @Override
                public void success(Recommendations recommendations, Response response) {
                    Log.d(TAG, "Get recommend success");
                    List<Track> mList = recommendations.tracks;
                    listManager.setRecommendTracks(mList);
                    setRecommendations();
                }

                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "Cannot get recommend " + spotifyError.getMessage());
                }
            });
            Log.d(TAG, "setRecommendations: " + listTracks.size());
        } else {
            ItemAdapter adapter = new ItemAdapter(listTracks, new ArrayList<>(), getParentFragment());
            adapter.notifyDataSetChanged();
            recommendationsRecyclerView.setAdapter(adapter);
        }
    }

    private void setTopTracks() {
        List<Track> listTracks = ListManager.getInstance().getTopTracks();
        if (listTracks.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 20);
            options.put(SpotifyService.OFFSET, 10);
            spotifyService.getTopTracks(options, new SpotifyCallback<Pager<Track>>() {
                @Override
                public void success(Pager<Track> trackPager, Response response) {
                    Log.d(TAG, "Get top tracks");
                    List<Track> mList = trackPager.items;
                    listManager.setTopTracks(mList);
//                    setTopTracks();
                }

                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "Can't get top track" + spotifyError.getMessage());
                }
            });
        } else {
            ItemAdapter adapter = new ItemAdapter(listTracks, new ArrayList<>(), getParentFragment());
            adapter.notifyDataSetChanged();
            topTracksRecyclerView.setAdapter(adapter);
        }
    }

    private void setAlbums() {

        List<AlbumSimple> listAlbums = listManager.getFavoriteAlbums();
        if (listAlbums.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 12);
            options.put(SpotifyService.OFFSET, new Random().nextInt(10));
            spotifyService.getNewReleases(options, new SpotifyCallback<NewReleases>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, "Can't get album " + spotifyError.getMessage());
                }

                @Override
                public void success(NewReleases newReleases, Response response) {
                    Log.d(TAG, "Get new album ");
                    List<AlbumSimple> mList = newReleases.albums.items;
                    listManager.setAlbums(mList);
                    setAlbums();
                }
            });
        } else {
            ItemAdapter adapter = new ItemAdapter(new ArrayList<>(), listAlbums, getParentFragment());
            adapter.notifyDataSetChanged();
            albumsRecycleView.setAdapter(adapter);
        }
    }

    private void setFollowArtist() {
        List<Artist> followedArtists = ListManager.getInstance().getFollowArtists();
        if (followedArtists.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 20);
            options.put("type", "artist");
            spotifyService.getFollowedArtists(options, new SpotifyCallback<ArtistsCursorPager>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    if (spotifyError.hasErrorDetails()) {
                        Log.e(TAG, "Error code: " + spotifyError.getErrorDetails().status + ", Message: "
                                + spotifyError.getErrorDetails().message);
                    } else {
                        Log.e(TAG, "An error occurred: " + spotifyError.getMessage());
                    }
                }

                @Override
                public void success(ArtistsCursorPager artistsCursorPager, Response response) {
                    Log.d(TAG, "Get followed artists success");
                    List<Artist> followedArtists = artistsCursorPager.artists.items;
                    listManager.setFollowArtists(followedArtists);
                    setFollowArtist();
                }
            });
        } else {
            // Tạo adapter mới và cập nhật RecyclerView
            FollowingAdapter adapter = new FollowingAdapter(followedArtists, getParentFragment());
            adapter.notifyDataSetChanged();
            followRecycleView.setAdapter(adapter);
        }
    }

    private void reloadPage() {
        // ListManager.getInstance().clear();
        // updateUI();
    }

}
