package com.example.music_mobile_app.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import androidx.palette.graphics.Palette;


public class HandleBackground {
    // Generate palette synchronously and return it.
    ImageView image;
    Drawable backgroundDrawable ;
    GradientDrawable gradientDrawable ;

    public void handleBackground(ImageView image, Drawable backgroundDrawable, OnPaletteGeneratedListener listener )
    {
        this.image = image;
        this.backgroundDrawable = backgroundDrawable;
        generateColorAlbumImage(listener);
    }
    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }



    private void generateColorAlbumImage(final OnPaletteGeneratedListener listener) {
        // Trích xuất Bitmap từ ImageView
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        // Sử dụng Palette để trích xuất màu từ Bitmap
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // Lấy màu chủ đạo từ Palette
                Palette.Swatch darkMutedSwatch = palette.getLightMutedSwatch();

                if (darkMutedSwatch != null) {
                    int backgroundColor = darkMutedSwatch.getRgb();

                    // Convert the RGB color to hexadecimal
                    String hexColor = String.format("#%06X", (0xFFFFFF & backgroundColor));
                    System.out.println("Vibrant Swatch Color: " + hexColor);
                    // Bạn có thể sử dụng màu dominantColor ở đây
                    // Ví dụ: setBackgroundColor, setTextColor, v.v.
                    updateStartColor(hexColor);

                    // Notify the listener that the Palette is ready
                    if (listener != null) {
                        listener.onPaletteGenerated(gradientDrawable);
                    }
                }
            }
        });
    }


    private GradientDrawable updateStartColor(String hexColor) {

        // Convert hexColor to RGB integer
        int newStartColor = Color.parseColor(hexColor);

//        // Find the ImageView within the fragment's layout
//        FrameLayout frameLayout = getView().findViewById(R.id.fragment_container);

        // Get the existing background drawable
//        Drawable backgroundDrawable = frameLayout.getBackground();

        if (backgroundDrawable instanceof GradientDrawable) {
            gradientDrawable = (GradientDrawable) backgroundDrawable;

            // Get the current colors array from gradient drawable
            int[] colors = gradientDrawable.getColors();

            // Update the startColor and centerColor in the colors array
            if (colors.length >= 2) {
                colors[0] = newStartColor; // Update startColor
                colors[1] = android.R.color.black;// Update centerColor
                colors[2] = android.R.color.black;// Update endColor

                // Set the updated colors array to the gradient drawable
                gradientDrawable.setColors(colors);

            }

        }
        return gradientDrawable;
    }
    public interface OnPaletteGeneratedListener {
        void onPaletteGenerated(GradientDrawable gradientDrawable);
    }
}
