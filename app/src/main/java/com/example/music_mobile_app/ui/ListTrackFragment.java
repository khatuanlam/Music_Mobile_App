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

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class ListTrackFragment extends Fragment {

    private OnDataChangeListener listener;

    public interface OnDataChangeListener {
        void onDataChanged(List<Track> newArrayList);
    }
    private RecyclerView recyclerView;
    private Button btnBack;

    private List<Track> trackList = new ArrayList<>();


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
//            header.setVisibility(View.VISIBLE);
            this.getParentFragmentManager().popBackStack();
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("ListTrack");
            // Chuyển đổi từ parcelableList sang List<Track>
            for (Parcelable parcelable : parcelableList) {
                if (parcelable instanceof Track) {
                    trackList.add((Track) parcelable);
                }
            }
            Toast.makeText(this.getActivity(), trackList.size() + "ListTrack", Toast.LENGTH_SHORT).show();
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), this.getContext(),
                    this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnDataChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDataChangeListener");
        }
    }
}

