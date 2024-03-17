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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class CombinedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Fragment fragment;
    private List<Track> trackList;
    private List<Artist> artistList;

    public CombinedRecyclerViewAdapter(Fragment fragment, List<Track> trackList, List<Artist> artistList) {
        this.fragment = fragment;
        this.trackList = trackList;
        this.artistList = artistList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < trackList.size()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_search_found_song, parent, false);
            return new FoundSongViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_search_found_artist, parent, false);
            return new FoundArtistViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FoundSongViewHolder) {

            Track track = trackList.get(position);
            ((FoundSongViewHolder) holder).bind(track);
        } else {

            Artist artist = artistList.get(position - trackList.size());
            ((FoundArtistViewHolder) holder).bind(artist);
        }
    }

    @Override
    public int getItemCount() {
        return trackList.size() + artistList.size();
    }

    public class FoundSongViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSingerName;

        public Track track;
        public TextView textViewSongName;
        public ImageView imageView;
        public CardView cardView;

        public ImageView optionsImageView;

        public PopupMenu optionsPopupMenu;
        private static final String CLIENT_ID = "7c60ff86c42246c1b928e8d87a743554";
        private static final String REDIRECT_URI = "http://localhost:3000/callback";

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

    public class FoundArtistViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewArtistType;

        public Artist artist;
        public TextView textViewArtistName;
        public ImageView imageView;
        public CardView cardView;

        public ImageView optionsImageView;

        public PopupMenu optionsPopupMenu;

        public FoundArtistViewHolder(View itemView) {
            super(itemView);
            textViewArtistName = itemView.findViewById(R.id.list_item_search_found_artist_name);
            textViewArtistType = itemView.findViewById(R.id.list_item_search_found_artist_type);
            imageView = itemView.findViewById(R.id.list_item_search_found_artist_image);
            cardView = itemView.findViewById(R.id.list_item_search_found_artist_cardview);
            optionsImageView = itemView.findViewById(R.id.list_item_search_found_artist_options);



        }

        public void bind(Artist artist) {
            this.artist = artist;
            textViewArtistName.setText(artist.name);
            textViewArtistType.setText(artist.type);

            if (artist.images.size() > 0) {
                Glide.with(fragment)
                        .load(artist.images.get(0).url)
                        .into(imageView);
            }

        }

    }
}