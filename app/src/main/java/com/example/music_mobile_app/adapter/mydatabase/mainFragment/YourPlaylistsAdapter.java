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
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.repository.sqlite.LiteSongRepository;
import com.example.music_mobile_app.ui.mydatabase.playlist.PlaylistDetailFragment;

import java.util.List;

public class YourPlaylistsAdapter extends RecyclerView.Adapter<YourPlaylistsAdapter.TopPlaylistViewHolder> {
    private Fragment fragment;
    private List<Playlist> mDataList;
    private Context context;

    private FragmentManager manager;

    private long userId;
    public LiteSongRepository liteSongRepository;
    public YourPlaylistsAdapter(Context context, Fragment fragment, FragmentManager manager, List<Playlist> dataList, long userId, LiteSongRepository liteSongRepository) {
        this.context = context;
        this.fragment = fragment;
        this.manager = manager;
        mDataList = dataList;
        this.userId = userId;
        this.liteSongRepository = liteSongRepository;
    }

    public void setmDataList(List<Playlist> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mydb_list_item_top_popular, parent, false);
        return new TopPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlaylistViewHolder holder, int position) {
        Playlist song = mDataList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class TopPlaylistViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;


        public TopPlaylistViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_item_top_popular_textview);
            imageView = itemView.findViewById(R.id.mydb_list_item_top_popular_imageview);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Playlist playlist = mDataList.get(position);
                        PlaylistDetailFragment playlistDetailFragment = new PlaylistDetailFragment(playlist, userId, liteSongRepository);
                        manager.beginTransaction()
                                .replace(R.id.fragment, playlistDetailFragment)
                                .commit();
                    }
                }
            });
        }

        public void bind(Playlist t) {
            textView.setText(t.getName());
                Glide.with(fragment)
                        .load("https://c8.alamy.com/comp/2BP6RWB/playlist-icon-from-music-collection-simple-line-playlist-icon-for-templates-web-design-and-infographics-2BP6RWB.jpg")
                        .into(imageView);
            }

        }
}
