package com.example.music_mobile_app.adapter.mydatabase.mainFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.mydatabase.album.AlbumDetailFragment;

import java.util.List;

public class TopAlbumPopularAdapter extends RecyclerView.Adapter<TopAlbumPopularAdapter.TopSongViewHolder> {
    private Fragment fragment;

    private FragmentManager manager;
    private List<Album> mDataList;
    private Context context;

    private long userId;
    public LiteSongRepository liteSongRepository;
    public TopAlbumPopularAdapter(Context context, Fragment fragment,FragmentManager manager, List<Album> dataList, long userId, LiteSongRepository liteSongRepository) {
        this.context = context;
        this.fragment = fragment;
        this.manager = manager;
        mDataList = dataList;
        this.userId = userId;
        this.liteSongRepository = liteSongRepository;
    }

    public void setmDataList(List<Album> mDataList) {
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
        Album t = mDataList.get(position);
        holder.bind(t);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TopSongViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;


        public TopSongViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_top_popular_textview);
            textView.setSelected(true);
            imageView = itemView.findViewById(R.id.mydb_list_item_top_popular_imageview);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Album album = mDataList.get(position);
                        AlbumDetailFragment albumDetailFragment = new AlbumDetailFragment(album, userId ,liteSongRepository);
                        manager.beginTransaction()
                                .replace(R.id.fragment, albumDetailFragment)
                                .commit();
                    }
                }
            });

        }

        public void bind(Album t) {
            textView.setText(t.getName());
            if (t.getImage() != null && !t.getImage().isEmpty()) {
                Glide.with(fragment)
                        .load(t.getImage())
                        .into(imageView);
            }

        }
    }

}