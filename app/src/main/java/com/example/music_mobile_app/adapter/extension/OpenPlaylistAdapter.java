package com.example.music_mobile_app.adapter.extension;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.extension.Playlist;

import java.util.List;

public class OpenPlaylistAdapter extends RecyclerView.Adapter<OpenPlaylistAdapter.OpenPlaylistViewHolder> {
    private Fragment fragment;
    private List<Playlist> mDataList;
    private Context context;

    private FragmentManager manager;

    private OnPlaylistClickListener playlistClickListener;

    public OpenPlaylistAdapter(Context context, Fragment fragment, FragmentManager manager, List<Playlist> dataList) {
        this.context = context;
        this.fragment = fragment;
        this.manager = manager;
        mDataList = dataList;
    }

    public void setOnPlaylistClickListener(OnPlaylistClickListener listener) {
        this.playlistClickListener = listener;
    }

    public void setmDataList(List<Playlist> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OpenPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.extension_list_item_open_playlist, parent, false);
        return new OpenPlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenPlaylistViewHolder holder, int position) {
        Playlist song = mDataList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class OpenPlaylistViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Playlist playlist;

        public OpenPlaylistViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.mydb_list_item_open_playlist_playListName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playlistClickListener != null) {
                        playlistClickListener.onPlaylistClick(playlist);
                    }
                }
            });
        }

        public void bind(Playlist t) {
            this.playlist = t;
            textView.setText(t.getName());
        }

    }
}
