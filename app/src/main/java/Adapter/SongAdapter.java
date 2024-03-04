package Adapter;

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

import Models.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private List<Song> songList;
    private Context context;
    public SongAdapter() {

    }
    public SongAdapter(List<Song> songList,Context context){
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
        holder.imageView.setImageResource(song.getSongImage());
        holder.textViewTitle.setText(song.getTitle());

        holder.overflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    showOverflowMenu(v, adapterPosition);
                }
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

                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewPosition;
        TextView textViewTitle;
        ImageButton overflowButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewPosition = itemView.findViewById(R.id.textViewPosition);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            overflowButton = itemView.findViewById(R.id.overflowButton);
        }
    }

}
