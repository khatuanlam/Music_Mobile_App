package com.example.music_mobile_app.adapter.mydatabase;

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
import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;
import java.util.Random;

import kaaes.spotify.webapi.android.models.AlbumSimple;

public class TopSongAdapter extends RecyclerView.Adapter<TopSongAdapter.TopSongViewHolder> {
    private Fragment fragment;
    private List<Song> mDataList;
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
    private Context context;

    public TopSongAdapter(Context context, Fragment fragment, List<Song> dataList) {
        this.context = context;
        this.fragment = fragment;
        mDataList = dataList;
    }

    public void setmDataList(List<Song> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mydb_list_item_search_song, parent, false);
        return new TopSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopSongViewHolder holder, int position) {
        Song song = mDataList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TopSongViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public CardView cardView;

        public TopSongViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_search_genre_textview);
            imageView = itemView.findViewById(R.id.list_item_search_genre_imageview);
            cardView = itemView.findViewById(R.id.list_item_search_genre_cardview);
        }

        public void bind(Song song) {
            textView.setText(song.getName());
            if (song.getImage() != null && !song.getImage().isEmpty()) {
                Glide.with(fragment)
                        .load(song.getImage())
                        .into(imageView);
            }

            Integer i = new Random().nextInt(randomBackgroundColors.length);
            String color = randomBackgroundColors[i];
            cardView.setCardBackgroundColor(Color.parseColor(color));
        }
    }

}