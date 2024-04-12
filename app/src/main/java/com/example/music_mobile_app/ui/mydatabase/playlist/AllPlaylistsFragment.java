package com.example.music_mobile_app.ui.mydatabase.playlist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.mydatabase.mainFragment.YourPlaylistsAdapter;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.ui.mydatabase.MainFragment;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.AllPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllPlaylistsFragment extends Fragment {

    private AllPlaylistViewModel allPlaylistViewModel;

    private ImageView imageViewBack;

    private Button addBtn;
    private FragmentManager manager;

    private YourPlaylistsAdapter yourPlaylistsAdapter;

    private RecyclerView songOfAlbumRecyclerView;

    private long userId;

    public AllPlaylistsFragment(long id)
    {
        this.userId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_all_playlists, container, false);
        imageViewBack = view.findViewById(R.id.mydb_all_playlists_fragment_back);
        addBtn = view.findViewById(R.id.mydb_all_playlists_fragment_addPlaylist);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.mydb_dialog_new_playlist, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setTitle("Nhập tên playlist");


                EditText editText = dialogView.findViewById(R.id.mydb_dialog_new_playlist_editText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String enteredText = editText.getText().toString();
                        allPlaylistViewModel.postFavoriteSongToUser(userId, enteredText);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        songOfAlbumRecyclerView = view.findViewById(R.id.mydb_all_playlists_fragment_recyclerView);
        GridLayoutManager topsong_layoutManager = new GridLayoutManager(getContext(), 3);
        songOfAlbumRecyclerView.setLayoutManager(topsong_layoutManager);

        allPlaylistViewModel = new ViewModelProvider(this).get(AllPlaylistViewModel.class);


        yourPlaylistsAdapter = new YourPlaylistsAdapter(getActivity(), this, manager, new ArrayList<Playlist>(), userId);
        songOfAlbumRecyclerView.setAdapter(yourPlaylistsAdapter);

        allPlaylistViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> songs) {
                yourPlaylistsAdapter.setmDataList(songs);
                Log.i("ALL PLAYLISTS","CAP NHAT");
                Log.i("ALL PLAYLISTS",String.valueOf(songs.size()));
            }
        });
        allPlaylistViewModel.getIsPostSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPostSuccess) {
                if(isPostSuccess == null)
                    return;
                if (isPostSuccess) {
                    Toast.makeText(getContext(), "ĐÃ THÊM", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "CÓ LỖI XẢY RA", Toast.LENGTH_SHORT).show();

                }
            }
        });


        allPlaylistViewModel.getAllPlaylistsByIdUser(userId);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        allPlaylistViewModel.getPlaylists().removeObservers(this);
        allPlaylistViewModel.getIsPostSuccess().removeObservers(this);
    }
 }

