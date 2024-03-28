package com.example.music_mobile_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.music_mobile_app.model.User;
import com.example.music_mobile_app.model.UserImage;
import com.example.music_mobile_app.network.mSpotifyService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends FragmentActivity {

    private static final String TAG = "EditAccountActivity";
    ShapeableImageView imageViewAvt;
    ImageButton imageButtonAvt;
    EditText editTextName;
    Button buttonSave;
    Uri uri = null;
    String authToken;

    private mSpotifyService spotifyService = MainActivity.mSpotifyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitvity_edit_account);


//        // Căn giữa tiêu đề
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        android.view.View customView = getLayoutInflater().inflate(R.layout.custom_actionbar_title, null);
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                ActionBar.LayoutParams.MATCH_PARENT,
//                Gravity.CENTER);
//        getSupportActionBar().setCustomView(customView, params);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)));
//        getSupportActionBar().setTitle("Chỉnh sửa hồ sơ");

        //retrieve a reference to a View with the ID
        imageViewAvt = findViewById(R.id.imageAvt);
        imageButtonAvt = findViewById(R.id.buttonEditAvt);
        editTextName = findViewById(R.id.editTextName);
        buttonSave = findViewById(R.id.buttonSave);

        //handle change avatar
        imageButtonAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(EditAccountActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        //get authToken
        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("AUTH_TOKEN", "");


        //get Name and avatar
        SharedPreferences sharedPreferencesUserData = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String displayName = sharedPreferencesUserData.getString("displayName", "");
        String imageUrl = sharedPreferencesUserData.getString("imageUrl", "");

        //Set name and avatar
        editTextName.setText(displayName);
        Picasso.get().load(imageUrl).into(imageViewAvt);

        //handle Save
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileOnSpotify(sharedPreferencesUserData, displayName);
            }
        });


    }

    // Xử lý sự kiện khi nút quay lại được nhấn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        imageViewAvt.setImageURI(uri);

    }

    private void updateProfileOnSpotify(SharedPreferences sharedPreferences, String displayName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!displayName.equals(editTextName.getText().toString())) {
            editor.putString("displayName", editTextName.getText().toString());
        }
        if (uri != null) {
            editor.putString("imageUrl", uri.toString());
        }

        // Kiểm tra xem có URL hình ảnh mới không
        if (!displayName.equals(editTextName.getText().toString()) || uri != null) {

            // Nếu có thay đổi, cập nhật vào SharedPreferences
            editor.apply();

            // Tạo một đối tượng User mới với thông tin cập nhật
            User updatedUser = new User();
            updatedUser.setDisplayName(editTextName.getText().toString());
            if (uri != null) {
                UserImage userImage = new UserImage(uri.toString(), 0, 0); // Không có thông tin kích thước, có thể cập nhật sau
                updatedUser.getImages().add(0, userImage);
            }

            // Gọi phương thức updateProfile của SpotifyService
            spotifyService.updateUserProfile(updatedUser, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    System.out.println(authToken);
                    if (response.isSuccessful()) {
                        Log.d(TAG, "User profile updated successfully on Spotify");
                        // Cập nhật thành công, có thể thực hiện các thao tác khác nếu cần
                    } else {
                        Log.e(TAG, "Failed to update user profile on Spotify: " + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG, "Failed to update user profile on Spotify", t);
                }
            });
        } else {
            Log.e(TAG, "Image URL is null. Cannot update profile on Spotify.");
        }
    }


}
