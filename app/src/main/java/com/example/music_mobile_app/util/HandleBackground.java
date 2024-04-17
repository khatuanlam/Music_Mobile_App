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
    private String hexColor;
    private ImageView image;
    private Drawable backgroundDrawable ;
    private GradientDrawable gradientDrawable ;
    private  int backgroundColor;


    public void handleBackground(ImageView image, Drawable backgroundDrawable, OnPaletteGeneratedListener listener )
    {
        this.image = image;
        this.backgroundDrawable = backgroundDrawable;
        generateColorAlbumImage(listener);
    }

    public void handleBackground(String hexColor, Drawable backgroundDrawable, OnPaletteGeneratedListener listener)
    {
        this.hexColor = hexColor;
        this.backgroundDrawable = backgroundDrawable;
        updateStartColor(listener);
    }

    private void generateColorAlbumImage(final OnPaletteGeneratedListener listener) {
        // Trích xuất Bitmap từ ImageView
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        // Sử dụng Palette để trích xuất màu từ Bitmap
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // Lấy màu chủ đạo từ Palette
                Palette.Swatch LightMutedSwatch = palette.getLightMutedSwatch();
                Palette.Swatch LightVibrantSwatch = palette.getLightVibrantSwatch();

                if (LightMutedSwatch != null) {
                    backgroundColor = LightMutedSwatch.getRgb();
                }
                else{
                    backgroundColor = LightVibrantSwatch.getRgb();
                }
                // Convert the RGB color to hexadecimal
                hexColor = String.format("#%06X", (0xFFFFFFFF & backgroundColor));
                System.out.println("Vibrant Swatch Color: " + hexColor);
                // Bạn có thể sử dụng màu dominantColor ở đây
                // Ví dụ: setBackgroundColor, setTextColor, v.v.
                updateStartColor(listener);

            }
        });
    }


    private GradientDrawable updateStartColor(final OnPaletteGeneratedListener listener) {

        // Convert hexColor to RGB integer
        int newStartColor = Color.parseColor(hexColor);

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
        // Notify the listener that the Palette is ready
        if (listener != null) {
            listener.onPaletteGenerated(gradientDrawable);
        }
        return gradientDrawable;
    }
    public interface OnPaletteGeneratedListener {
        void onPaletteGenerated(GradientDrawable gradientDrawable);
    }
}

