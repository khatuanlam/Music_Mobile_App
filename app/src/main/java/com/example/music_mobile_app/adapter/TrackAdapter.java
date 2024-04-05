package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Track;
import com.example.music_mobile_app.ui.FavoriteFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private List<Track> trackList;
    private Context context;
    private FavoriteFragment favoriteFragment;
    private boolean showDeleteMenuItem = false;
    public TrackAdapter(List<Track> trackList, FragmentActivity activity) {

    }

    public TrackAdapter(List<Track> trackList,Context context) {
        this.trackList = trackList;
        this.context = context;
        notifyDataSetChanged();
    }


    private ListenerManager.TrackAdapterListener listener;

    public void setListener(ListenerManager.TrackAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrackAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new TrackViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        if (trackList != null) {
            Track track = trackList.get(position);
            holder.TextViewPosition.setText(String.valueOf(position + 1 ));
            holder.trackName.setText(track.getName());
            holder.artistName.setText(track.getArtists().get(0).getName());

            Album album = track.getAlbum();
            if (album != null && album.getImageUrl() != null && !album.getImageUrl().isEmpty()) {
                String imageUrl = album.getImageUrl().get(0).getUrl();
                Picasso.get().load(imageUrl).into(holder.trackImage);
            } else {
                holder.trackImage.setVisibility(View.GONE);
            }

            // Set OnClickListener for overflow button
            holder.overflowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getBindingAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        showPopupMenu(clickedPosition, holder.overflowButton);
                    }
                }
            });
        }
    }


    public void showPopupMenu(int position ,View view){
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.search_options_view_item_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deleteTrack_from_favorite:
                        if (listener != null) {
                            listener.onDeleteTrackClicked(trackList.get(position).getId());
                        }
                        return true;
                    default:
                        return false;
                }

            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return trackList != null ? trackList.size() : 0;
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView TextViewPosition;
        TextView trackName;
        TextView artistName;
        ImageView trackImage;
        ImageButton overflowButton;
        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            TextViewPosition = itemView.findViewById(R.id.textViewPosition);
            trackName = itemView.findViewById(R.id.songTitle);
            artistName = itemView.findViewById(R.id.artistname);
            trackImage = itemView.findViewById(R.id.TrackImageSong);
            overflowButton = itemView.findViewById(R.id.overflowSongmenu);
        }
    }
}
