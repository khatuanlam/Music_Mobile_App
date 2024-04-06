package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.ui.AccountFragment;
import com.example.music_mobile_app.ui.AlbumFragment;
import com.example.music_mobile_app.ui.PlaylistDetailFragment;

import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class PlayTrackAdapter extends RecyclerView.Adapter<PlayTrackAdapter.ItemHorizontalHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private List<Track> trackList;
    private List<PlaylistSimple> playlistList;

    private Context context;
    private int selectedItem = RecyclerView.NO_POSITION; // Khởi tạo biến để lưu vị trí item được chọn
    private int flag = 0;
    private static VariableManager varManager = MainActivity.varManager;
    // Khai báo một biến để lưu trữ playlistId đã chọn
    private String selectedPlaylistId = null;

    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    public interface OnPlaylistClickListener {
        void onPlaylistClick(String playlistId);
    }

    private OnPlaylistClickListener playlistClickListener;

    public void setOnPlaylistClickListener(OnPlaylistClickListener listener) {
        this.playlistClickListener = listener;
    }

    public PlayTrackAdapter(List<Track> trackList, List<PlaylistSimple> playlistList, Context context) {
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

        // Thiết lập màu nền cho item dựa trên selectedItem
        holder.itemView.setBackgroundColor(position == selectedItem ? ContextCompat.getColor(context, R.color.purple_50) : Color.TRANSPARENT);
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

            itemView.setOnClickListener(v -> {
                // Cập nhật selectedItem khi item được click
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();

                // Xử lý logic khi item được click ở đây
                if (flag == 0) {//Nếu là track
                    Intent intent = new Intent(context, PlayTrackActivity.class);
                    intent.putExtra("Track", mTrack);
                    context.startActivity(intent);
                } else {//Nếu là playlist
                    Log.d(TAG, "Playlist Clicked: " + mPlaylist.id);
                    if (playlistClickListener != null) {
                        playlistClickListener.onPlaylistClick(mPlaylist.id);
                    }
                }
            });
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
            Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
        }
    }
}
