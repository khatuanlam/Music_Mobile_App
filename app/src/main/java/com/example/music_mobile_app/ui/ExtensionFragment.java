package com.example.music_mobile_app.ui;

import static android.view.View.GONE;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.music_mobile_app.R;

import com.example.music_mobile_app.util.HandleBackground;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExtensionFragment extends Fragment {
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "INTERSTITIAL AD";

    private Spinner spinner;
    private Button button;
    private FrameLayout content_container;
    private Drawable backgroundDrawable;

    public ExtensionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                // mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        spinner = view.findViewById(R.id.download_spinner_filter);
        button = view.findViewById(R.id.download_testLoadAds);

        //Kiểm tra nếu avatar header bị ẩn => set hiện
        CircleImageView header = getParentFragment().getView().findViewById(R.id.avt);
        if(header.getVisibility()==GONE){
            header.setVisibility(View.VISIBLE);
        }

        //get background framelayout
        content_container = view.findViewById(R.id.content_container);
        backgroundDrawable = content_container.getBackground();

        handleBackground();

        String[] options = { "Mới nhất", "Cũ nhất" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                // Toast.makeText(requireContext(), selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) {
                    mInterstitialAd.show((Activity) requireContext());
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                }
            }
        });
        return view;
    }
    private  void handleBackground(){
        // Lấy giá trị màu integer từ tài nguyên màu
        int startColorInt = ContextCompat.getColor(getContext(), R.color.teal_700);
        // Chuyển đổi giá trị màu integer thành mã hex
        String startColorHex = String.format("#%06X", (0xFFFFFFFF & startColorInt)); // Bỏ đi hai ký tự đầu tiên (alpha channel)

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

}