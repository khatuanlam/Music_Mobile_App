package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.AddSongPlaylistActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.PlaylistItem;

import java.util.List;
public class AccountAdapter extends ArrayAdapter<PlaylistItem> {

    private List<PlaylistItem> playlistItems;
    private LayoutInflater inflater;
    private Context context;
    private int selectedItem = -1;
    public AccountAdapter(Context context, List<PlaylistItem> playlistItems) {
        super(context, 0, playlistItems);
        this.playlistItems = playlistItems;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaylistAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_playlist, parent, false);
            viewHolder = new PlaylistAdapter.ViewHolder();
            viewHolder.playlistNameTextView = convertView.findViewById(R.id.list_item_name);
            viewHolder.playlistImageView = convertView.findViewById(R.id.list_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlaylistAdapter.ViewHolder) convertView.getTag();
        }
        PlaylistItem playlistItem = playlistItems.get(position);
        viewHolder.playlistNameTextView.setText(playlistItem.getName());
        // Hiển thị hình ảnh nếu có
        if (playlistItem.getImages() != null && !playlistItem.getImages().isEmpty()) {
            Glide.with(getContext())
                    .load(playlistItem.getImages().get(0).url)
                    .into(viewHolder.playlistImageView);
        }
        //Setting click item
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_playlist, parent, false);
            viewHolder = new PlaylistAdapter.ViewHolder();
            viewHolder.playlistNameTextView = convertView.findViewById(R.id.list_item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PlaylistAdapter.ViewHolder) convertView.getTag();
        }
        final PlaylistItem currentItem = getItem(position);
        // Đặt nội dung của item
        viewHolder.playlistNameTextView.setText(currentItem.getName());
        // Xác định màu nền cho item được chọn
        if (position == selectedItem) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_50));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        }
        // Xử lý sự kiện click cho mỗi item
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật vị trí của item được chọn
                selectedItem = position;
                // Thông báo rằng dữ liệu đã thay đổi để cập nhật giao diện người dùng
                notifyDataSetChanged();

                String playlistId = playlistItem.getId();
                // Chuyển sang AddSongPlaylistActivity và truyền playlistId
                Intent intent = new Intent(getContext(), AddSongPlaylistActivity.class);
                intent.putExtra("playlistId", playlistId);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }
    static class ViewHolder {
        TextView playlistNameTextView;
        ImageView playlistImageView;
    }
}
