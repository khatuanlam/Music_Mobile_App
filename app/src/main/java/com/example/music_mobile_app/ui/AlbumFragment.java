package com.example.music_mobile_app.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.music_mobile_app.R;
import com.example.music_mobile_app.utils.HandleBackground;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    private ImageView albumImage;
    private FrameLayout frameLayout;
    private Drawable backgroundDrawable;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        // Find the ImageView within the fragment's layout
        albumImage = view.findViewById(R.id.albumImage);
        //get background framelayout
        frameLayout = view.findViewById(R.id.fragment_container);
        backgroundDrawable = frameLayout.getBackground();

        // Find the RecyclerView within the fragment's layout
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        // Set up RecyclerView (example)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter for RecyclerView (you need to implement your own adapter)
//        YourAdapter adapter = new YourAdapter(/* pass necessary parameters */);
//        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create an instance of HandleBackground and call handleBackground method
        HandleBackground backgroundHandler = new HandleBackground();
        backgroundHandler.handleBackground(albumImage, backgroundDrawable, new HandleBackground.OnPaletteGeneratedListener() {
            @Override
            public void onPaletteGenerated(GradientDrawable updatedDrawable) {
                // Set the updated Drawable as the background of your view
                frameLayout.setBackground(updatedDrawable);
            }
        });
    }





}