package com.example.music_mobile_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.ui.FavoriteFragment;
import com.example.music_mobile_app.ui.PlaylistFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ItemHorizontalAdapter extends RecyclerView.Adapter<ItemHorizontalAdapter.ItemHorizontalHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<Track> trackList;
    private List<PlaylistSimple> playlistList;
    private Context context;
    private Fragment fragment;
    private Album mAlbum;
    private int selectedItem = RecyclerView.NO_POSITION;
    private int type = 0;
    private boolean isSend = false;
    private PlaylistSimple mPlaylistTrack;
    private String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    public ItemHorizontalAdapter(List<Track> trackList, Album album, List<PlaylistSimple> playlistList, Context context,
                                 Fragment fragment) {
        this.trackList = trackList;
        this.playlistList = playlistList;
        this.context = context;
        this.fragment = fragment;
        this.mAlbum = album != null ? album : new Album();
    }

    @NonNull
    @Override
    public ItemHorizontalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_horizontal, parent, false);
        return new ItemHorizontalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHorizontalHolder holder, int position) {
        if (!playlistList.isEmpty() && position < playlistList.size()) {
            holder.bindPlaylist(playlistList.get(position));
        } else if (!trackList.isEmpty() && position < trackList.size()) {
            holder.bindTrack(trackList.get(position));
        }
        holder.itemView.setBackgroundColor(
                position == selectedItem ? ContextCompat.getColor(context, R.color.purple_50) : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        if (trackList.isEmpty()) {
            type = 1;
            return playlistList.size();
        } else {
            return trackList.size();
        }
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public void setPlaylistTrack(PlaylistSimple playlistTrack) {
        if (playlistTrack != null) {
            this.mPlaylistTrack = playlistTrack;
        } else {
            Log.e(TAG, "Plalist's Track is null");
        }
    }


    protected class ItemHorizontalHolder extends RecyclerView.ViewHolder {
        private Track mTrack;
        private PlaylistSimple mPlaylist;
        private ImageView item_image;
        private TextView item_name;
        private TextView item_artist;
        private ImageButton options;
        private PopupMenu optionMenu;

        public ItemHorizontalHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.horizontal_item_image);
            item_name = itemView.findViewById(R.id.horizontal_item_name);
            item_artist = itemView.findViewById(R.id.horizontal_item_artist);
            options = itemView.findViewById(R.id.options);
            // Sự kiện cho Spinner
            if (type == 1) {
                options.setVisibility(View.GONE);
            } else {
                options.setOnClickListener(v -> {
                    // Khởi tạo PopupMenu trong constructor
                    optionMenu = new PopupMenu(fragment.getContext(), options);
                    optionMenu.getMenuInflater().inflate(R.menu.threedot, optionMenu.getMenu());
                    optionMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.deletedItem:
                                // Call from favorite
                                if (fragment instanceof FavoriteFragment) {
                                    MethodsManager.getInstance().removeFromFavorite(mTrack.id,
                                            new ListenerManager.OnGetCompleteListener() {
                                                @Override
                                                public void onComplete(boolean type) {
                                                    Log.d(TAG, "Delete complete");
                                                    // Reload favorite
                                                    int position = getAbsoluteAdapterPosition();
                                                    if (position != RecyclerView.NO_POSITION) {
                                                        removeItem(position);
                                                        MethodsManager.getInstance().getUserFavorite(true, new MethodsManager.OnFavoriteTracksLoadedListener() {
                                                            @Override
                                                            public void onFavoriteTracksLoaded(List<Track> trackList) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onError(Throwable error) {

                                                }
                                            });
                                } else if (fragment instanceof PlaylistFragment) {
                                    // Call from playlist
                                    MethodsManager.getInstance().showRemoveDialog(mPlaylistTrack.id, mTrack.uri,
                                            fragment, new ListenerManager.OnGetCompleteListener() {
                                                @Override
                                                public void onComplete(boolean type) {
                                                    // Reload favorite
                                                    int position = getAbsoluteAdapterPosition();
                                                    if (position != RecyclerView.NO_POSITION) {
                                                        removeItem(position);
                                                        MethodsManager.getInstance().getUserFavorite(true, new MethodsManager.OnFavoriteTracksLoadedListener() {
                                                            @Override
                                                            public void onFavoriteTracksLoaded(List<Track> trackList) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onError(Throwable error) {

                                                }
                                            });
                                } else {
                                    int position = getAbsoluteAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        removeItem(position);
//                                        Toast.makeText(fragment.getContext(), "Delete Complete", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                return true;
                            case R.id.add_list:
                                // Xử lý khi mục add_list được chọn
                                MethodsManager.getInstance().showAddToPlaylist(fragment.getActivity(), mTrack);
                                return true;

                            case R.id.add_to_fav:
                                MethodsManager.getInstance().addToFavorite(mTrack.id,
                                        new ListenerManager.OnGetCompleteListener() {
                                            @Override
                                            public void onComplete(boolean type) {
                                                Log.d(TAG, "Adding complete");
                                                int position = getAbsoluteAdapterPosition();
                                                if (position != RecyclerView.NO_POSITION) {
                                                    // Reload favorite
                                                    MethodsManager.getInstance().getUserFavorite(true, new MethodsManager.OnFavoriteTracksLoadedListener() {
                                                        @Override
                                                        public void onFavoriteTracksLoaded(List<Track> trackList) {
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable error) {

                                            }
                                        });
                                return true;
                            default:
                                return false;
                        }
                    });
                    optionMenu.show();
                });
            }

            itemView.setOnClickListener(v -> {
                if (type == 0 && !isSend) {
                    Intent intent = new Intent(fragment.getActivity(), PlayTrackActivity.class);
                    intent.putExtra("Track", mTrack);
                    intent.putExtra("Track's Album", mAlbum);
                    intent.setAction("Play Track");
                    fragment.getContext().startActivity(intent);
                } else if (isSend) {
                    selectedItem = getAbsoluteAdapterPosition();
                    EventBus.getDefault().post(mPlaylist);
                    notifyDataSetChanged();
                } else {
                    MethodsManager.getInstance().getPlayListTrack(mPlaylist.id, context,
                            new ListenerManager.ListTrackOnCompleteListener() {
                                @Override
                                public void onComplete(List<Track> trackList) {
                                    sendDetailPlaylist(trackList, mPlaylist);
                                }

                                @Override
                                public void onError(Throwable error) {
                                    Log.e(TAG, "Cannot not get " + mPlaylist.name);
                                }
                            });
                }
            });
        }

        public void bindPlaylist(final PlaylistSimple playlist) {
            this.mPlaylist = playlist;
            item_name.setText(playlist.name);
            item_artist.setText(playlist.owner.display_name);
            if (playlist.images != null) {
                baseImage = playlist.images.get(0).url;
            }
            Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);

        }

        public void bindTrack(final Track track) {
            this.mTrack = track;
            item_name.setText(track.name);
            item_artist.setText(track.artists.get(0).name);
            if (track.album != null) {
                baseImage = track.album.images.get(0).url;
            } else {
                baseImage = mAlbum.images.get(0).url;
            }
            Glide.with(context).load(baseImage).override(Target.SIZE_ORIGINAL).into(item_image);

        }

        private void sendDetailPlaylist(List<Track> trackList, PlaylistSimple playlist) {
            FragmentManager manager = fragment.getParentFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putParcelable("PlaylistDetail", playlist);
            bundle.putParcelableArrayList("ListTrack", new ArrayList<>(trackList));
            PlaylistFragment playlistFragment = new PlaylistFragment();
            playlistFragment.setArguments(bundle);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.fragment, playlistFragment).commit();
        }

        // Phương thức để xóa một phần tử từ danh sách
        public void removeItem(int position) {
            if (position >= 0 && position < trackList.size()) {
                trackList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        }



    }
}
