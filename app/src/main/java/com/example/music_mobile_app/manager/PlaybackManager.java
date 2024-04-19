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
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.ErrorCallback;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Empty;
import com.spotify.protocol.types.PlayerState;

import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyService;


public class PlaybackManager {
    private static PlaybackManager playbackManager;
    private final String TAG = this.getClass().getSimpleName();
    private static SpotifyAppRemote mSpotifyAppRemote;
    private Context context;
    private final ErrorCallback mErrorCallback = this::logError;

    public interface SpotifyAppRemoteConnectorListener {
        void onSpotifyAppRemoteConnected();
    }

    public static PlaybackManager getInstance(Context context, Subscription.EventCallback<PlayerState> mPlayerStateCallback, boolean reload) {
        if (playbackManager == null || reload == true) {
            playbackManager = new PlaybackManager(context, mPlayerStateCallback);
        } else {
            playbackManager.setPlayerState(mPlayerStateCallback);
        }
        return playbackManager;
    }

    public void connect(Subscription.EventCallback<PlayerState> mPlayerStateCallback) {
        playbackManager = new PlaybackManager(context, mPlayerStateCallback);
    }

    public PlaybackManager(Context context, Subscription.EventCallback<PlayerState> mPlayerStateCallback) {
        this.context = context;
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);

        // Connect to Spotify Player
        ConnectionParams connectionParams = new ConnectionParams.Builder(AuthLoginActivity.CLIENT_ID).setRedirectUri(AuthLoginActivity.REDIRECT_URI).showAuthView(true).build();

        SpotifyAppRemote.connect(context, connectionParams, new Connector.ConnectionListener() {
            @Override
            public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                mSpotifyAppRemote = spotifyAppRemote;
                Log.d(TAG, "Player Connected");
                // Now you can start interacting with App Remote
                setPlayerState(mPlayerStateCallback);

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("MainActivity", throwable.getMessage(), throwable);
                // Something went wrong when attempting to connect! Handle errors here
            }
        });
    }

    public void setPlayerState(Subscription.EventCallback<PlayerState> mPlayerStateCallback) {
        mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(mPlayerStateCallback);
    }

    public void play(String uri, ListenerManager.OnGetCompleteListener listener) {
        mSpotifyAppRemote.getPlayerApi().play(uri).setResultCallback(empty -> {
            listener.onComplete(true);
        }).setErrorCallback(this::logError);
    }

    public void onSeekForward() {
        mSpotifyAppRemote.getPlayerApi().seekToRelativePosition(15000).setResultCallback(data -> logMessage("seek fwd", 10)).setErrorCallback(mErrorCallback);
    }

    public void addToQueue(String trackURI) {
        mSpotifyAppRemote.getPlayerApi().queue(trackURI).setResultCallback(new CallResult.ResultCallback<Empty>() {
            @Override
            public void onResult(Empty empty) {
                Log.d(TAG, "Add to queue success");
            }
        });
    }

    public void onSeekBack() {
        mSpotifyAppRemote.getPlayerApi().seekToRelativePosition(-15000).setResultCallback(data -> logMessage("seek back", 10)).setErrorCallback(mErrorCallback);
    }

    public void onSkipNextButtonClicked() {
        mSpotifyAppRemote.getPlayerApi().skipNext().setResultCallback(data -> logMessage("skip next", 10)).setErrorCallback(mErrorCallback);
    }

    public void onSkipPreviousButtonClicked() {
        mSpotifyAppRemote.getPlayerApi().skipPrevious().setResultCallback(data -> logMessage("skip previous", 10)).setErrorCallback(mErrorCallback);
    }


    public void onPlayPauseButtonClicked() {
        mSpotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(playerState -> {
            if (playerState.isPaused) {
                mSpotifyAppRemote.getPlayerApi().resume().setResultCallback(empty -> logMessage("play", 10)).setErrorCallback(mErrorCallback);
            } else {
                mSpotifyAppRemote.getPlayerApi().pause().setResultCallback(empty -> logMessage("pause", 10)).setErrorCallback(mErrorCallback);
            }
        });
    }

    public void onToggleShuffleButtonClicked() {
        mSpotifyAppRemote.getPlayerApi().toggleShuffle().setResultCallback(empty -> logMessage("toggle shuffle", 10)).setErrorCallback(mErrorCallback);
    }

    public void Disconnect() {
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    public void onRepeatMode() {

    }

    public PlayerApi getPlayerApi() {
        return mSpotifyAppRemote.getPlayerApi();
    }


    private void logMessage(String msg, int duration) {
        Log.d(TAG, msg);
    }

    private void logError(Throwable throwable) {
        Log.e(TAG, "", throwable);
    }

}
