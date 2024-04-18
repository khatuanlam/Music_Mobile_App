package com.example.music_mobile_app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
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

    public void createPlaylistOnSpotify(Fragment fragment, String playlistName) {

        // Tạo yêu cầu tạo playlist mới
        Map<String, Object> options = new HashMap<>();
        options.put("name", playlistName);
        options.put("public", true);

        // Get user information
        SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");
        spotifyService.createPlaylist(USER_ID, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                // Lấy ID của playlist mới được tạo
                String playlistID = playlist.id;
                Log.d(fragment.getTag(), "Create playlist success");
                // Reload playlist
                getUserPlaylists(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(fragment.getTag(), "Error creating playlist on Spotify: " + error.getMessage());

            }
        });
    }

    public void getUserPlaylists(Boolean type) {
        List<PlaylistSimple> playlistsList = ListManager.getInstance().getPlaylistList();
        if (playlistsList.isEmpty() || type == true) {
            Map<String, Object> options = new HashMap<>();
            options.put(SpotifyService.LIMIT, 20);
            spotifyService.getMyPlaylists(options, new SpotifyCallback<Pager<PlaylistSimple>>() {
                @Override
                public void failure(SpotifyError spotifyError) {
//                    Log.e(TAG, "failure: " + spotifyError.getMessage());
                }

                @Override
                public void success(Pager<PlaylistSimple> playlistSimplePager, Response response) {
//                    Log.d(TAG, "Get playlist success: ");
                    List<PlaylistSimple> mList = playlistSimplePager.items;
                    // Lưu giá trị về bộ nhớ
                    ListManager.getInstance().setPlaylistList(mList);
                    getUserPlaylists(false);
                }
            });
        }
    }

    //    public void getUserFavorite(boolean type) {
//        List<Track> favorite = ListManager.getInstance().getFavoriteTracks();
//        if (favorite.isEmpty() || type == true) {
//            Map<String, Object> options = new HashMap<>();
//            options.put(SpotifyService.LIMIT, 20);
//            spotifyService.getMySavedTracks(options, new SpotifyCallback<Pager<SavedTrack>>() {
//                @Override
//                public void failure(SpotifyError spotifyError) {
////                    Log.e(TAG, "failure: " + spotifyError.getMessage());
//                }
//
//                @Override
//                public void success(Pager<SavedTrack> savedTrackPager, Response response) {
//                    List<Track> trackList = new ArrayList<>();
//                    for (SavedTrack savedTrack : savedTrackPager.items) {
//                        trackList.add(savedTrack.track);
//                    }
//                    Log.e("xxx", "success: " + trackList.size() + "");
//                    // Lưu giá trị về bộ nhớ
//                    ListManager.getInstance().setFavoriteTracks(trackList);
//                }
//            });
//        }
//    }

    //MaiThy - Update getUserFavorite xử lý gọi callback khi hoàn thành
    public interface OnFavoriteTracksLoadedListener {
        void onFavoriteTracksLoaded(List<Track> trackList);
    }

    public void getUserFavorite(boolean type, final OnFavoriteTracksLoadedListener callback) {
        List<Track> favorite = ListManager.getInstance().getFavoriteTracks();
        if (favorite.isEmpty() || type) {
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
                    // Lưu giá trị vào ListManager
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

    public void getArtistTopTrack(String artistId, String countryCode, ListenerManager.ListTrackOnCompleteListener listener) {
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
