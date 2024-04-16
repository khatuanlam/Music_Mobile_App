package com.example.music_mobile_app.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.ui.AlbumFragment;

import java.util.List;
import java.util.Random;

import kaaes.spotify.webapi.android.models.AlbumSimple;

public class SearchGenreAdapter extends RecyclerView.Adapter<SearchGenreAdapter.GenreViewHolder> {
    private Fragment fragment;
    private List<AlbumSimple> mDataList;
    private String[] randomBackgroundColors = {
            "#CC3333",
            "#CC6633",
            "#CCCC33",
            "#33CC33",
            "#3366CC",
            "#6633CC",
            "#CC6699",
            "#336666",
            "#33CC33",
            "#CC6633",
            "#CC3333",
            "#CCCC33",
            "#3366CC",
            "#33CC33",
            "#6633CC",
            "#CC6699",
            "#336666",
            "#33CC33",
            "#CC6633",
            "#CC3333"
    };

    public SearchGenreAdapter(Fragment fragment, List<AlbumSimple> dataList) {
        this.fragment = fragment;
        mDataList = dataList;
    }


    public void setmDataList(List<AlbumSimple> mDataList) {
        this.mDataList = mDataList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        AlbumSimple albumSimple = mDataList.get(position);
        holder.bind(albumSimple);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {

        public AlbumSimple mAlbum;
        public TextView textView;
//        public TextView textViewAlbumName;
//        public TextView  textViewSongAlbumName;
        public ImageView imageView;
        public CardView cardView;

        public GenreViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_search_genre_textview);
            imageView = itemView.findViewById(R.id.list_item_search_genre_imageview);
            cardView = itemView.findViewById(R.id.list_item_search_genre_cardview);

            itemView.setOnClickListener(v -> {
                FragmentManager manager = fragment.getChildFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putParcelable("AlbumDetail", (Parcelable) mAlbum);
                AlbumFragment albumFragment = new AlbumFragment();
                albumFragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.fragment, albumFragment).commit();
            });
        }

        public void bind(AlbumSimple albumSimple) {
            this.mAlbum = albumSimple;
            textView.setText(albumSimple.name);
            if (albumSimple.images.size() > 0) {
                Glide.with(fragment)
                        .load(albumSimple.images.get(0).url)
                        .into(imageView);
            }

            Integer i = new Random().nextInt(randomBackgroundColors.length);
            String color = randomBackgroundColors[i];
            cardView.setCardBackgroundColor(Color.parseColor(color));
        }
    }

}