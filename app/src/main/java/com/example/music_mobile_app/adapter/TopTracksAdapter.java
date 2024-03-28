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

public class TopTracksAdapter extends RecyclerView.Adapter<TopTracksAdapter.TopTracksHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<Track> trackList;
    private Fragment fragment;


    public TopTracksAdapter(List<Track> trackList, Fragment fragment) {
        this.trackList = trackList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TopTracksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.top_track, parent, false);

        return new TopTracksHolder(view);
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TopTracksHolder holder, int position) {
        holder.bind(trackList.get(position));
    }

    protected class TopTracksHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;

        public TopTracksHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.top_track_image);
            item_name = itemView.findViewById(R.id.top_track_name);
            item_artist = itemView.findViewById(R.id.top_track_artist);

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
