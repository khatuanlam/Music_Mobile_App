package com.example.music_mobile_app.manager;

import android.content.Context;
import android.util.Log;

import com.example.music_mobile_app.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;


public class MethodsManager {

    private static final String TAG = MethodsManager.class.getSimpleName();
    private static MethodsManager methodsManager;

    private SpotifyService spotifyService = MainActivity.spotifyService;

    public static MethodsManager getInstance(Context context) {
        if (methodsManager == null) {
            methodsManager = new MethodsManager();
        }
        return methodsManager;
    }

    public interface onCompleteListener {
        void onComplete();
//        List<Track> items

        void onError(Throwable error);
    }


    public void getRecentlyTracks(int limit, final onCompleteListener listener) {

    }

    public ArrayList<Track> getRecommendTracks(final onCompleteListener listener) {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 10);
        ArrayList<Track> result = new ArrayList<Track>();

        spotifyService.getRecommendations(options, new SpotifyCallback<Recommendations>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.e(TAG, "failure: " + spotifyError.getErrorDetails());
            }

            @Override
            public void success(Recommendations recommendations, Response response) {
                List<Track> mList = recommendations.tracks;
                result.addAll(mList);

                if (listener != null) {
                    listener.onComplete();
                } else {
                    Log.d(TAG, "Bad listener");
                }
            }
        });
        return result;
    }
}
