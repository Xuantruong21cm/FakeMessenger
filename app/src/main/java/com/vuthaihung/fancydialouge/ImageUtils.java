package com.vuthaihung.fancydialouge;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {

    public static void loadImage(ImageView imageView, String path) {
        Glide.with(imageView)
                .load(path)
                .placeholder(R.drawable.ic_user_default)
                .into(imageView);
    }

    public static void loadImageDefault(ImageView imageView, String path) {
        Glide.with(imageView)
                .load(path)
                .placeholder(R.drawable.bg_no_image)
                .into(imageView);
    }
}
