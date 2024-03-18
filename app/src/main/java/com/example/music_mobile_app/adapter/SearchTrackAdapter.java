package com.example.music_mobile_app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SearchTrackAdapter extends RecyclerView.Adapter<SearchTrackAdapter.FoundSongViewHolder> {
    private Fragment fragment;
    public List<Track> mDataList;

    public static Track track;


    public SearchTrackAdapter(Fragment fragment, List<Track> dataList) {
        this.fragment = fragment;
        this.mDataList = dataList;
    }

    public void setmDataList(List<Track> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoundSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_found_song, parent, false);
        return new FoundSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundSongViewHolder holder, int position) {
        track = mDataList.get(position);
        holder.bind(track);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class FoundSongViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSingerName;
        public TextView textViewSongName;
        public ImageView imageView;
        public CardView cardView;

        public FoundSongViewHolder(View itemView) {
            super(itemView);
            textViewSingerName = itemView.findViewById(R.id.list_item_search_found_song_singerName);
            textViewSongName = itemView.findViewById(R.id.list_item_search_found_song_songName);
            imageView = itemView.findViewById(R.id.list_item_search_found_song_image);
            cardView = itemView.findViewById(R.id.list_item_search_found_song_cardview);

            itemView.setOnClickListener((View v) -> {
                Intent intent = new Intent(fragment.getContext(), PlayTrackActivity.class);
                intent.putExtra("Track", track);
                fragment.getActivity().startActivity(intent);
            });
        }

        public void bind(Track track) {
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

    }

}