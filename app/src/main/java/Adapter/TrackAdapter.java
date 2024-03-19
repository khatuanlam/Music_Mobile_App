package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.model.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private List<Track> trackList;
    private Context context;

    public TrackAdapter(List<Track> trackList, FragmentActivity activity) {

    }

    public TrackAdapter(List<Track> trackList,Context context) {
        this.trackList = trackList;
        this.context = context;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TrackAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        if (trackList != null) {
            Track track = trackList.get(position);
            holder.trackName.setText(track.getName());
            holder.artistName.setText(track.getArtists().get(1).getName());
            String imageurl = track.getAlbum().getImageUrl().get(0).getUrl();
            Picasso.get().load(imageurl).into(holder.trackImage);

        }
    }

    @Override
    public int getItemCount() {
        return trackList != null ? trackList.size() : 0;
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView trackName;
        TextView artistName;
        ImageView trackImage;
        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.songTitle);
            artistName = itemView.findViewById(R.id.artistname);
            trackImage = itemView.findViewById(R.id.TrackImageSong);
        }
    }
}
