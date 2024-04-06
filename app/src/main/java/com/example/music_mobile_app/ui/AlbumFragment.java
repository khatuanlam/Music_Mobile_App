package com.example.music_mobile_app.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AlbumFragment extends Fragment {

    private SpotifyService spotifyService = MainActivity.spotifyService;
    private final String TAG = this.getClass().getSimpleName();
    private ImageView albumImage;
    private FrameLayout frameLayout;
    private Drawable backgroundDrawable;
    private RecyclerView recyclerView;
    private TextView albumArtist, albumName, albumYear;
    private Button add_to_fav;
    private static AlbumSimple albumDetail;
    private boolean isAddedToFavorites = false;



    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    //    private static ListManager listManager = MainActivity.listManager;
    private static VariableManager varManager = MainActivity.varManager;

    public AlbumFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get detail album
        Bundle bundle = getArguments();
        if (bundle != null) {
            albumDetail = (AlbumSimple) bundle.getParcelable("AlbumDetail");
        } else {
            Log.e(TAG, "Cannot get album detail");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        albumImage = view.findViewById(R.id.albumImage);
        albumArtist = view.findViewById(R.id.albumArtist);
        albumYear = view.findViewById(R.id.albumYear);
        albumName = view.findViewById(R.id.albumName);
        add_to_fav = view.findViewById(R.id.btn_add_to_fav);

        frameLayout = view.findViewById(R.id.fragment_container);
        backgroundDrawable = frameLayout.getBackground();
        recyclerView = view.findViewById(R.id.album_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getAlbum(albumDetail.id, new ListenerManager.AlbumCompleteListener() {
            @Override
            public void onComplete(Album album) {
                if (album.id != albumDetail.id) {
                    albumName.setText(album.name);
                    albumYear.setText(album.release_date);
                    albumArtist.setText(album.artists.get(0).name);
                    Glide.with(getActivity()).load(albumDetail.images.get(0).url).into(albumImage);
                }
                getAlbumTracks(album.id);
            }

            @Override
            public void onError(Throwable error) {
                Log.e(TAG, error.getMessage());
            }
        });

        add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddedToFavorites) {
                    setUnAdd_to_fav(albumDetail.id);
                    add_to_fav.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black)); // Đổi màu nền về đen
                    isAddedToFavorites = false; // Đánh dấu là chưa được thêm vào favorites
                } else {

                    setAdd_to_fav(albumDetail.id);
                    add_to_fav.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_light));
                    isAddedToFavorites = true; // Đánh dấu là đã được thêm vào favorites
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create an instance of HandleBackground and call handleBackground method
        HandleBackground backgroundHandler = new HandleBackground();
        backgroundHandler.handleBackground(albumImage, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
            @Override
            public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                // Set the updated Drawable as the background of your view
                frameLayout.setBackground(updatedDrawable);
            }
        });

    }

    private void getAlbum(String albumId, final ListenerManager.AlbumCompleteListener listener) {
        spotifyService.getAlbum(albumId, new SpotifyCallback<Album>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.e(TAG, "Cannot get this album");
                listener.onError(spotifyError);
            }

            @Override
            public void success(Album album, Response response) {
                // Get albumTracks
                Log.d(TAG, "Get this album success");
                varManager.setAlbum(album);
                listener.onComplete(album);
            }
        });
    }


    private void getAlbumTracks(String id) {
        spotifyService.getAlbumTracks(id, new SpotifyCallback<Pager<Track>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.e(TAG, "Cannot get this album tracks");
            }

            @Override
            public void success(Pager<Track> trackPager, Response response) {
                if (trackPager != null && trackPager.items != null) {
                    Log.d(TAG, "Get this album tracks success ");
                    List<Track> mList = trackPager.items;
                    ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(mList, new ArrayList<>(), getContext());
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e(TAG, "Album list is null");
                }

            }
        });
    }

    public void setAdd_to_fav(String id){
        spotifyService.addToMySavedAlbums(id, new Callback<Object>() {

            @Override
            public void success(Object o, Response response) {
                Log.d(TAG, "success: added to my list");
                Toast.makeText(getContext(), "add to myliked list", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage());

            }
        });
    }
    public void setUnAdd_to_fav(String id){
        spotifyService.removeFromMySavedAlbums(id, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d(TAG, "success: Unadded to myliked list");
                Toast.makeText(getContext(), "remove from my list", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, error.getMessage());

            }
        });
    }
}