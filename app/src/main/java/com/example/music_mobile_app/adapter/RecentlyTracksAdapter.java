package com.example.music_mobile_app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class RecentlyTracksAdapter extends RecyclerView.Adapter<RecentlyTracksAdapter.RecentlyTracksHolder> {

    public final String TAG = this.getClass().getSimpleName();
    private List<Track> listRecentlyTracks;

    private Fragment fragment;

    public RecentlyTracksAdapter(List<Track> listRecentlyTracks, Fragment fragment) {
        this.listRecentlyTracks = listRecentlyTracks;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecentlyTracksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.recently_played_music, parent, false);

        return new RecentlyTracksHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyTracksHolder holder, int position) {
        holder.bind(listRecentlyTracks.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    protected class RecentlyTracksHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;

        public RecentlyTracksHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.recently_item_image);
            item_name = itemView.findViewById(R.id.recently_item_name);
            item_artist = itemView.findViewById(R.id.recently_item_artist);

            itemView.setOnClickListener((View v) -> {
                Intent intent = new Intent(fragment.getContext(), PlayTrackActivity.class);
                intent.putExtra("Track", mTrack);
                fragment.getActivity().startActivity(intent);
            });
        }

        public void bind(final Track track) {
            this.mTrack = track;
            item_artist.setText(track.artists.get(0).name);
            Glide.with(fragment).load(track.album.images.get(0).url).override(Target.SIZE_ORIGINAL).into(item_image);
            item_name.setText(track.name);

        }
    }
}
