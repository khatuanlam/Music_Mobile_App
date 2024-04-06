package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.ui.FavoriteFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private List<Album> AlbumList;
    private Context context;
    private FavoriteFragment favoriteFragment;
    private boolean showDeleteMenuItem = false;
    public AlbumAdapter(List<Album> AlbumList, FragmentActivity activity) {

    }

    public AlbumAdapter(List<Album> AlbumList,Context context) {
        this.AlbumList = AlbumList;
        this.context = context;
        notifyDataSetChanged();
    }


    private ListenerManager.AlbumAdapterListener listener;

    public void setListener(ListenerManager.AlbumAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.liked_item, parent, false);
        return new AlbumViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        if (AlbumList != null) {
            Album Album = AlbumList.get(position);
            holder.AlbumName.setText(Album.getName());
            holder.artistName.setText(Album.getArtists().get(0).getName());


            if (Album != null && Album.getImageUrl() != null && !Album.getImageUrl().isEmpty()) {
                String imageUrl = Album.getImageUrl().get(0).getUrl();
                Picasso.get().load(imageUrl).into(holder.AlbumImage);
            } else {
                holder.AlbumImage.setVisibility(View.GONE);
            }

            // Set OnClickListener for overflow button
            holder.overflowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = holder.getBindingAdapterPosition();
                    if (clickedPosition != RecyclerView.NO_POSITION) {
                        showPopupMenu(clickedPosition, holder.overflowButton);
                    }
                }
            });
//            holder.likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }


    public void showPopupMenu(int position ,View view){
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.search_options_view_item_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete_from_favorite:
                        if (listener != null) {
                            listener.onDeleteAlbumClicked(AlbumList.get(position).getId());
                        }
                        return true;
                    default:
                        return false;
                }

            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return AlbumList != null ? AlbumList.size() : 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView TextViewPosition;
        TextView AlbumName;
        TextView artistName;
        ImageView AlbumImage;
        Button likeButton, overflowButton;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
//            TextViewPosition = itemView.findViewById(R.id.textViewPosition);
            AlbumName = itemView.findViewById(R.id.ItemTitleLiked);
            artistName = itemView.findViewById(R.id.ItemArtistLiked);
            AlbumImage = itemView.findViewById(R.id.ItemImage);
            overflowButton = itemView.findViewById(R.id.overflowAlbumMenu);
        }
    }
}
