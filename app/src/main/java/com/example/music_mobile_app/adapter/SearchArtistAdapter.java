package com.example.music_mobile_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_found_artist, parent, false);
        return new FoundArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoundArtistViewHolder holder, int position) {
        Artist artist = mDataList.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null)
            return 0;
        return mDataList.size();
    }

    public class FoundArtistViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewArtistType;

        public Artist artist;
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


        }

        public void bind(Artist artist) {
            this.artist = artist;
            textViewArtistName.setText(artist.name);
            textViewArtistType.setText(artist.type);

            if (artist.images.size() > 0) {
                Glide.with(fragment)
                        .load(artist.images.get(0).url)
                        .into(imageView);
            }

        }

    }

}