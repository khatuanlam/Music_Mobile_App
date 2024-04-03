package com.example.music_mobile_app.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.ui.AlbumFragment;

import java.io.Serializable;
import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.TracksHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<Track> trackList;
    private List<AlbumSimple> albumList;
    private Fragment fragment;

    private int flag = 0;
    String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    public ItemAdapter(List<Track> trackList, List<AlbumSimple> albumList, Fragment fragment) {
        this.trackList = trackList;
        this.albumList = albumList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TracksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_track, parent, false);

        return new TracksHolder(view);
    }

    @Override
    public int getItemCount() {
        if (trackList.isEmpty()) {
            flag = 1;
            return albumList.size();
        } else {
            return trackList.size();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TracksHolder holder, int position) {

        if (!albumList.isEmpty() && position < albumList.size()) {
            holder.bindAlbums(albumList.get(position));
        } else if (!trackList.isEmpty() && position < trackList.size()) {
            holder.bindTrack(trackList.get(position));
        }
    }

    protected class TracksHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private AlbumSimple mAlbum;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;

        public TracksHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.track_item_image);
            item_name = itemView.findViewById(R.id.track_item_name);
            item_artist = itemView.findViewById(R.id.track_item_artist);

            // Set to track
            if (flag == 0) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(fragment.getContext(), PlayTrackActivity.class);
                    intent.putExtra("Track", mTrack);
                    fragment.getActivity().startActivity(intent);
                });
            } else {
                //Set to album
                itemView.setOnClickListener(v -> {
                    FragmentManager manager = fragment.getChildFragmentManager();
                    // Send detail album
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("AlbumDetail", (Parcelable) mAlbum);
                    AlbumFragment albumFragment = new AlbumFragment();
                    albumFragment.setArguments(bundle);
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.addToBackStack(null);
                    manager.beginTransaction().replace(R.id.fragment, albumFragment).commit();
                });
            }
        }

        public void bindTrack(final Track track) {
            flag = 0;
            this.mTrack = track;
            item_artist.setText(track.artists.get(0).name);
            if (track.album.images != null) {
                baseImage = track.album.images.get(0).url;
            }
            Glide.with(fragment).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
            item_name.setText(track.name);
        }

        public void bindAlbums(final AlbumSimple albumSimple) {
            flag = 1;
            this.mAlbum = albumSimple;
            if (albumSimple.images != null) {
                baseImage = albumSimple.images.get(0).url;
            }
            Glide.with(fragment).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);
            item_name.setText(albumSimple.name);
            item_artist.setText(albumSimple.album_type.toUpperCase());
        }
    }
}
