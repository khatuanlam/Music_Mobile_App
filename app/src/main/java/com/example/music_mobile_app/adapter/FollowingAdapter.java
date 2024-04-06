package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.music_mobile_app.R;
import java.util.List;
import kaaes.spotify.webapi.android.models.Artist;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<Artist> mArtists;
    private Context mContext;

    public FollowingAdapter(List<Artist> artists, Context context) {
        this.mArtists = artists;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Artist artist = mArtists.get(position);
        holder.artistName.setText(artist.name);
        Glide.with(mContext).load(artist.images.get(0).url).into(holder.artistImage);
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView artistImage;
        public TextView artistName, artistNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.horizontal_item_image);
            artistName = itemView.findViewById(R.id.horizontal_item_name);
            artistNumber = itemView.findViewById(R.id.horizontal_item_artist);
        }
    }
}

