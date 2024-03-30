package com.example.music_mobile_app.ui.mydatabase;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.music_mobile_app.MainActivity;
import com.example.music_mobile_app.R;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SubSearchFragment extends Fragment {
    public SubSearchFragment() {
    }

    private EditText editText;
    private ImageView imageView;
    private FragmentManager manager;

    private SubSearchInformationFragment subSearchInformationFragment;
    private SubSearchRecyclerViewFoundSongFragment subSearchRecyclerViewFoundSongFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subSearchInformationFragment = new SubSearchInformationFragment();
        manager = getParentFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydb_fragment_sub_search, container, false);
        editText = view.findViewById(R.id.mydb_search_subSearchTextBox);
        imageView = view.findViewById(R.id.mydb_search_subSearchBack);
        subSearchInformationFragment = new SubSearchInformationFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.mydb_search_subSearchMainFragmentContainer, subSearchInformationFragment)
                .commit();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainFragment mainFragment = new MainFragment();
                manager.beginTransaction()
                        .replace(R.id.fragment, mainFragment)
                        .commit();

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (editable.toString().length() == 0) {
                            getChildFragmentManager().beginTransaction()
                                    .replace(R.id.mydb_search_subSearchMainFragmentContainer, subSearchInformationFragment)
                                    .commit();
                        } else {
//                            getTrack(editText.getText().toString());
                        }

                    }
                }, 1000);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}