package com.example.music_mobile_app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.music_mobile_app.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

public class MethodsManager {
    private static MethodsManager methodsManager;
    private SpotifyService spotifyService = MainActivity.spotifyService;

    public static MethodsManager getInstance() {
        if (methodsManager == null) {
            methodsManager = new MethodsManager();
        }
        return methodsManager;
    }

    //    private void addTrack(String userId, String playlistId, String trackId) {
//        // Tạo tham số cho yêu cầu POST
//        Map<String, Object> queryParams = new HashMap<>();
//
//        Map<String, Object> bodyParams = new HashMap<>();
//        List<String> uris = new ArrayList<>();
//        uris.add("spotify:track:" + trackId); // Thêm URI của bài hát vào danh sách
//        bodyParams.put("uris", uris);
//
//        // Gọi API để thêm bài hát vào playlist
//        spotifyService.addTracksToPlaylist(USER_ID, playlistId, queryParams, bodyParams, new Callback<Pager<PlaylistTrack>>() {
//            @Override
//            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
//                // Xử lý kết quả thành công
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                // Xử lý khi gặp lỗi
//                Log.e("AddSongPlaylistActivity", "Error adding track to playlist: " + error.getMessage());
//                Toast.makeText(AddSongPlaylistActivity.this, "Failed to add track to playlist", Toast.LENGTH_SHORT).show();
//            }
//        });
////    }
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

    public void getPlayListTrack(String playlistId, Context context, ListenerManager.ListTrackOnCompleteListener listener) {
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

    public MethodsManager() {

    }
}
