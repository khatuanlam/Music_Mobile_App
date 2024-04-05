package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.ui.AlbumFragment;

import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class DetailPlaylistAdapter extends RecyclerView.Adapter<DetailPlaylistAdapter.DetailPlaylistHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private List<PlaylistTrack> playlistTracks;
    private Context context;
    private int selectedItem = RecyclerView.NO_POSITION; // Khởi tạo biến để lưu vị trí item được chọn
    private static final String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    public DetailPlaylistAdapter(List<PlaylistTrack> playlistTracks, Context context) {
        this.playlistTracks = playlistTracks;
        this.context = context;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(String trackUri    );
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public DetailPlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_horizontal, parent, false);
        return new DetailPlaylistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPlaylistHolder holder, int position) {
        holder.bindPlaylist(playlistTracks.get(position));
        holder.itemView.setBackgroundColor(position == selectedItem ? ContextCompat.getColor(context, R.color.purple_50) : ContextCompat.getColor(context, android.R.color.transparent));

        holder.itemView.setOnLongClickListener(v -> {
            String trackUri = playlistTracks.get(position).track.uri;
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(trackUri);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return playlistTracks.size();
    }

    protected class DetailPlaylistHolder extends RecyclerView.ViewHolder {
        private PlaylistTrack playlistTrack;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;

        public DetailPlaylistHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.horizontal_item_image);
            item_name = itemView.findViewById(R.id.horizontal_item_name);
            item_artist = itemView.findViewById(R.id.horizontal_item_artist);

            itemView.setOnClickListener(v -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
                PlaylistTrack track = playlistTracks.get(getAdapterPosition());
                Intent intent = new Intent(context, PlayTrackActivity.class);
                intent.putExtra("Track", track.track);
                context.startActivity(intent);
            });
        }

        public void bindPlaylist(final PlaylistTrack playlistTrack) {
            this.playlistTrack = playlistTrack;
            item_name.setText(playlistTrack.track.name);
            if (playlistTrack.track.album.images != null && !playlistTrack.track.album.images.isEmpty()) {
                String imageUrl = playlistTrack.track.album.images.get(0).url;
                Glide.with(context).load(imageUrl).override(Target.SIZE_ORIGINAL).into(item_image);
            } else {
                Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
            }
        }
    }
}
