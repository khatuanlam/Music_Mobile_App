package com.example.music_mobile_app.adapter.mydatabase.mainFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

public class TopSongPopularAdapter extends RecyclerView.Adapter<TopSongPopularAdapter.TopSongViewHolder> {
    private Fragment fragment;
    private List<Song> mDataList;
    private Context context;

    public TopSongPopularAdapter(Context context, Fragment fragment, List<Song> dataList) {
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
                .inflate(R.layout.mydb_list_item_top_popular, parent, false);
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
        public Song song;

        public TopSongViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_top_popular_textview);
            imageView = itemView.findViewById(R.id.mydb_list_item_top_popular_imageview);
        }

        public void bind(Song t) {
            this.song = t;
            textView.setText(t.getName());
            if (t.getImage() != null && !t.getImage().isEmpty()) {
                Glide.with(fragment)
                        .load(t.getImage())
                        .into(imageView);
            }

        }
    }

}