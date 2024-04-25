package com.example.music_mobile_app.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.spotify.sdk.android.auth.AuthorizationHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MethodsManager {
    private static MethodsManager methodsManager;
    private SpotifyService spotifyService = MainActivity.spotifyService;

    private static String selectedPlaylistId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(PlaylistSimple playlistSimple) {
        selectedPlaylistId = playlistSimple.id;
        String name = playlistSimple.name;
        Log.d("", "onDataEvent: " + name);
    }

    public MethodsManager() {
        EventBus.getDefault().register(this);
    }

    public static MethodsManager getInstance() {

        if (methodsManager == null) {
            methodsManager = new MethodsManager();
        }
        return methodsManager;
    }

    // Playlist
    public void createPlaylistOnSpotify(Activity activity, String playlistName,
                                        ListenerManager.OnCreatePlaylistCompleteListener listener) {

        // Tạo yêu cầu tạo playlist mới
        Map<String, Object> options = new HashMap<>();
        options.put("name", playlistName);
        options.put("public", true);
        // Get user information
        SharedPreferences sharedPreferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        spotifyService.createPlaylist(USER_ID, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                // Lấy ID của playlist mới được tạo
                String playlistID = playlist.id;
                Log.d(activity + "", "Create playlist success");
                // Load
                getUserPlaylists(true);
                listener.onComplete(playlist);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(activity + "", "Error creating playlist on Spotify: " + error.getMessage());
                listener.onError(error);
            }
        });
    }

    public void getUserPlaylists(Boolean permission) {
        List<PlaylistSimple> playlistsList = ListManager.getInstance().getPlaylistList();
        if (playlistsList.isEmpty() || permission == true) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 20);
            spotifyService.getMyPlaylists(options, new SpotifyCallback<Pager<PlaylistSimple>>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    // Log.e(TAG, "failure: " + spotifyError.getMessage());
                }

                @Override
                public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
                    // Log.d(TAG, "Get playlist success: ");
                    List<PlaylistSimple> mList = playlistSimplePager.items;
                    // Lưu giá trị về bộ nhớ
                    ListManager.getInstance().setPlaylistList(mList);
                }
            });
        }
    }

    public void getPlayListTrack(String playlistId, Context context,
                                 ListenerManager.ListTrackOnCompleteListener listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        if (userId == "") {
            Toast.makeText(context, "Not found userId", Toast.LENGTH_LONG).show();
        }
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 20);
        spotifyService.getPlaylistTracks(userId, playlistId, options, new SpotifyCallback<Pager<PlaylistTrack>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }

            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                List<PlaylistTrack> mList = playlistTrackPager.items;
                List<Track> playlistTrack = new ArrayList<>();
                for (PlaylistTrack item : mList) {
                    playlistTrack.add(item.track);
                }
                listener.onComplete(playlistTrack);
            }
        });
    }

    public void showAddToPlaylist(Activity activity, Track detailTrack) {

        RecyclerView recyclerView;
        Button btn_add_to_playlist;
        Button btn_add_song;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_playlist, null);

        builder.setView(addView);

        // Tạo adapter và gán cho RecyclerView
        recyclerView = addView.findViewById(R.id.playlist_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        btn_add_to_playlist = addView.findViewById(R.id.buttonAddPlaylist);
        btn_add_song = addView.findViewById(R.id.buttonAddSong);

        AlertDialog alertDialog = builder.create();

        // Load user's playlists
        List<PlaylistSimple> playlistList = ListManager.getInstance().getPlaylistList();
        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(new ArrayList<>(), null, playlistList, activity,
                null);
        adapter.setSend(true);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        // Create playlist
        btn_add_to_playlist.setOnClickListener(v -> {
            Log.e("add playlist", "click");

        });

        alertDialog.show();

        btn_add_song.setOnClickListener(v -> {
            if (selectedPlaylistId != null) { // Kiểm tra xem đã chọn playlist chưa
                Log.d(activity + "", "Success!!!!");
                addTrackToPlaylist(selectedPlaylistId, detailTrack, activity);
                Toast.makeText(activity, "Add song playlist successful!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(activity + "", "fail");
                // Thông báo cho người dùng rằng họ cần chọn một playlist trước khi thêm bài hát
                Toast.makeText(activity, "Please select a playlist first", Toast.LENGTH_SHORT).show();
            }
            EventBus.getDefault().unregister(this);
            alertDialog.dismiss();
        });
    }

    public void showRemoveDialog(String playlistId, String trackUri, Fragment fragment,
                                 ListenerManager.OnGetCompleteListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa bài hát này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Gọi phương thức để xóa bài hát
            removeTrackFromPlaylist(playlistId, trackUri, fragment);
            listener.onComplete(true);
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    // public void getUserFavorite(boolean type) {
    // List<Track> favorite = ListManager.getInstance().getFavoriteTracks();
    // if (favorite.isEmpty() || type == true) {
    // Map<String, Object> options = new HashMap<>();
    // options.put(SpotifyService.LIMIT, 20);
    // spotifyService.getMySavedTracks(options, new
    // SpotifyCallback<Pager<SavedTrack>>() {
    // @Override
    // public void failure(SpotifyError spotifyError) {
    //// Log.e(TAG, "failure: " + spotifyError.getMessage());
    // }
    //
    // @Override
    // public void success(Pager<SavedTrack> savedTrackPager, Response response) {
    // List<Track> trackList = new ArrayList<>();
    // for (SavedTrack savedTrack : savedTrackPager.items) {
    // trackList.add(savedTrack.track);
    // }
    // Log.e("xxx", "success: " + trackList.size() + "");
    // // Lưu giá trị về bộ nhớ
    // ListManager.getInstance().setFavoriteTracks(trackList);
    // }
    // });
    // }
    // }

    // MaiThy - Update getUserFavorite xử lý gọi callback khi hoàn thành
    public interface OnFavoriteTracksLoadedListener {
        void onFavoriteTracksLoaded(List<Track> trackList);
    }

    public void getUserFavorite(boolean type, final OnFavoriteTracksLoadedListener callback) {
        List<Track> favorite = ListManager.getInstance().getFavoriteTracks();
        if (favorite.isEmpty() || type == true) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 20);
            spotifyService.getMySavedTracks(options, new SpotifyCallback<Pager<SavedTrack>>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    // Xử lý lỗi
                }

                @Override
                public void success(Pager<SavedTrack> savedTrackPager, Response response) {
                    List<Track> trackList = new ArrayList<>();
                    for (SavedTrack savedTrack : savedTrackPager.items) {
                        trackList.add(savedTrack.track);
                    }
                    ListManager.getInstance().setFavoriteTracks(trackList);
                    // Gọi callback khi hoàn thành
                    if (callback != null) {
                        callback.onFavoriteTracksLoaded(trackList);
                    }
                }
            });
        } else {
            // Sử dụng danh sách yêu thích đã lưu nếu có
            if (callback != null) {
                callback.onFavoriteTracksLoaded(favorite);
            }
        }
    }

    public void checkContains(String trackID, ListenerManager.OnGetCompleteListener listener) {

        // Check if user has follow this song
        spotifyService.containsMySavedTracks(trackID, new SpotifyCallback<boolean[]>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }

            @Override
            public void success(boolean[] booleans, Response response) {
                // Check if user has follow this song
                if (booleans[0] == false) {
                    listener.onComplete(true);
                } else {
                    listener.onComplete(false);
                }
            }
        });
    }

    public void addToFavorite(String trackID, ListenerManager.OnGetCompleteListener listener) {
        spotifyService.addToMySavedTracks(trackID, new SpotifyCallback<Object>() {
            @Override
            public void success(Object o, Response response) {
                listener.onComplete(true);
            }

            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }
        });
    }

    public void removeFromFavorite(String trackID, ListenerManager.OnGetCompleteListener listener) {
        spotifyService.removeFromMySavedTracks(trackID, new SpotifyCallback<Object>() {
            @Override
            public void success(Object o, Response response) {
                listener.onComplete(true);
            }

            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }
        });
    }

    private void removeTrackFromPlaylist(String playlistId, String trackUri, Fragment fragment) {
        // Tạo một instance của TracksToRemove và thêm các TrackToRemove vào danh sách
        TracksToRemove tracksToRemove = new TracksToRemove();
        tracksToRemove.tracks = new ArrayList<>();

        // Tạo một TrackToRemove và thêm trackId vào URI
        TrackToRemove trackToRemove = new TrackToRemove();
        trackToRemove.uri = trackUri;
        tracksToRemove.tracks.add(trackToRemove);

        SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("UserData",
                Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");

        // Gọi service để xóa bài hát khỏi playlist
        spotifyService.removeTracksFromPlaylist(USER_ID, playlistId, tracksToRemove, new Callback<SnapshotId>() {
            @Override
            public void success(SnapshotId snapshotId, Response response) {
                Log.d(fragment.getTag(), "Remove track from playlist success");
                Toast.makeText(fragment.getContext(), "Đã xóa track thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(fragment.getTag(), "Remove track from playlist failed: " + error.getMessage());
                Toast.makeText(fragment.getContext(), "Xóa track thất bại: " + error.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void addTrackToPlaylist(String selectedPlaylistId, Track mTrack, Activity activity) {

        Map<String, Object> options = new HashMap<>();
        Map<String, Object> bodyParams = new HashMap<>();
        List<String> uris = new ArrayList<>();
        uris.add(mTrack.uri); // Thêm URI của bài hát vào danh sách
        bodyParams.put("uris", uris);
        // bodyParams.put("position", ListManager.getInstance().getPlaylistList().size()
        // + 1);

        SharedPreferences sharedPreferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        // Gọi API để thêm bài hát vào playlist
        spotifyService.addTracksToPlaylist(USER_ID, selectedPlaylistId, options, bodyParams,
                new SpotifyCallback<Pager<PlaylistTrack>>() {
                    @Override
                    public void failure(SpotifyError spotifyError) {
                        Log.e(activity + "", "Error creating song playlist on Spotify: " + spotifyError.getMessage());
                    }

                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        Log.d(activity + "", "Create song playlist success");

                    }
                });
    }

    // Album
    public void getAlbum(String albumId, ListenerManager.AlbumCompleteListener listener) {
        spotifyService.getAlbum(albumId, new SpotifyCallback<Album>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }

            @Override
            public void success(Album album, Response response) {
                Album result = album;
                getAlbumTracks(albumId, new ListenerManager.ListTrackOnCompleteListener() {
                    @Override
                    public void onComplete(List<Track> trackList) {
                        listener.onComplete(result, trackList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        listener.onError(error);
                    }
                });
            }
        });
    }

    public void getAlbumTracks(String id, ListenerManager.ListTrackOnCompleteListener listener) {
        spotifyService.getAlbumTracks(id, new SpotifyCallback<Pager<Track>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }

            @Override
            public void success(Pager<Track> trackPager, Response response) {
                if (trackPager != null && trackPager.items != null) {
                    List<Track> mList = trackPager.items;
                    listener.onComplete(mList);
                }
            }
        });
    }

    public void getArtistTopTrack(String artistId, String countryCode,
                                  ListenerManager.ListTrackOnCompleteListener listener) {
        spotifyService.getArtistTopTrack(artistId, countryCode, new SpotifyCallback<Tracks>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                listener.onError(spotifyError);
            }

            @Override
            public void success(Tracks tracks, Response response) {
                List<Track> trackList = tracks.tracks;
                listener.onComplete(trackList);
            }
        });
    }

}
