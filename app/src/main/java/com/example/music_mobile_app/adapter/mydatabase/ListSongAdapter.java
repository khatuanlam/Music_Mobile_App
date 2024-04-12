package com.example.music_mobile_app.adapter.mydatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.service.mydatabase.impl.DownloadServiceImpl;
import com.example.music_mobile_app.service.mydatabase.myinterface.DownloadCallback;
import com.example.music_mobile_app.service.mydatabase.myinterface.DownloadService;
import com.example.music_mobile_app.viewmodel.mydatabase.favorite.FavoriteSongsViewModel;
import com.example.music_mobile_app.viewmodel.mydatabase.playlist.SongsOfPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.SongViewHolder> implements OnPlaylistClickListener {
    private Fragment fragment;
    private List<Song> mDataList;
    private Context context;

    private Playlist selectedPlaylist;

    private FavoriteSongsViewModel favoriteSongsViewModel;

    private long userId;

    private AlertDialog alertDialog;
    private RecyclerView playlistRecyclerView;

    private OpenPlaylistAdapter openPlaylistAdapter;
    private  FragmentManager manager;
    private SongsOfPlaylistViewModel songsOfPlaylistViewModel;


    private String type;
    private Song selectedSong;

    public Playlist playlist;

    public void setOpenPlaylists(List<Playlist> playlists)
    {
        openPlaylistAdapter.setmDataList(playlists);
    }


    public ListSongAdapter(Context context, Fragment fragment, FragmentManager manager, List<Song> dataList, FavoriteSongsViewModel favoriteSongsViewModel, long id, SongsOfPlaylistViewModel songsOfPlaylistViewModel, String type, Playlist playlist) {
        this.context = context;
        this.fragment = fragment;
        mDataList = dataList;
        this.manager = manager;
        this.favoriteSongsViewModel = favoriteSongsViewModel;
        this.songsOfPlaylistViewModel = songsOfPlaylistViewModel;
        this.userId = id;
        this.type = type;
        this.playlist = playlist;


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.mydb_dialog_open_playlist, null);
        playlistRecyclerView = dialogView.findViewById(R.id.mydb_dialog_open_playlist_recyclerView);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        openPlaylistAdapter = new OpenPlaylistAdapter(context, fragment, manager, new ArrayList<>());
        openPlaylistAdapter.setOnPlaylistClickListener(this);
        playlistRecyclerView.setAdapter(openPlaylistAdapter);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedPlaylist != null) {
                } else {
                    Toast.makeText(context, "Vui lòng chọn playlist trước khi thêm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(dialogView);
        alertDialog = builder.create();
    }
    @Override
    public void onPlaylistClick(Playlist playlist) {

        selectedPlaylist = playlist;
        alertDialog.dismiss();
        songsOfPlaylistViewModel.postSongToPlaylist(selectedSong.getId(), playlist.getId());
    }
    public void setmDataList(List<Song> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mydb_list_item_song_top_popular_dai, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mDataList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    public class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, textviewPopularity, textViewStt, textViewSingerName;
        public ImageView imageView;

        public ImageView moreVert;

        public Song song;


        public SongViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.mydb_list_item_song_popular_dai_songNameTextView);
            textviewPopularity = itemView.findViewById(R.id.mydb_list_item_song_popular_dai_soLuotThich);
            textViewStt = itemView.findViewById(R.id.mydb_list_item_song_popular_dai_stt);

            imageView = itemView.findViewById(R.id.mydb_list_item_song_popular_dai_imageView);
            moreVert = itemView.findViewById(R.id.mydb_list_item_song_popular_dai_more);

            moreVert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });

        }
        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(context, view);

            if (type.equals("Popular Song") || type.equals("Album Song")) {
                popupMenu.inflate(R.menu.mydb_list_item_song);
            } else if (type.equals("Favorite Song")) {
                popupMenu.inflate(R.menu.mydb_list_item_song_favorite);
            } else if (type.equals("Playlist Song")) {
                popupMenu.inflate(R.menu.mydb_list_item_song_playlist);
            } else {
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.mydb_menu_item_song_addToFavorite:
                            favoriteSongsViewModel.postFavoriteSongToUser(song.getId(), userId);
                            break;
                        case R.id.mydb_menu_item_song_openPlaylist:
                            selectedSong = song;
                            showPlaylistListDialog();
                            break;
                        case R.id.mydb_menu_item_song_download:

                            DownloadService downloadService = new DownloadServiceImpl(context);
                            downloadService.downloadMp3(song.getUrlSong(), new DownloadCallback() {
                                @Override
                                public void onDownloadComplete() {
//                                    Toast.makeText(context, "CHUẨN BỊ TẢI",Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case R.id.mydb_menu_item_song_favorite_remove:
                            favoriteSongsViewModel.deleteFavoriteSongByIdUser(song.getId(), userId);
                            break;
                        case R.id.mydb_menu_item_song_playlist_remove:
                            songsOfPlaylistViewModel.deleteSongFromPlaylist(song.getId(), playlist.getId());
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
        public void showPlaylistListDialog()
        {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
            layoutParams.width = width;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            alertDialog.getWindow().setAttributes(layoutParams);
            alertDialog.setCanceledOnTouchOutside(true);

            alertDialog.show();
        }
        public void bind(Song song) {
            this.song = song;
            textView.setText(song.getName());
            textviewPopularity.setText(song.getPopularity() + " ❤️");
            if(type.equals("Popular Song")) {
                textViewStt.setText(String.valueOf(getAdapterPosition() + 1));
            }
            if (song.getImage() != null && !song.getImage().isEmpty()) {
                Glide.with(fragment)
                        .load(song.getImage())
                        .into(imageView);
            }

        }
    }

}