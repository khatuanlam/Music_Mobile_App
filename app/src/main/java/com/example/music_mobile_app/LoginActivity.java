package com.example.music_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    @SuppressWarnings("SpellCheckingInspection")
    public static final String CLIENT_ID = "5de6930c8a744270851a5064c7ff6333";
    @SuppressWarnings("SpellCheckingInspection")
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    private static final String TAG = "Spotify " + LoginActivity.class.getSimpleName();

    private static final int REQUEST_CODE = 1337;

    public static final String AUTH_TOKEN = "AUTH_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_login);
        Button mLoginButton = (Button) findViewById(R.id.buttonStart);
        mLoginButton.setOnClickListener(mListener);


    }


    private void openLoginWindow() {

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "streaming", "user-top-read", "user-read-recently-played"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    String accessToken = response.getAccessToken();
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    intent.putExtra(AUTH_TOKEN, accessToken);
                    startActivity(intent);
                    destroy();

                    //set auth_token
                    SharedPreferences sharedPreferences = getSharedPreferences("AuthToken", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AUTH_TOKEN", accessToken);
                    editor.apply();
                    break;

                // Auth flow returned an error
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
                    break;
            }
        }
    };

    public void destroy() {
        LoginActivity.this.finish();
    }
}
