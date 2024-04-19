package com.example.music_mobile_app.ui;

import static android.view.View.GONE;

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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.adapter.ItemHorizontalAdapter;
import com.example.music_mobile_app.manager.ListManager;
import com.example.music_mobile_app.manager.MethodsManager;
import com.example.music_mobile_app.util.HandleBackground;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerview;
    private FrameLayout content_container;
    private Drawable backgroundDrawable;

    private TextView quantity;

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

        return view;
    }

    private void handleBackground() {
        // Lấy giá trị màu integer từ tài nguyên màu
        int startColorInt = ContextCompat.getColor(getContext(), R.color.purple_700);
        // Chuyển đổi giá trị màu integer thành mã hex
        String startColorHex = String.format("#%06X", (0xFFFFFFFF & startColorInt)); // Bỏ đi hai ký tự đầu tiên (alpha
        // channel)

        // Xử lý background
        HandleBackground backgroundHandler = new HandleBackground();
        backgroundHandler.handleBackground(startColorHex, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
            @Override
            public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                // Set the updated Drawable as the background of your view
                content_container.setBackground(updatedDrawable);
            }
        });
    }

    private void prepareData(View view) {
        quantity = view.findViewById(R.id.quantity);
        recyclerview = view.findViewById(R.id.favorite_recyclerview);
        content_container = view.findViewById(R.id.content_container);
        LinearLayoutManager favorite_layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(favorite_layout);

        // get background framelayout
        content_container = view.findViewById(R.id.content_container);
        backgroundDrawable = content_container.getBackground();
    }

    private void initView() {
        setFavoriteTracks(false);
        handleBackground();
    }

    private void setFavoriteTracks(boolean permission) {
        List<Track> trackList = ListManager.getInstance().getFavoriteTracks();
        if (trackList.isEmpty()) {
            // Nếu danh sách favorite chưa được lấy thì load lại để lấy
            MethodsManager.getInstance().getUserFavorite(true);
            Toast.makeText(getActivity(), "List is empty", Toast.LENGTH_SHORT).show();
        }
        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(trackList, null, new ArrayList<>(), getContext(), this);
        if (trackList != null) {
            quantity.setText(adapter.getItemCount() + " bài hát");
        }
        recyclerview.setAdapter(adapter);
    }

}
