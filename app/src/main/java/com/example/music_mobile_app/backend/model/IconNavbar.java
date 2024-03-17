package com.example.music_mobile_app.backend.model;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IconNavbar {
    private Button button;
    private View view;

    public IconNavbar(Button button, View view, TextView textView, Drawable drawable) {
        this.button = button;
        this.view = view;
        this.textView = textView;
        this.drawable = drawable;
    }

    private TextView textView;

    private Drawable drawable;


    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
