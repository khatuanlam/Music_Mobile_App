package Adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;

import java.util.List;

import com.example.music_mobile_app.model.Song;
import com.example.music_mobile_app.AddToPlaylistActivity;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> songList;
    private Context context;

    public SongAdapter() {

    }

    public SongAdapter(List<Song> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, final int position) {
        Song song = songList.get(position);
        holder.textViewPosition.setText(String.valueOf(song.getId()));
        holder.imageSong.setImageResource(song.getSongImage());
        holder.songTitle.setText(song.getTitle());

        holder.overflowButton.setOnClickListener((View v) -> {
            int adapterPosition = holder.getAbsoluteAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showOverflowMenu(v, adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }


    private void showOverflowMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.threedot); // Thay đổi thành menu của bạn

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Xử lý các sự kiện menu ở đây
                switch (item.getItemId()) {

                    //================================================================
//                    case R.id.add_list:
//                        // Mở giao diện add_to_playlist khi mục menu được chọn
//                        Intent intent = new Intent(context, AddToPlaylistActivity.class);
//                        context.startActivity(intent);
//                        Log.d("SongAdapter", "Add to playlist clicked");
//                        return true;

                    case R.id.add_list:
                        // Mở giao diện add_to_playlist khi mục menu được chọn
                        Intent intent = new Intent(context, AddToPlaylistActivity.class);
                        // Gửi vị trí của mục nhạc được chọn đến AddToPlaylistActivity
                        intent.putExtra("selectedSongPosition", position);
                        context.startActivity(intent);
                        return true;
                    //================================================================


                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSong;
        TextView textViewPosition;
        TextView songTitle;
        ImageButton overflowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSong = itemView.findViewById(R.id.imageSong);
            textViewPosition = itemView.findViewById(R.id.textViewPosition);

            songTitle = itemView.findViewById(R.id.songTitle);
            overflowButton = itemView.findViewById(R.id.overflowSongmenu);

        }

    }

}
