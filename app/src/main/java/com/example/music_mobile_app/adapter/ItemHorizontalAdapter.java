package com.example.music_mobile_app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.ui.PlaylistFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ItemHorizontalAdapter extends RecyclerView.Adapter<ItemHorizontalAdapter.ItemHorizontalHolder> implements AdapterView.OnItemLongClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private List<Track> trackList;
    private List<PlaylistSimple> playlistList;
    private Context context;
    private Fragment fragment;
    private Album mAlbum;
    private List<Album> albumList;
    private int selectedItem = RecyclerView.NO_POSITION; // Khởi tạo biến để lưu vị trí item được chọn
    private int type = 0;
    private boolean isSend = false;

    private static String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";


    public ItemHorizontalAdapter(List<Track> trackList, Album album, List<PlaylistSimple> playlistList, Context context, Fragment fragment) {
        this.trackList = trackList;
        this.playlistList = playlistList;
        this.context = context;
        this.fragment = fragment;
        if (album == null) {
            this.mAlbum = new Album();
        } else {
            this.mAlbum = album;
        }

    }

    @NonNull
    @Override
    public ItemHorizontalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_horizontal, parent, false);

        return new ItemHorizontalHolder(view);
    }

    @SuppressLint("ResourceAsColor")
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
            type = 1;
            return playlistList.size();
        } else {
            return trackList.size();
        }
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    protected class ItemHorizontalHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private PlaylistSimple mPlaylist;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;
        private Spinner options;

        public ItemHorizontalHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.horizontal_item_image);
            item_name = itemView.findViewById(R.id.horizontal_item_name);
            item_artist = itemView.findViewById(R.id.horizontal_item_artist);
            options = itemView.findViewById(R.id.spinner);

            if (type == 0) {
                itemView.setOnClickListener(v -> {
                    if (isSend) {

                    } else {
                        Intent intent = new Intent(context, PlayTrackActivity.class);
                        intent.putExtra("Track", mTrack);
                        intent.putExtra("Track's Album", mAlbum);
                        context.startActivity(intent);
                    }
                });
            } else {
                itemView.setOnClickListener(v -> {
                    if (isSend) {
                        // Cập nhật selectedItem khi item được click
                        selectedItem = getAbsoluteAdapterPosition();
                        EventBus.getDefault().post(mPlaylist);
                        notifyDataSetChanged();
                    } else {
                        MethodsManager.getInstance().getPlayListTrack(mPlaylist.id, context, new ListenerManager.ListTrackOnCompleteListener() {
                            @Override
                            public void onComplete(List<Track> trackList) {
                                // Send detail playlist
                                sendDetailPlaylist(trackList, mPlaylist);
                            }

                            @Override
                            public void onError(Throwable error) {
                                Log.e(TAG, "Cannot not get " + mPlaylist.name);
                            }
                        });
                    }
                });
                //
                itemView.setOnLongClickListener(v -> {
                    return true;
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
            if (track.album != null) {
                baseImage = track.album.images.get(0).url;
            } else {
                baseImage = mAlbum.images.get(0).url;
            }
            Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
        }

        public void bindAlbum(final Album album) {

        }

        private void sendDetailPlaylist(List<Track> trackList, PlaylistSimple playlist) {
            FragmentManager manager = fragment.getChildFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putParcelable("PlaylistDetail", (Parcelable) playlist);
            bundle.putParcelableArrayList("ListTrack", new ArrayList<Parcelable>(trackList));
            PlaylistFragment playlistFragment = new PlaylistFragment();
            playlistFragment.setArguments(bundle);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            manager.beginTransaction().replace(R.id.fragment, playlistFragment).commit();
        }


    }
}
