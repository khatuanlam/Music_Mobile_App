package com.example.music_mobile_app.manager.SearchManager.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;


import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SearchTrackAdapter extends RecyclerView.Adapter<SearchTrackAdapter.FoundSongViewHolder> {
    private Fragment fragment;
    public List<Track> mDataList;

    public SearchTrackAdapter(Fragment fragment, List<Track> dataList) {
        this.fragment = fragment;
        mDataList = dataList;
    }

    public void setmDataList(List<Track> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoundSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_found_song, parent, false);
        return new FoundSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundSongViewHolder holder, int position) {
        Track track = mDataList.get(position);
        holder.bind(track);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class FoundSongViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSingerName;

        public Track track;
        public TextView textViewSongName;
        public ImageView imageView;
        public CardView cardView;

        public ImageView optionsImageView;

        public PopupMenu optionsPopupMenu;


        public FoundSongViewHolder(View itemView) {
            super(itemView);
            textViewSingerName = itemView.findViewById(R.id.list_item_search_found_song_singerName);
            textViewSongName = itemView.findViewById(R.id.list_item_search_found_song_songName);
            imageView = itemView.findViewById(R.id.list_item_search_found_song_image);
            cardView = itemView.findViewById(R.id.list_item_search_found_song_cardview);
            optionsImageView = itemView.findViewById(R.id.list_item_search_found_song_options);

            optionsPopupMenu = new PopupMenu(fragment.requireContext(), optionsImageView);

            optionsPopupMenu.getMenuInflater().inflate(R.menu.search_options_view_item_menu, optionsPopupMenu.getMenu());
            optionsImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionsPopupMenu.show();
                }
            });
            optionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.search_options_view_item_menu_download:
                            downloadHandler();
                            return true;
                        case R.id.search_options_view_item_menu_add_favorite:
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }

        public void bind(Track track) {
            this.track = track;
            textViewSongName.setText(track.album.name);
            if (track.artists.size() > 0) {
                textViewSingerName.setText(track.artists.get(0).name);
            }
            if (track.album.images.size() > 0) {
                Glide.with(fragment)
                        .load(track.album.images.get(0).url)
                        .into(imageView);
            }

        }
        public void downloadHandler()
        {
            Toast.makeText(fragment.requireContext(), "Tai xuong: " +track.album.name, Toast.LENGTH_SHORT).show();
        }

    }

}