package com.example.music_mobile_app.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
    private TextView searchText;
    private TextView browseText;
    private TextView radioText;
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
        Button browseLayout = view.findViewById(R.id.nav_browse);
        Button searchLayout = view.findViewById(R.id.nav_search);
        Button radioLayout = view.findViewById(R.id.nav_radio);
        Button libraryLayout = view.findViewById(R.id.nav_library);

        homeText = view.findViewById(R.id.nav_home_text);
        browseText = view.findViewById(R.id.nav_browse_text);
        searchText = view.findViewById(R.id.nav_search_text);
        radioText = view.findViewById(R.id.nav_radio_text);
        downloadText = view.findViewById(R.id.nav_library_text);

        homeLayout.setOnClickListener(mListener);
        browseLayout.setOnClickListener(mListener);
        searchLayout.setOnClickListener(mListener);
        radioLayout.setOnClickListener(mListener);
        libraryLayout.setOnClickListener(mListener);

        return view;
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };
    Drawable home;
    Drawable browse;
    Drawable search;
    Drawable radio;
    Drawable library;
    int focusMode;
    int defocusMode;
    
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        home = getResources().getDrawable(R.drawable.ic_home_black_24dp, null);
        browse = getResources().getDrawable(R.drawable.ic_like_black_24dp, null);
        search = getResources().getDrawable(R.drawable.ic_search_black_24dp, null);
        radio = getResources().getDrawable(R.drawable.ic_radio_black_24dp, null);
        library = getResources().getDrawable(R.drawable.ic_library_music_black_24dp, null);

        focusMode = getResources().getColor(R.color.colorWhite, null);
        defocusMode = getResources().getColor(R.color.colorNavIcon, null);
    }
}