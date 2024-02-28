package com.ergea.moveflix.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ergea.moveflix.R
import com.google.android.material.snackbar.Snackbar

typealias Rid = R.id
typealias Rdrawable = R.drawable

fun View.showSnackBar(text: String) =
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .show()

fun View.show(visible: Boolean) {
    this.isVisible = visible
}

fun ImageView.loadImage(context: Context, url: String?) {
    Glide.with(context)
        .load(url)
        .placeholder(Rdrawable.baseline_image_not_supported_24)
        .error(Rdrawable.baseline_image_not_supported_24)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
fun String.commonImageUrl() = "https://image.tmdb.org/t/p/w500$this"