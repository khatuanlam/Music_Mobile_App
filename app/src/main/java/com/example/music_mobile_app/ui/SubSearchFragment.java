package com.example.music_mobile_app.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SubSearchFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    public SubSearchFragment() {
    }

    private CircleImageView avt;
    private EditText editText;
    private ImageView imageView;
    private FragmentManager manager;
    private SpotifyService spotifyService = MainActivity.spotifyService;
    private SubSearchInformationFragment subSearchInformationFragment;
    private SubSearchRecyclerViewFoundSongFragment subSearchRecyclerViewFoundSongFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subSearchInformationFragment = new SubSearchInformationFragment();
        subSearchRecyclerViewFoundSongFragment = new SubSearchRecyclerViewFoundSongFragment();
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_search, container, false);
        editText = view.findViewById(R.id.search_subSearchTextBox);
        imageView = view.findViewById(R.id.search_subSearchBack);
        subSearchInformationFragment = new SubSearchInformationFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.search_subSearchMainFragmentContainer, subSearchInformationFragment)
                .commit();

        // Lấy tham chiếu đến MainFragment
        MainFragment mainFragment = (MainFragment) getParentFragment();

        // Kiểm tra mainFragment không null và có tham chiếu đến avt
        if (mainFragment != null) {
            avt = mainFragment.getView().findViewById(R.id.avt);
            avt.setVisibility(View.GONE);
        }

        //onclick back
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, searchFragment)
                        .commit();
                avt.setVisibility(View.VISIBLE);

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (editable.toString().length() == 0) {
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.search_subSearchMainFragmentContainer, subSearchInformationFragment)
                                    .commit();
                        } else {

                            List<Track> trackList = getTrack(editText.getText().toString());
                            List<Artist> artistList = getArtist(editText.getText().toString());
                            List<AlbumSimple> albumSimpleList = getAlbums(editText.toString());
                            subSearchRecyclerViewFoundSongFragment = new SubSearchRecyclerViewFoundSongFragment(trackList, artistList, albumSimpleList);
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.search_subSearchMainFragmentContainer, subSearchRecyclerViewFoundSongFragment)
                                    .commit();


                        }

                    }
                }, 1000);
            }
        });


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public List<Track> getTrack(String q) {

        spotifyService.searchTracks(q, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Log.i("GET DATA", "GET THANH CONG");
                Pager<Track> tracks = tracksPager.tracks;
                List<Track> trackList = tracks.items;
                if (subSearchRecyclerViewFoundSongFragment == null) {
                    subSearchRecyclerViewFoundSongFragment = new SubSearchRecyclerViewFoundSongFragment();
                }
                if (subSearchRecyclerViewFoundSongFragment.isAdded()) {
                    subSearchRecyclerViewFoundSongFragment.setTrackList(trackList);
                } else {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.search_subSearchMainFragmentContainer, subSearchRecyclerViewFoundSongFragment)
                            .commit();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("GET DATA LOI", "GET KHONG THANH CONG");
                Log.i("GET DATA LOI", Objects.requireNonNull(error.getMessage()));
            }
        });
        Map<String, Object> options = new HashMap<>();
        options.put("limit", 10);
        TracksPager tracksPager = spotifyService.searchTracks(q, options);
        return tracksPager.tracks.items;
    }

    public List<Artist> getArtist(String q) {
        Map<String, Object> options = new HashMap<>();
        options.put("limit", 10);
        ArtistsPager artistsPager = spotifyService.searchArtists(q, options);
        return artistsPager.artists.items;
    }

    public List<AlbumSimple> getAlbums(String q) {
        List<AlbumSimple> albumSimpleList = spotifyService.searchAlbums(q).albums.items;
        if (albumSimpleList.isEmpty()) {
            Log.d(TAG, "Search " + q + " not found");
        }
        return albumSimpleList;
    }
}