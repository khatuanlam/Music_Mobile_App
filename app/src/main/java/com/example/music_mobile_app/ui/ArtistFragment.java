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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_mobile_app.R;

import java.util.ArrayList;
import java.util.List;

import com.example.music_mobile_app.adapter.SongAdapter;

import com.example.music_mobile_app.util.HandleBackground;
import com.example.music_mobile_app.model.Song;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {
    ImageView artistAvatar;
    LinearLayout content_container;
    private Drawable backgroundDrawable;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private TextView artistName;
    private TextView listeners;
    private ImageView artistImage;
    private Button playMusic;
    private RecyclerView recyclerView;
    private SongAdapter SongAdapter;
    public ArtistFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1, String param2) {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager manager = getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        artistName = view.findViewById(R.id.textArtistName);
        artistImage = view.findViewById(R.id.artistAvatar);
        listeners = view.findViewById(R.id.followerNumber);
        playMusic = view.findViewById(R.id.playAristButton);
        List<Song> songList = new ArrayList<>();

        songList.add(new Song(1, R.drawable.sontungmtp, "Song 1"));
        songList.add(new Song(2, R.drawable.sontungmtp, "Song 2"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerMusicView);
        SongAdapter = new SongAdapter(songList, getContext());
        recyclerView.setAdapter(SongAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Find the ImageView within the fragment's layout
        artistAvatar = view.findViewById(R.id.artistAvatar);
        //get background framelayout
        content_container = view.findViewById(R.id.content_container);
        backgroundDrawable = content_container.getBackground();

        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create an instance of HandleBackground and call handleBackground method
        HandleBackground backgroundHandler = new HandleBackground();
        backgroundHandler.handleBackground(artistImage, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
            @Override
            public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                // Set the updated Drawable as the background of your view
                content_container.setBackground(updatedDrawable);
            }
        });
    }


}