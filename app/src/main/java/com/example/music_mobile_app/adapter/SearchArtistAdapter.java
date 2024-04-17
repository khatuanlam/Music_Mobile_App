package com.example.music_mobile_app.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import kaaes.spotify.webapi.android.models.Track;

public class SearchArtistAdapter extends RecyclerView.Adapter<SearchArtistAdapter.FoundArtistViewHolder> {
    private Fragment fragment;
    public List<Artist> mDataList;

    public SearchArtistAdapter(Fragment fragment, List<Artist> dataList) {
        this.fragment = fragment;
        mDataList = dataList;
    }

    public void setmDataList(List<Artist> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoundArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_found_artist, parent, false);
        return new FoundArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundArtistViewHolder holder, int position) {
        Artist artist = mDataList.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) return 0;
        return mDataList.size();
    }

    public class FoundArtistViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewArtistType;

        public Artist mArtist;
        public TextView textViewArtistName;

        public ImageView imageView;
        public CardView cardView;

        public ImageView optionsImageView;

        public PopupMenu optionsPopupMenu;

        public FoundArtistViewHolder(View itemView) {
            super(itemView);
            textViewArtistName = itemView.findViewById(R.id.list_item_search_found_artist_name);
            textViewArtistType = itemView.findViewById(R.id.list_item_search_found_artist_type);
            imageView = itemView.findViewById(R.id.list_item_search_found_artist_image);
            cardView = itemView.findViewById(R.id.list_item_search_found_artist_cardview);
            optionsImageView = itemView.findViewById(R.id.list_item_search_found_artist_options);

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

        public void bind(Artist artist) {
            this.mArtist = artist;
            textViewArtistName.setText(artist.name);
            textViewArtistType.setText(artist.type);

            if (artist.images.size() > 0) {
                Glide.with(fragment).load(artist.images.get(0).url).into(imageView);
            }

        }
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

}