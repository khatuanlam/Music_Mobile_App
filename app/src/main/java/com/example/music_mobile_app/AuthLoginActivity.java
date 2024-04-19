package com.example.music_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class AuthLoginActivity extends FragmentActivity {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String CLIENT_ID = "95fe47fe6b524ab2ba54354da461321a";

    @SuppressWarnings("SpellCheckingInspection")
    public static final String REDIRECT_URI = "http://localhost:8888/callback";

    private static final String TAG = "Spotify " + AuthLoginActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 1337;

    public static final String AUTH_TOKEN = "AUTH_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Button mLoginButton = (Button) findViewById(R.id.buttonStart);
        mLoginButton.setOnClickListener(mListener);

    }

    private void openLoginWindow() {

        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID,
                AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming",
                "user-read-private", "user-top-read", "user-read-recently-played",
                "playlist-modify-public", "playlist-read-private", " playlist-modify-private",
                "user-library-read", "user-read-playback-state", "user-modify-playback-state", "user-follow-modify",
                "user-read-currently-playing", "user-follow-read", "user-library-modify"});

        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    String accessToken = response.getAccessToken();
                    // set auth_token, connectionParams
                    newIntent(accessToken);
                    break;

                case ERROR:
                    Log.e(TAG, "Auth error: " + response.getError());
                    break;

                // Most likely auth flow was cancelled
                default:
                    Log.d(TAG, "Auth result: " + response.getType());
            }
        }
    }

    View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonStart:
                    openLoginWindow();
            }
        }
    };

    public void destroy() {
        AuthLoginActivity.this.finish();
    }

    public void newIntent(String accessToken) {
        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, accessToken);
        editor.apply();
        editor.commit();
        Intent intent = new Intent(AuthLoginActivity.this,
                MainActivity.class);
        startActivity(intent);
    }
}