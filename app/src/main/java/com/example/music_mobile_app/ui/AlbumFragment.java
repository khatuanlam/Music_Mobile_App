package com.example.music_mobile_app.ui;

import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;

public class AlbumFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private ImageView albumImage;
    private ImageButton btnBack, btnPlay;
    private FrameLayout frameLayout;
    private Drawable backgroundDrawable;
    private RecyclerView recyclerView;
    private TextView albumArtist, albumName, albumYear;
    private AlbumSimple albumDetail;
    private Album mAlbum;
    private FragmentManager manager;
    private static List<Track> trackList = new ArrayList<>();
    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        // Hide header
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        header.setVisibility(View.GONE);

        prepareData(view);

        initView();

        // Get detail album
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumDetail = (AlbumSimple) bundle.getParcelable("AlbumDetail");
            mAlbum = (Album) bundle.getParcelable("Album");
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            List<Track> trackList = new ArrayList<>();
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof Track) {
                    trackList.add((Track) parcelable);
                }
            }
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, mAlbum, new ArrayList<>(),
                    getContext(), getParentFragment());
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "Cannot get album detail");
        }

        albumName.setText(mAlbum.name);
        albumYear.setText(mAlbum.release_date);
        albumArtist.setText(mAlbum.artists.get(0).name);
        Glide.with(this)
                .load((albumDetail.images.get(0).url != null) ? albumDetail.images.get(0).url : baseImage)
                .override(Target.SIZE_ORIGINAL)
                .into(new ImageViewTarget<Drawable>(albumImage) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        // Khi quá trình tải ảnh hoàn thành, resource sẽ chứa Drawable
                        if (resource != null) {
                            // setImageDrawable cho artistImage
                            albumImage.setImageDrawable(resource);

                            // Xử lý background => Đổi màu theo ảnh của artist
                            HandleBackground backgroundHandler = new HandleBackground();
                            backgroundHandler.handleBackground(albumImage, backgroundDrawable,
                                    new HandleBackground.OnPaletteGeneratedListener() {
                                        @Override
                                        public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                                            // Set the updated Drawable as the background of your view
                                            frameLayout.setBackground(updatedDrawable);
                                        }
                                    });
                        } else {
                            // Xử lý khi không thể tải được Drawable
                        }
                    }
                });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void prepareData(View view) {
        albumImage = view.findViewById(R.id.albumImage);
        albumArtist = view.findViewById(R.id.albumArtist);
        albumYear = view.findViewById(R.id.albumYear);
        albumName = view.findViewById(R.id.albumName);
        btnBack = view.findViewById(R.id.backButton);
        btnPlay = view.findViewById(R.id.btn_play_album);

        frameLayout = view.findViewById(R.id.album_fragment_container);
        backgroundDrawable = frameLayout.getBackground();
        recyclerView = view.findViewById(R.id.album_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initView() {
        // Get detail album
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumDetail = (AlbumSimple) bundle.getParcelable("AlbumDetail");
            mAlbum = (Album) bundle.getParcelable("Album");
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof Track) {
                    trackList.add((Track) parcelable);
                }
            }
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, mAlbum, new ArrayList<>(),
                    getContext(), getParentFragment());
            recyclerView.setAdapter(adapter);
        } else {
            Log.e(TAG, "Cannot get album detail");
        }

        albumName.setText(mAlbum.name);
        albumYear.setText(mAlbum.release_date);
        albumArtist.setText(mAlbum.artists.get(0).name);
        Glide.with(this)
                .load((albumDetail.images.get(0).url != null) ? albumDetail.images.get(0).url : baseImage)
                .override(Target.SIZE_ORIGINAL).into(albumImage);


        // Onclick back
        btnBack.setOnClickListener(v -> {
            manager.popBackStack();
        });

        // Play Album
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PlayTrackActivity.class);
            intent.putExtra("Album", mAlbum);
            intent.putExtra("Track's Album", albumDetail);
            intent.putParcelableArrayListExtra("ListTrack", (ArrayList<Track>) trackList);
            intent.setAction("Play Album");
            this.startActivity(intent);
        });

    }

}