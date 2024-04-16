package com.example.music_mobile_app.adapter;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.ListenerManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.ui.AlbumFragment;
import com.example.music_mobile_app.ui.SubSearchRecyclerViewFoundSongFragment;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Track;

public class SearchAlbumAdapter extends RecyclerView.Adapter<SearchAlbumAdapter.AlbumViewHolder> {
    private Fragment fragment;
    private List<AlbumSimple> mDataList;
    private FragmentManager fragmentManager;

    public SearchAlbumAdapter(List<AlbumSimple> albumList, SubSearchRecyclerViewFoundSongFragment subSearchRecyclerViewFoundSongFragment) {
    }

    public interface OnAlbumSelectedListener {
        void onAlbumSelected(AlbumSimple album);
    }
    public SearchAlbumAdapter(Fragment fragment, List<AlbumSimple> dataList) {
        this.fragment = fragment;
        mDataList = dataList;
        if (fragment.isAdded()) {
            this.fragmentManager = fragment.getChildFragmentManager(); // Khởi tạo FragmentManager
        }

    }


    public void setmDataList(List<AlbumSimple> mDataList) {
        this.mDataList = mDataList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_found_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        AlbumSimple albumSimple = mDataList.get(position);
        holder.bind(albumSimple);
        holder.imageView.setOnClickListener(v -> {
            AlbumFragment albumFragment = new AlbumFragment();
            Bundle args = new Bundle();
            args.putString("albumId", albumSimple.id);
            args.putString("albumName", albumSimple.name);
            albumFragment.setArguments(args);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.album_fragment_container, albumFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        public AlbumSimple mAlbum;
        public TextView textViewAlbumName;
        public TextView textViewAlbumType;
        public ImageView imageView;
        public CardView cardView;
        public ImageView optionsImageView;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            textViewAlbumName = itemView.findViewById(R.id.list_item_search_found_album_name);
            textViewAlbumType = itemView.findViewById(R.id.list_item_search_found_album_type);
            imageView = itemView.findViewById(R.id.list_item_search_found_album_image);
            cardView = itemView.findViewById(R.id.list_item_search_found_album_cardview);
            optionsImageView = itemView.findViewById(R.id.list_item_search_found_album_options);


//            itemView.setOnClickListener((View v) -> {
//            Intent intent = new Intent(fragment.getContext(), PlayTrackActivity.class);
//            intent.putExtra("Track's Album", mAlbum);
//            fragment.getActivity().startActivity(intent);});

            itemView.setOnClickListener(v -> {
                Log.e(TAG, "Album ID : " + mAlbum.id);
                MethodsManager.getInstance().getAlbum(mAlbum.id, new ListenerManager.AlbumCompleteListener() {
                    @Override
                    public void onComplete(Album album, List<Track> trackList) {
                        // Send detail album
                        sendDetailAlbum(album, trackList);
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "Cannot get detail album");
                    }
                });

            });
        }

        public void bind(AlbumSimple albumSimple) {
            this.mAlbum = albumSimple;
            textViewAlbumName.setText(albumSimple.name);
            textViewAlbumType.setText(albumSimple.type);
            if (albumSimple.images.size() > 0) {
                Glide.with(fragment)
                        .load(albumSimple.images.get(0).url)
                        .into(imageView);
            }

        }

        public void sendDetailAlbum(Album album, List<Track> trackList) {
            FragmentManager manager = fragment.getChildFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putParcelable("AlbumDetail", (Parcelable) mAlbum);
            bundle.putParcelable("Album", (Parcelable) album);
            bundle.putParcelableArrayList("ListTrack", new ArrayList<Parcelable>(trackList));
            AlbumFragment albumFragment = new AlbumFragment();
            albumFragment.setArguments(bundle);

//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.addToBackStack(null);
//            transaction.replace(R.id.search_fragment, albumFragment).commit();
        }
    }

}