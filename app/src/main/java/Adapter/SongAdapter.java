//package Adapter;
//
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.music_mobile_app.R;
//
//import java.util.List;
//
//import com.example.music_mobile_app.model.Song;
//
//import kaaes.spotify.webapi.android.models.PlaylistSimple;
//import kaaes.spotify.webapi.android.models.Track;
//import kaaes.spotify.webapi.android.models.TrackSimple;
//
//public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
//    private List<TrackSimple> songList;
//
//    private Context context;
//
//    public SongAdapter(List<PlaylistSimple> songList, FragmentActivity activity) {
//
//    }
//
//    public SongAdapter(List<TrackSimple> songList, Context context) {
//        this.songList = songList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, final int position) {
//
//        TrackSimple song = songList.get(position);
//        holder.bind(song);
////        holder.textViewPosition.setText(String.valueOf(song.getId()));
////        holder.imageSong.setImageResource(song.getSongImage());
////        holder.songTitle.setText(song.getTitle());
////        holder.artistName.setText((song.getArtistName()));
////        holder.overflowButton.setOnClickListener((View v) -> {
////            int adapterPosition = holder.getAdapterPosition();
////            if (adapterPosition != RecyclerView.NO_POSITION) {
////                showOverflowMenu(v, adapterPosition);
////            }
////        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return songList.size();
//    }
//
//
//    private void showOverflowMenu(View view, final int position) {
//        PopupMenu popupMenu = new PopupMenu(context, view);
//        popupMenu.inflate(R.menu.threedot); //
//
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                // Xử lý các sự kiện menu ở đây
//                switch (item.getItemId()) {
//
//                    default:
//                        return false;
//                }
//            }
//        });
//
//        popupMenu.show();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageSong;
//        TextView textViewPosition;
//        TextView songTitle;
//        ImageButton overflowButton;
//        TextView artistName;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageSong = itemView.findViewById(R.id.imageSong);
//            textViewPosition = itemView.findViewById(R.id.textViewPosition);
//            songTitle = itemView.findViewById(R.id.songTitle);
//            artistName = itemView.findViewById(R.id.artistname);
//            overflowButton = itemView.findViewById(R.id.overflowSongmenu);
//
//        }
//        public void bind(TrackSimple track){
//            textViewPosition.setText(track.track_number);
//            songTitle.setText(track.name);
//            artistName.setText(track.artists.toString());
//
//        }
//    }
//
//}
