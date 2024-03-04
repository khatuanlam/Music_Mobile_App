package com.example.music_mobile_app.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.music_mobile_app.R;

public class MainFragment extends Fragment {

    private static final String TAG = "Spotify MainFragment";

    private FragmentManager manager;

    private TextView homeText;
    private TextView favoriteText;
    private TextView searchText;
    private TextView downloadText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button homeLayout = view.findViewById(R.id.nav_home);
        Button favoriteLayout = view.findViewById(R.id.nav_favorite);
        Button searchLayout = view.findViewById(R.id.nav_search);
        Button downloadLayout = view.findViewById(R.id.nav_download);

        homeText = view.findViewById(R.id.nav_home_text);
        favoriteText = view.findViewById(R.id.nav_favorite_text);
        searchText = view.findViewById(R.id.nav_search_text);
        downloadText = view.findViewById(R.id.nav_download_text);

        homeLayout.setOnClickListener(mListener);
        favoriteLayout.setOnClickListener(mListener);
        searchLayout.setOnClickListener(mListener);
        downloadLayout.setOnClickListener(mListener);

        return view;
    }

    View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.nav_home:
                    Log.d(TAG, "HOME");
                    if (view.isActivated()) break;
                    manager.beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
                    break;
                case R.id.nav_favorite:
                    Log.d(TAG, "FAVORITE");
                    manager.beginTransaction().replace(R.id.fragment, new FavoriteFragment()).commit();
                    break;
                case R.id.nav_search:
                    Log.d(TAG, "SEARCH");
                    manager.beginTransaction().replace(R.id.fragment, new SearchFragment()).commit();
                    break;
                case R.id.nav_download:
                    Log.d(TAG, "DOWNLOAD");
                    manager.beginTransaction().replace(R.id.fragment, new DownloadFragment()).commit();
                    break;
            }
        }
    };

    void actionTransaction(Fragment fragment, int nav_icon) {
        manager.beginTransaction().replace(R.id.fragment, fragment).commit();
    }


    Drawable home;
    Drawable favorite;
    Drawable search;

    Drawable download;
    int focusMode;
    int defocusMode;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        home = getResources().getDrawable(R.drawable.ic_home_black_24dp, null);
        favorite = getResources().getDrawable(R.drawable.ic_like_black_24dp, null);
        search = getResources().getDrawable(R.drawable.ic_search_black_24dp, null);
        download = getResources().getDrawable(R.drawable.ic_download_black_24dp, null);

        focusMode = getResources().getColor(R.color.colorWhite, null);
        defocusMode = getResources().getColor(R.color.colorNavIcon, null);
    }
}