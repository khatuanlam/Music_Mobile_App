package com.example.music_mobile_app.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ArtistFragment extends Fragment implements ListenerManager.OnFollowCompleteListener {

    private final String TAG = this.getClass().getSimpleName();
    private LinearLayout content_container;
    private Drawable backgroundDrawable;
    private TextView artistName;
    private TextView listeners;
    private ImageView artistImage;
    private Button playMusic, btnFollow;
    private RecyclerView recyclerView;
    private Artist mArtist;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private String baseImage = VariableManager.getVariableManager().baseImage;
    private ImageButton backButton;
    private FrameLayout fragment_container;
    private View headerView;
    private boolean isFollowing = false;
    private FragmentManager manager;

    public ArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist, container, false);

        //Hide header
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        header.setVisibility(View.GONE);

        prepareData(view);

        initView();

        // Onclick back
        backButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        if (btnFollow != null) {
            initializeFollowButtonState(mArtist.id);
            btnFollow.setOnClickListener(v -> {
                if (isFollowing == false) {
                    // Nếu chưa theo dõi, thực hiện hành động theo dõi
                    followArtist(mArtist.id);
                    setFollowButtonState(btnFollow, true);
                } else {
                    // Nếu đang theo dõi, thực hiện hành động bỏ theo dõi
                    unfollowArtists(mArtist.id);
                    // Cập nhật trạng thái và văn bản của nút
                    setFollowButtonState(btnFollow, false);
                }
            });
            // Reload follow artist
            ListManager.getInstance().setFollowArtists(null);
        } else {
            // Nếu btnFollow là null, hiển thị thông báo lỗi
            Log.e(TAG, "Button btnFollow is null");
        }




        return view;
    }


    private void prepareData(View view) {
        artistName = view.findViewById(R.id.textArtistName);
        artistImage = view.findViewById(R.id.artistAvatar);
        listeners = view.findViewById(R.id.followerNumber);
//        overflowMenu = view.findViewById(R.id.overflowArtistButton);
        playMusic = view.findViewById(R.id.playArtistButton);
        recyclerView = view.findViewById(R.id.recyclerArtistMusicView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listeners = view.findViewById(R.id.followerNumber);
        btnFollow = view.findViewById(R.id.btnFollow);
        backButton = view.findViewById(R.id.backButton);


        fragment_container = view.findViewById(R.id.fragment_container);

        //Update: Bổ sung xử lý background thay đổi theo hình của artist
        //get background framelayout
        content_container = view.findViewById(R.id.content_container);
        backgroundDrawable = content_container.getBackground();

    }

    private void initView() {
        // Get detail artist
        Bundle bundle = getArguments();
        if (bundle != null) {
            mArtist = (Artist) bundle.getParcelable("ArtistDetail");
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            List<Track> trackList = new ArrayList<>();
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof Track) {
                    trackList.add((Track) parcelable);
                }
            }

            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(), getParentFragment());
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

            artistName.setText(mArtist.name);
            listeners.setText(mArtist.followers.total + " người nghe hàng tháng");
            Glide.with(this).load((mArtist.images == null) ? baseImage : mArtist.images.get(0).url)
                    .override(Target.SIZE_ORIGINAL)
                    .into(new ImageViewTarget<Drawable>(artistImage) {
                        @Override
                        protected void setResource(@Nullable Drawable resource) {
                            // Khi quá trình tải ảnh hoàn thành, resource sẽ chứa Drawable
                            if (resource != null) {
                                // setImageDrawable cho artistImage
                                artistImage.setImageDrawable(resource);

                                // Xử lý background => Đổi màu theo ảnh của artist
                                HandleBackground backgroundHandler = new HandleBackground();
                                backgroundHandler.handleBackground(artistImage, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
                                    @Override
                                    public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                                        // Set the updated Drawable as the background of your view
                                        content_container.setBackground(updatedDrawable);
                                    }
                                });
                            } else {
                                // Xử lý khi không thể tải được Drawable
                            }
                        }
                    });

        } else {
            Log.e(TAG, "Cannot get album detail");
        }

    }

    @Override
    public void setFollowButtonState(Button button, boolean status) {
        isFollowing = status;
        if (isFollowing) {
            button.setText("Bỏ theo dõi");
        } else {
            button.setText("Theo dõi");
        }
    }

    @Override
    public void initializeFollowButtonState(String artistId) {
        // Check status of following artists
        spotifyService.isFollowingArtists(artistId, new SpotifyCallback<boolean[]>() {
            @Override
            public void failure(SpotifyError spotifyError) {
            }

            @Override
            public void success(boolean[] booleans, Response response) {
                // Followed
                if (booleans[0] == true) {
                    setFollowButtonState(btnFollow, true);
                } else {
                    // Not Followed
                    setFollowButtonState(btnFollow, false);
                }
            }
        });
    }

    @Override
    public void followArtist(String artistId) {
        spotifyService.followArtists(artistId, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {
                Log.d(TAG, "Follow artist success");
                // Cập nhật trạng thái và văn bản của nút
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error following artist on Spotify: " + error.getMessage());
            }
        });
    }

    @Override
    public void unfollowArtists(String artistId) {
        Log.e(TAG, "artistId : " + artistId);
        spotifyService.unfollowArtists(artistId, new Callback<Object>() {
            @Override
            public void success(Object object, retrofit.client.Response response) {
                Log.d(TAG, "UnFollow artist success");
                // Cập nhật trạng thái và văn bản của nút
                setFollowButtonState(btnFollow, false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error following artist on Spotify: " + error.getMessage());
            }
        });
    }
}