package com.example.music_mobile_app.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.util.HandleBackground;
import com.example.music_mobile_app.network.mSpotifyService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Result;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private mSpotifyService mSpotifyService;
    private ImageView playlistImage, btnBack;
    private TextView playlistName, playlistOwner;
    public Button editPlaylist;
    private RecyclerView recyclerView;
    private FragmentManager manager;
    private PlaylistSimple playlistDetail;
    private FrameLayout fragment_container;
    private Drawable backgroundDrawable;
    private String baseImage = VariableManager.getVariableManager().baseImage;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private Bitmap capturedImage;
    private static final String BASE_URL = "https://api.spotify.com/v1/";

    public PlaylistFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get detail playlist
        Bundle bundle = getArguments();
        if (bundle != null) {
            playlistDetail = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
        } else {
            Log.e(TAG, "Cannot get playlist detail");
        }

        manager = getParentFragmentManager();

        // Khởi tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Tạo đối tượng mSpotifyService từ Retrofit
        mSpotifyService = retrofit.create(mSpotifyService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        // Hide header
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        header.setVisibility(View.GONE);

        preparedData(view);

        // Onclick back
        btnBack.setOnClickListener(v -> {
            manager.popBackStack();
        });

        initView();

        return view;

    }

    private void preparedData(View view) {

        playlistImage = view.findViewById(R.id.playlistImage);
        playlistName = view.findViewById(R.id.playlistName);
        playlistOwner = view.findViewById(R.id.playlistOwner);
        recyclerView = view.findViewById(R.id.playlist_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        btnBack = view.findViewById(R.id.backButton);
        editPlaylist = view.findViewById(R.id.btn_edit);
        editPlaylist.setOnClickListener(v -> {
            ShowDialogEdit();
        });
        playlistName.setText(playlistDetail.name);
        playlistOwner.setText(playlistDetail.owner.display_name);

        // get background framelayout
        fragment_container = view.findViewById(R.id.content_container);
        backgroundDrawable = fragment_container.getBackground();

        Glide.with(this).load((playlistDetail.images == null) ? baseImage : playlistDetail.images.get(0).url)
                .override(Target.SIZE_ORIGINAL)
                .into(new ImageViewTarget<Drawable>(playlistImage) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        // Khi quá trình tải ảnh hoàn thành, resource sẽ chứa Drawable
                        if (resource != null) {
                            // setImageDrawable cho artistImage
                            playlistImage.setImageDrawable(resource);

                            // Xử lý background => Đổi màu theo ảnh của artist
                            HandleBackground backgroundHandler = new HandleBackground();
                            backgroundHandler.handleBackground(playlistImage, backgroundDrawable,
                                    new HandleBackground.OnPaletteGeneratedListener() {
                                        @Override
                                        public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                                            // Set the updated Drawable as the background of your view
                                            fragment_container.setBackground(updatedDrawable);
                                        }
                                    });
                        } else {
                            // Xử lý khi không thể tải được Drawable
                        }
                    }
                });
    }

    private void initView() {
        setPlaylist();
    }

    public void ShowDialogEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View addView = inflater.inflate(R.layout.dialog_edit_playlist, null);

        builder.setView(addView);

        Button btnCaptureImage = addView.findViewById(R.id.btn_capture_image);
        Button btnChooseImage = addView.findViewById(R.id.btn_choose_image);
        Button btnChangeName = addView.findViewById(R.id.btn_change_name);

        AlertDialog alertDialog = builder.create();

        // Xử lý khi người dùng nhấn chụp ảnh
        btnCaptureImage.setOnClickListener(v -> {
            Log.e(TAG, "Click Chụp ảnh");

            // Thực hiện hành động khi người dùng nhấn nút "Chụp ảnh" mở camera để chụp ảnh
            // Tạo Intent để mở ứng dụng Camera
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Kiểm tra xem thiết bị có ứng dụng Camera không
            if (cameraIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                // Mở ứng dụng Camera
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                alertDialog.dismiss();
            } else {
                // Thông báo nếu không tìm thấy ứng dụng Camera
                Toast.makeText(requireContext(), "Không tìm thấy ứng dụng Camera trên thiết bị của bạn",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý khi người dùng nhấn chọn ảnh
        btnChooseImage.setOnClickListener(v -> {
            Log.e(TAG, "Click Chọn ảnh");
            // Thực hiện hành động khi người dùng nhấn nút "Chọn ảnh"
            // Mở thư viện ảnh để chọn ảnh
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        // Xử lý khi người dùng nhấn đổi tên
        btnChangeName.setOnClickListener(v -> {
            Log.e(TAG, "Click Đổi tên");
            // Hiển thị dialog để người dùng nhập tên mới cho playlist
            AlertDialog.Builder nameBuilder = new AlertDialog.Builder(getContext());
            nameBuilder.setTitle("Nhập tên mới cho playlist");

            // Tạo một EditText để người dùng nhập tên mới
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            nameBuilder.setView(input);

            // Xử lý khi người dùng nhấn "OK"
            nameBuilder.setPositiveButton("OK", (dialog, which) -> {
                // Lấy tên mới từ EditText
                String newName = input.getText().toString();
                // Gọi phương thức để cập nhật tên playlist
                changePlaylistName(newName);
            });

            // Xử lý khi người dùng nhấn "Cancel"
            nameBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            // Hiển thị dialog
            nameBuilder.show();
        });
        alertDialog.show();
    }

    private void changePlaylistName(String newPlaylistName) {
        // Lấy id của người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String USER_ID = sharedPreferences.getString("userId", "Not found UserId");

        // Tạo một Map để chứa dữ liệu mới của playlist
        Map<String, Object> playlistData = new HashMap<>();
        playlistData.put("name", newPlaylistName); // Đặt tên mới cho playlist

        // Gọi service để cập nhật thông tin playlist
        spotifyService.changePlaylistDetails(USER_ID, playlistDetail.id, playlistData, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                // Xử lý khi cập nhật thành công
                Log.d(TAG, "Đổi tên playlist thành công");
                Toast.makeText(getContext(), "Đã đổi tên playlist thành công", Toast.LENGTH_SHORT).show();
                playlistName.setText(newPlaylistName);
            }

            @Override
            public void failure(RetrofitError error) {
                // Xử lý khi gặp lỗi
                Log.e(TAG, "Đổi tên playlist thất bại: " + error.getMessage());
                Toast.makeText(getContext(), "Đổi tên playlist thất bại: " + error.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    // Trong phương thức onActivityResult()
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                // Xử lý khi nhận được ảnh từ camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Nhận ảnh dưới dạng Bitmap
                    capturedImage = (Bitmap) extras.get("data");
                    // Cập nhật ảnh lên Spotify
                    updatePlaylistImageOnSpotify();
                    Log.d(TAG, "onActivityResult: Called updatePlaylistImageOnSpotify from CAMERA_REQUEST_CODE");
                }
            } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                // Xử lý khi nhận được ảnh từ thư viện
                Uri selectedImage = data.getData();
                try {
                    // Nhận ảnh dưới dạng Bitmap từ Uri
                    capturedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(),
                            selectedImage);
                    // Cập nhật ảnh lên Spotify
                    updatePlaylistImageOnSpotify();
                    Log.d(TAG, "onActivityResult: Called updatePlaylistImageOnSpotify from GALLERY_REQUEST_CODE");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updatePlaylistImageOnSpotify() {
        // Kiểm tra nếu ảnh không null và mSpotifyService đã khởi tạo
        if (capturedImage != null && mSpotifyService != null) {
            // Chuyển đổi Bitmap thành Base64 string
            String base64Image = convertBitmapToBase64(capturedImage);
            if (base64Image != null) {
                // Tạo đối tượng RequestBody từ chuỗi base64Image
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), base64Image);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Authentication",
                        Context.MODE_PRIVATE);
                String authToken = sharedPreferences.getString("AUTH_TOKEN", "");
                // Gọi service để cập nhật ảnh lên Spotify
                Call<Void> call = mSpotifyService.uploadPlaylistImage("Bearer " + authToken, playlistDetail.id,
                        imageRequestBody);
                call.enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Thành công
                            Log.e(TAG, "Success!!!");
                            Toast.makeText(getContext(), "Đã cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                            updateImage(base64Image);
                            setPlaylist();
                        } else {
                            // Xử lý khi gặp lỗi
                            String errorMessage = "Cập nhật ảnh thất bại";
                            try {
                                errorMessage = "Cập nhật ảnh thất bại: " + response.errorBody().string();
                            } catch (IOException e) {
                                Log.e(TAG, "IOException when reading error body: " + e.getMessage());
                            }
                            Log.e(TAG, errorMessage);
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Xảy ra lỗi khi gửi yêu cầu
                        Log.e(TAG, "Error: " + t.getMessage());
                        Toast.makeText(getContext(), "Có lỗi xảy ra: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // In ra đường dẫn gửi yêu cầu cập nhật ảnh
                Log.d(TAG, "Update Playlist Image URL: " + call.request().url());
            }
        }
    }

    // Phương thức để chuyển đổi Bitmap thành Base64 string
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private void setPlaylist() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            PlaylistSimple playlistDetail = (PlaylistSimple) bundle.getParcelable("PlaylistDetail");
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            List<Track> trackList = new ArrayList<>();
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof Track) {
                    trackList.add((Track) parcelable);
                }
            }
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(),
                    this);
            adapter.setPlaylistTrack(playlistDetail);
            // Set send to detail
            adapter.setSend(false);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "Cannot get album detail");
        }
    }

    private void updateImage(String newImage) {
        byte[] decodedString = Base64.decode(newImage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Glide.with(this).load((decodedImage))
                .override(Target.SIZE_ORIGINAL)
                .into(new ImageViewTarget<Drawable>(playlistImage) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        // Khi quá trình tải ảnh hoàn thành, resource sẽ chứa Drawable
                        if (resource != null) {
                            // setImageDrawable cho artistImage
                            playlistImage.setImageDrawable(resource);

                            // Xử lý background => Đổi màu theo ảnh của artist
                            HandleBackground backgroundHandler = new HandleBackground();
                            backgroundHandler.handleBackground(playlistImage, backgroundDrawable,
                                    new HandleBackground.OnPaletteGeneratedListener() {
                                        @Override
                                        public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                                            // Set the updated Drawable as the background of your view
                                            fragment_container.setBackground(updatedDrawable);
                                        }
                                    });
                        } else {
                            // Xử lý khi không thể tải được Drawable
                        }
                    }
                });
    }

}
