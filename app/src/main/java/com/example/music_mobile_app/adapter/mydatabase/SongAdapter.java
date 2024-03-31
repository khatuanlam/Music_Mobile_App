package com.example.music_mobile_app.adapter.mydatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private Fragment fragment;
    private List<Song> mDataList;
    private Context context;

    public SongAdapter(Context context, Fragment fragment, List<Song> dataList) {
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
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mydb_list_item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = mDataList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ImageView moreVert;

        public Song song;


        public SongViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.mydb_list_item_song_songNameTextView);
            imageView = itemView.findViewById(R.id.mydb_list_item_song_imageView);
            moreVert = itemView.findViewById(R.id.mydb_list_item_song_more);

            moreVert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });

        }
        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.mydb_list_item_song); // Assuming you have defined a menu resource file named song_menu.xml
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.mydb_menu_item_song_addFavorite:
                            Toast.makeText(context, song.getName() + "add favorite", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.mydb_menu_item_song_removeFavorite:
                            Toast.makeText(context, song.getName() + "remove favorite", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.mydb_menu_item_song_download:
                            Toast.makeText(context, song.getName() +"download favorite", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
        public void bind(Song song) {
            this.song = song;
            textView.setText(song.getName());
            if (song.getImage() != null && !song.getImage().isEmpty()) {
                Glide.with(fragment)
                        .load(song.getImage())
                        .into(imageView);
            }

        }
    }

}