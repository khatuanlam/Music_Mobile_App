package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.PlaylistItem;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<Adapter.AccountAdapter.ViewHolder> {

        private List<PlaylistItem> playlistItems;

        public AccountAdapter(List<PlaylistItem> playlistItems) {
            this.playlistItems = playlistItems;
        }

        @NonNull
        @Override
        public Adapter.AccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
            return new Adapter.AccountAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.AccountAdapter.ViewHolder holder, int position) {
            PlaylistItem playlistItem = playlistItems.get(position);
            holder.playlistNameTextView.setText(playlistItem.getName());
            // Hiển thị hình ảnh nếu có
            if (playlistItem.getImages() != null && !playlistItem.getImages().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(playlistItem.getImages().get(0).url)
                        .into(holder.playlistImageView);
            }
        }

        @Override
        public int getItemCount() {
            return playlistItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView playlistNameTextView;
            ImageView playlistImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                playlistNameTextView = itemView.findViewById(R.id.list_item_name);
                playlistImageView = itemView.findViewById(R.id.list_item_image);
            }
        }
    }

