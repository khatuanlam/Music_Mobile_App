package com.example.music_mobile_app.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.music_mobile_app.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.SongAdapter;
import Models.Song;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private TextView artistName;
    private TextView listeners;
    private ImageView artistImage;
    private Button followButton;
    private Button overflowMenu;
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
        overflowMenu = view.findViewById(R.id.overflowArtistButton);
        playMusic = view.findViewById(R.id.playAristButton);
        List<Song> songList = new ArrayList<>();
        songList.add(new Song(1, R.drawable.sontungmtp, "Em của ngày hôm qua"));
        songList.add(new Song(2, R.drawable.sontungmtp, "Chúng ta của hiện tại"));
        songList.add(new Song(3, R.drawable.sontungmtp, "Cơn mưa ngang qua"));
        songList.add(new Song(4, R.drawable.sontungmtp, "Nắng ấm xa dần"));
        songList.add(new Song(5, R.drawable.sontungmtp, "Vết mưa"));
        songList.add(new Song(6, R.drawable.sontungmtp, "Mơ"));
        recyclerView = view.findViewById(R.id.recyclerMusicView);
        SongAdapter = new SongAdapter(songList, getActivity()); // Sửa đoạn này
        recyclerView.setAdapter(SongAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;

    }


}