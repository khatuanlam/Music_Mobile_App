package com.example.music_mobile_app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.music_mobile_app.R;
import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.model.IconNavbar;
import com.example.music_mobile_app.repository.sqlite.MusicDatabaseHelper;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainFragment extends Fragment {

    private static final String TAG = "Spotify MainFragment";

    private FragmentManager manager;
    private TextView homeText;
    private Button homeLayout;
    private TextView favoriteText;
    private Button favoriteLayout;
    private TextView searchText;
    private Button searchLayout;
    private TextView extentionText;
    private Button extensionLayout;
    private CircleImageView account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        prepareData(view);



        // Home
        manager.beginTransaction().replace(R.id.fragment, new HomeFragment()).commit();
        return view;
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Add animation here
            FragmentTransaction transaction = manager.beginTransaction().setCustomAnimations(0, 0);

            // All main fragment
            switch (view.getId()) {
                case R.id.nav_home:
                    Log.d(TAG, "HOME");
                    if (view.isActivated()) break;
                    transaction.replace(R.id.fragment, new HomeFragment()).commit();
                    current_view = new IconNavbar(homeLayout, view, homeText, home);
                    setFocusMode(current_view);
                    break;
                case R.id.nav_favorite:
                    Log.d(TAG, "FAVORITE");
                    if (view.isActivated()) break;
                    transaction.replace(R.id.fragment, new FavoriteFragment()).commit();
                    current_view = new IconNavbar(favoriteLayout, view, favoriteText, favorite);
                    setFocusMode(current_view);
                    break;
                case R.id.nav_search:
                    Log.d(TAG, "SEARCH");
                    if (view.isActivated()) break;
                    transaction.replace(R.id.fragment, new SearchFragment()).commit();
                    current_view = new IconNavbar(searchLayout, view, searchText, search);
                    setFocusMode(current_view);
                    break;
                case R.id.nav_extension:
                    Log.d(TAG, "EXTENSION");
                    if (view.isActivated()) break;
                    manager.beginTransaction().replace(R.id.fragment, new com.example.music_mobile_app.ui.mydatabase.MainFragment()).commit();
                    current_view = new IconNavbar(extensionLayout, view, extentionText, download);
                    setFocusMode(current_view);
                    break;
            }
        }
    };

    private void setDefocusMode(IconNavbar iconNavbar) {
        iconNavbar.getDrawable().setTint(defocusMode);
        iconNavbar.getView().setBackground(iconNavbar.getDrawable());
        iconNavbar.getTextView().setTextColor(defocusMode);
        iconNavbar.getTextView().setTypeface(Typeface.DEFAULT);
        iconNavbar.getTextView().setActivated(false);
    }

    private void setFocusMode(IconNavbar iconNavbar) {
        // Replace prev and current view
        if (prev_view != null) {
            setDefocusMode(prev_view);
        }
        prev_view = current_view;
        iconNavbar.getDrawable().setTint(focusMode);
        iconNavbar.getView().setBackground(iconNavbar.getDrawable());
        iconNavbar.getTextView().setTextColor(focusMode);
        iconNavbar.getTextView().setTypeface(Typeface.DEFAULT_BOLD);
        iconNavbar.getTextView().setActivated(true);
    }

    Drawable home;
    Drawable favorite;
    Drawable search;
    Drawable download;
    int focusMode;
    int defocusMode;
    static IconNavbar prev_view;
    static IconNavbar current_view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        home = getResources().getDrawable(R.drawable.ic_home_black_24dp, null);
        favorite = getResources().getDrawable(R.drawable.ic_like_black_24dp, null);
        search = getResources().getDrawable(R.drawable.ic_search_white_24dp, null);
        download = getResources().getDrawable(R.drawable.music_solid, null);

        focusMode = getResources().getColor(R.color.colorWhite, null);
        defocusMode = getResources().getColor(R.color.colorNavIcon, null);

    }

    private void prepareData(View view) {

        homeLayout = view.findViewById(R.id.nav_home);
        favoriteLayout = view.findViewById(R.id.nav_favorite);
        searchLayout = view.findViewById(R.id.nav_search);
        extensionLayout = view.findViewById(R.id.nav_extension);

        homeText = view.findViewById(R.id.nav_home_text);
        favoriteText = view.findViewById(R.id.nav_favorite_text);
        searchText = view.findViewById(R.id.nav_search_text);
        extentionText = view.findViewById(R.id.nav_extension_text);

        homeLayout.setOnClickListener(mListener);
        favoriteLayout.setOnClickListener(mListener);
        searchLayout.setOnClickListener(mListener);
        extensionLayout.setOnClickListener(mListener);

        // Setting avt img value
        account = view.findViewById(R.id.avt);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        Glide.with(this).load(sharedPreferences.getString("imageUrl", "")).override(Target.SIZE_ORIGINAL).into(account);

        account.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = manager.beginTransaction().setCustomAnimations(0, 0);
            fragmentTransaction.replace(R.id.fragment, new AccountFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

    }


}