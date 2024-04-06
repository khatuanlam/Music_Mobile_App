package com.example.music_mobile_app.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.music_mobile_app.R;


public class FavoriteFragment extends Fragment {


    private RecyclerView favorite_recycleview;

    private TextView quantity;


    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //Hide header
//        RelativeLayout header = getParentFragment().getView().findViewById(R.id.header);
//        header.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);


        return view;
    }

    private void prepareData(View view) {
        quantity = view.findViewById(R.id.quantity);
        favorite_recycleview = view.findViewById(R.id.favorite_recycleview);

        
    }
}