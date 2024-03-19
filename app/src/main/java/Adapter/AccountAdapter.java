package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.PlaylistItem;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<PlaylistItem> {

        private List<PlaylistItem> playlistItems;
        private LayoutInflater inflater;

    public AccountAdapter(Context context, List<PlaylistItem> playlistItems) {
        super(context, 0, playlistItems);
        this.playlistItems = playlistItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_playlist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.playlistNameTextView = convertView.findViewById(R.id.list_item_name);
            viewHolder.playlistImageView = convertView.findViewById(R.id.list_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PlaylistItem playlistItem = playlistItems.get(position);
        viewHolder.playlistNameTextView.setText(playlistItem.getName());
        // Hiển thị hình ảnh nếu có
        if (playlistItem.getImages() != null && !playlistItem.getImages().isEmpty()) {
            Glide.with(getContext())
                    .load(playlistItem.getImages().get(0).url)
                    .into(viewHolder.playlistImageView);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView playlistNameTextView;
        ImageView playlistImageView;
    }
}