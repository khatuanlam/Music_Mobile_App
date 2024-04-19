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

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.client.Response;

public class AlbumFragment extends Fragment {

    private SpotifyService spotifyService = MainActivity.spotifyService;
    private final String TAG = this.getClass().getSimpleName();
    private ImageView albumImage, btnBack;
    private FrameLayout frameLayout;
    private Drawable backgroundDrawable;
    private RecyclerView recyclerView;
    private TextView albumArtist, albumName, albumYear;
    private static AlbumSimple albumDetail;
    private static Album mAlbum;
    private FragmentManager manager;
    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    // private static ListManager listManager = MainActivity.listManager;
    private static VariableManager varManager = MainActivity.varManager;

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
        RelativeLayout header = getParentFragment().getView().findViewById(R.id.header);
        header.setVisibility(View.GONE);

        prepareData(view);

        // Onclick back
        btnBack.setOnClickListener(v -> {
            manager.popBackStack();
        });

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
                .override(Target.SIZE_ORIGINAL).into(albumImage);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // // Create an instance of HandleBackground and call handleBackground method
        // HandleBackground backgroundHandler = new HandleBackground();
        // backgroundHandler.handleBackground(albumImage, backgroundDrawable, new
        // HandleBackground.OnPaletteGeneratedListener() {
        // @Override
        // public void onPaletteGenerated(GradientDrawable updatedDrawable) {
        // // Set the updated Drawable as the background of your view
        // frameLayout.setBackground(updatedDrawable);
        // }
        // });
    }

    private void prepareData(View view) {
        albumImage = view.findViewById(R.id.albumImage);
        albumArtist = view.findViewById(R.id.albumArtist);
        albumYear = view.findViewById(R.id.albumYear);
        albumName = view.findViewById(R.id.albumName);
        btnBack = view.findViewById(R.id.backButton);

        frameLayout = view.findViewById(R.id.album_fragment_container);
        backgroundDrawable = frameLayout.getBackground();
        recyclerView = view.findViewById(R.id.album_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

}