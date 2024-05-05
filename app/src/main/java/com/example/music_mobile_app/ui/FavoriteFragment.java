package com.example.music_mobile_app.ui;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_mobile_app.PlayTrackActivity;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.models.Track;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerview;
    private FrameLayout frame_container;
    private Drawable backgroundDrawable;
    private ProgressBar loadingProgressBar;

    private boolean loading = true;

    private TextView quantity;

    private Button btn_play;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        // Kiểm tra nếu avatar header bị ẩn => set hiện
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        if (header.getVisibility() == GONE) {
            header.setVisibility(View.VISIBLE);
        }

        prepareData(view);
        initView();

        handleBackground();
        // Loading đợi xử lý data
        handleLoading();


        btn_play.setOnClickListener(v -> {
            Intent intent = new Intent(this.getActivity(), PlayTrackActivity.class);
            intent.putParcelableArrayListExtra("ListTrack", (ArrayList<Track>) ListManager.getInstance().getFavoriteTracks());
            intent.setAction("Play Favorite");
            this.startActivity(intent);
        });

        return view;
    }

    private void handleLoading() {
        if (loading) {
            // Show loading progress bar
            loadingProgressBar.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        } else {
            // Show loading progress bar
            loadingProgressBar.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
        }
    }

    private void handleBackground() {
        // Lấy giá trị màu integer từ tài nguyên màu
        int startColorInt = ContextCompat.getColor(getContext(), R.color.purple_700);
        // Chuyển đổi giá trị màu integer thành mã hex
        String startColorHex = String.format("#%06X", (0xFFFFFFFF & startColorInt)); // Bỏ đi hai ký tự đầu tiên (alpha
        // channel)

        // Xử lý background
        HandleBackground backgroundHandler = new HandleBackground();
        backgroundHandler.handleBackground(startColorHex, backgroundDrawable,
                new HandleBackground.OnPaletteGeneratedListener() {
                    @Override
                    public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                        // Set the updated Drawable as the background of your view
                        frame_container.setBackground(updatedDrawable);
                    }
                });
    }

    private void prepareData(View view) {
        btn_play = view.findViewById(R.id.btn_play_favorite);
        quantity = view.findViewById(R.id.quantity);
        recyclerview = view.findViewById(R.id.favorite_recyclerview);
        frame_container = view.findViewById(R.id.frame_container);
        LinearLayoutManager favorite_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);
        recyclerview.setLayoutManager(favorite_layout);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

        // get background framelayout
        backgroundDrawable = frame_container.getBackground();
    }

    private void initView() {
        setFavoriteTracks();
        handleBackground();
    }

    // MaiThy - Update setFavoriteTracks xử lý set lại recyclerview khi
    // trackList.isEmpty
    // và loading khi đợi set recyclerview
    private void setFavoriteTracks() {
        List<Track> trackList = ListManager.getInstance().getFavoriteTracks();
        if (trackList.isEmpty()) {
            MethodsManager.getInstance().getUserFavorite(true, new MethodsManager.OnFavoriteTracksLoadedListener() {
                @Override
                public void onFavoriteTracksLoaded(List<Track> mTrackList) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTrackList.isEmpty()) {
                                Toast.makeText(getActivity(), "List is empty", Toast.LENGTH_SHORT).show();
                            } else {
                                ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(mTrackList, null,
                                        new ArrayList<>(), getContext(), FavoriteFragment.this);
                                adapter.notifyDataSetChanged();
                                quantity.setText(adapter.getItemCount() + " bài hát");
                                recyclerview.setAdapter(adapter);
                                loading = false;
                                handleLoading();
                                ListManager.getInstance().setFavoriteTracks(mTrackList);

                            }
                        }
                    });
                }
            });
        } else {
            ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(),
                    this);
            quantity.setText(adapter.getItemCount() + " bài hát");
            adapter.notifyDataSetChanged();
            recyclerview.setAdapter(adapter);
            loading = false;
            handleLoading();
        }

    }

}
