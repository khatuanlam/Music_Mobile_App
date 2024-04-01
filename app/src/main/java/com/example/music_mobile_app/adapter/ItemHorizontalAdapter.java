package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.ui.AlbumFragment;

import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ItemHorizontalAdapter extends RecyclerView.Adapter<ItemHorizontalAdapter.ItemHorizontalHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private List<Track> trackList;
    private List<PlaylistSimple> playlistList;

    private Context context;
    private int flag = 0;
    private static VariableManager varManager = MainActivity.varManager;

    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    public ItemHorizontalAdapter(List<Track> trackList, List<PlaylistSimple> playlistList, Context context) {
        this.trackList = trackList;
        this.playlistList = playlistList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHorizontalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_horizontal, parent, false);

        return new ItemHorizontalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHorizontalHolder holder, int position) {
        if (!playlistList.isEmpty() && position < playlistList.size()) {
            holder.bindPlaylist(playlistList.get(position));
        } else if (!trackList.isEmpty() && position < trackList.size()) {
            holder.bindTrack(trackList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (trackList.isEmpty()) {
            flag = 1;
            return playlistList.size();
        } else {
            return trackList.size();
        }
    }

    protected class ItemHorizontalHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private PlaylistSimple mPlaylist;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;

        public ItemHorizontalHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.horizontal_item_image);
            item_name = itemView.findViewById(R.id.horizontal_item_name);
            item_artist = itemView.findViewById(R.id.horizontal_item_artist);

            if (flag == 0) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PlayTrackActivity.class);
                    intent.putExtra("Track", mTrack);
                    context.startActivity(intent);
                });
            } else {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("Playlist", mPlaylist);
                    context.startActivity(intent);
                });
            }
        }

        public void bindPlaylist(final PlaylistSimple playlist) {
            this.mPlaylist = playlist;
            item_name.setText(playlist.name);
            item_artist.setText(playlist.owner.display_name);
            if (playlist.images != null) {
                baseImage = playlist.images.get(0).url;
            }
            Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
        }

        public void bindTrack(final Track track) {
            this.mTrack = track;
            item_name.setText(track.name);
            item_artist.setText(track.artists.get(0).name);
//            if (track.album.images != null) {
//                baseImage = track.album.images.get(0).url;
//            }
            Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
        }
    }
}
