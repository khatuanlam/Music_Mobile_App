package com.example.music_mobile_app.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class ListTrackFragment extends Fragment {


    private RecyclerView recyclerView;
    private Button btnBack;

    private List<Track> trackList = PlayTrackActivity.queuePlayTrack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_track, container, false);

        prepareData(view);

        return view;
    }


    public void prepareData(View view) {

        recyclerView = view.findViewById(R.id.queue_recyclerview);
        LinearLayoutManager favorite_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(favorite_layout);


        btnBack = view.findViewById(R.id.queue_back);
        // Onclick back
        btnBack.setOnClickListener(v -> {
            this.getParentFragmentManager().popBackStack();
        });

        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), this.getContext(),
                ListTrackFragment.this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


}

