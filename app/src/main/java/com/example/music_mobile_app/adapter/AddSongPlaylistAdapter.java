package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.PlaylistItem;

import java.util.List;

public class AddSongPlaylistAdapter extends ArrayAdapter<PlaylistItem> {
    private List<PlaylistItem> songPlaylists;
    private LayoutInflater inflater;

    public AddSongPlaylistAdapter(Context context, List<PlaylistItem> songPlaylist) {
        super(context, 0, songPlaylist);
        this.songPlaylists = songPlaylist;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_playlist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.songNameTextView = convertView.findViewById(R.id.list_item_name);
            viewHolder.songImageView = convertView.findViewById(R.id.list_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lấy thông tin của một bài hát từ danh sách songPlaylists
        PlaylistItem song = songPlaylists.get(position);

        // Hiển thị thông tin bài hát trên giao diện
        viewHolder.songNameTextView.setText(song.getName());

        // Hiển thị hình ảnh nếu có
        if (song.getImages() != null && !song.getImages().isEmpty()) {
            Glide.with(getContext())
                    .load(song.getImages().get(0).url) // Sửa thành getUrl()
                    .into(viewHolder.songImageView);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView songNameTextView;
        ImageView songImageView;
    }
}