package com.example.music_mobile_app.manager.SearchManager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.example.music_mobile_app.R;

import java.util.List;
import java.util.Random;

import kaaes.spotify.webapi.android.models.AlbumSimple;

public class SearchAlbumAdapter extends RecyclerView.Adapter<SearchAlbumAdapter.GenreViewHolder> {
    private Fragment fragment;
    private List<AlbumSimple> mDataList;
    private String[] randomBackgroundColors = {
            "#FFB3BA",
            "#FFDFBA",
            "#FFFFBA",
            "#BAFFC9",
            "#BAE1FF",
            "#D8BAFF",
            "#FFD8FC",
            "#B3E6FF",
            "#BAFFC9",
            "#FFDFBA",
            "#FFB3BA",
            "#FFFFBA",
            "#BAE1FF",
            "#BAFFC9",
            "#D8BAFF",
            "#FFD8FC",
            "#B3E6FF",
            "#BAFFC9",
            "#FFDFBA",
            "#FFB3BA"
    };


    public SearchAlbumAdapter(Context context, Fragment fragment, List<AlbumSimple> dataList) {
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
        public TextView textView;
        public ImageView imageView;
        public CardView cardView;

        public GenreViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_search_genre_textview);
            imageView = itemView.findViewById(R.id.list_item_search_genre_imageview);
            cardView = itemView.findViewById(R.id.list_item_search_genre_cardview);
        }

        public void bind(AlbumSimple albumSimple) {
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