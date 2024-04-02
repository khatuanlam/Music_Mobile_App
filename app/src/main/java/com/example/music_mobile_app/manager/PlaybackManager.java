package com.example.music_mobile_app.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.music_mobile_app.AuthLoginActivity;
import com.example.music_mobile_app.MainActivity;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.ErrorCallback;
import com.spotify.protocol.types.PlayerState;

import java.util.List;


public class PlaybackManager {
    private static PlaybackManager playbackManager;
    private final String TAG = this.getClass().getSimpleName();
    private static SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi playerApi;
    private Context context;
    private final ErrorCallback mErrorCallback = this::logError;

    public static PlaybackManager getInstance(Context context) {
        if (playbackManager == null) {
            playbackManager = new PlaybackManager(context);
        }
        return playbackManager;
    }

    public PlaybackManager(Context context) {
        this.context = context;

        // Connect to Spotify Player
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(AuthLoginActivity.CLIENT_ID)
                        .setRedirectUri(AuthLoginActivity.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d(TAG, "Player Connected");
                        // Now you can start interacting with App Remote
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    public void play(String uri) {
        mSpotifyAppRemote.getPlayerApi().play(uri)
                .setResultCallback(empty -> {
                }).setErrorCallback(this::logError);
    }



    public void resumeOrPausePlayer() {
        mSpotifyAppRemote.
                getPlayerApi()
                .getPlayerState().setResultCallback(playerState -> {
                    if (playerState.isPaused) {
                        mSpotifyAppRemote.getPlayerApi().resume()
                                .setResultCallback(msg -> {
                                });
                    } else {
                        mSpotifyAppRemote.getPlayerApi().pause()
                                .setResultCallback(msg -> {

                                });
                    }
                });
    }

    public void onRepeatMode() {

    }

    public PlayerApi getPlayerApi() {
        playerApi = mSpotifyAppRemote.getPlayerApi();
        return playerApi;
    }

    public void setPlayerApi(PlayerApi playerApi) {
        this.playerApi = playerApi;
    }

    private void logMessage(String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
        Log.d(TAG, msg);
    }

    private void logError(Throwable throwable) {
        Toast.makeText(context, "logError", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "", throwable);
    }
}
