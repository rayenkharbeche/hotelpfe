package com.example.hotelapplication.Adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.hotelapplication.R


object ImageLo {

    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(thumbs: ImageView, url: String?) {
        if (url != null) {
            Glide.with(thumbs)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(thumbs)
        }
    }
}