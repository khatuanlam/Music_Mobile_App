package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.ui.ArtistFragment;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<Artist> mArtists;
    private static Fragment fragment;

    public FollowingAdapter(List<Artist> artists, Fragment fragment) {
        this.mArtists = artists;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(mArtists.get(position));

    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView artistImage;
        public TextView artistName, artistNumber;
        public Artist mArtist;

        public ViewHolder(View itemView) {
            super(itemView);

            artistImage = itemView.findViewById(R.id.track_item_image);
            artistName = itemView.findViewById(R.id.track_item_name);
//            artistNumber = itemView.findViewById(R.id.track_item_artist);
            itemView.setOnClickListener(v -> {
                MethodsManager.getInstance().getArtistTopTrack(mArtist.id, "", new ListenerManager.ListTrackOnCompleteListener() {
                    @Override
                    public void onComplete(List<Track> trackList) {
                        // Send detail artist
                        sendToDetailArtist(mArtist, trackList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(fragment.getTag(), "Cannot get this " + mArtist.name);
                    }
                });
            });
        }

        public void sendToDetailArtist(Artist artist, List<Track> artistTopTrack) {
            FragmentManager manager = fragment.getChildFragmentManager();
            ArtistFragment artistFragment = new ArtistFragment();
            // Attach artistdetail
            Bundle bundle = new Bundle();
            bundle.putParcelable("ArtistDetail", (Parcelable) artist);
            bundle.putParcelableArrayList("ListTrack", new ArrayList<Parcelable>(artistTopTrack));
            artistFragment.setArguments(bundle);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            manager.beginTransaction().replace(R.id.fragment, artistFragment).commit();
        }

        public void bind(final Artist artist) {
            this.mArtist = artist;
            artistName.setText(artist.name);
            Glide.with(fragment).load(artist.images.get(0).url).into(artistImage);
        }
    }

}

