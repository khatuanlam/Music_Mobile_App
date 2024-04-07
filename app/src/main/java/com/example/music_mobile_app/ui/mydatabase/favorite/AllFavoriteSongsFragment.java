package com.example.music_mobile_app.ui.mydatabase.favorite;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.favorite.FavoriteSongsAdapter;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllFavoriteSongsFragment extends Fragment {

    private FavoriteSongsViewModel favoriteSongsViewModel;
    private TextView textView;
    private ImageView imageViewBack;
    private FragmentManager manager;

    private FavoriteSongsAdapter mFavoriteSongsAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    private long id;

    public AllFavoriteSongsFragment(long id)
    {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_favorite_songs, container, false);
        imageViewBack = view.findViewById(R.id.mydb_favorite_songs_fragment_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });
        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_favorite_songs_fragment_songs_recyclerView);
        LinearLayoutManager topsong_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        favoriteSongsViewModel = new ViewModelProvider(this).get(FavoriteSongsViewModel.class);


        mFavoriteSongsAdapter = new FavoriteSongsAdapter(getActivity(), this, new ArrayList<Song>(), favoriteSongsViewModel, id);
        songOfAlbumRecyclerView.setAdapter(mFavoriteSongsAdapter);


        favoriteSongsViewModel.getSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mFavoriteSongsAdapter.setmDataList(songs);
                Log.i("FAVORITE","CAP NHAT");
                Log.i("FAVORITE",String.valueOf(songs.size()));
            }
        });
        favoriteSongsViewModel.getIsDeleteSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleteSuccess) {
                if(isDeleteSuccess == null)
                    return;
                if (isDeleteSuccess) {
                    Toast.makeText(getContext(), "ĐÃ GỠ KHỎI DANH SÁCH YÊU THÍCH", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });

        favoriteSongsViewModel.getAllFavoriteSongsByUserId(id);


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        favoriteSongsViewModel.getSongs().removeObservers(this);
    }
 }

