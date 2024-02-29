package com.ergea.moveflix.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ergea.moveflix.R
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.glide.transformations.BlurTransformation
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

typealias Rid = R.id
typealias Rdrawable = R.drawable
const val TIME_ZONE_ID_JAKARTA = "Asia/Jakarta"
const val SERVER_DATE_FORMAT = "yyyy-MM-dd"
const val UI_DATE_FORMAT = "dd MMMM yyyy"

fun View.showSnackBar(text: String) =
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .show()

fun View.show(visible: Boolean) {
    this.isVisible = visible
}

@SuppressLint("CheckResult")
fun ImageView.loadImage(context: Context, url: String?, isBlur: Boolean = false) {
    Glide.with(context)
        .load(url).apply {
            if (isBlur) {
                apply(RequestOptions.bitmapTransform(BlurTransformation(25, 5)))
            }
        }
        .placeholder(Rdrawable.baseline_image_not_supported_24)
        .error(Rdrawable.baseline_image_not_supported_24)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun String.commonImageUrl() = "https://image.tmdb.org/t/p/w500$this"

fun String.commonYoutubeUrl() = "https://www.youtube.com/embed/$this"

@SuppressLint("ObsoleteSdkInt")
fun Activity.transparentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val w: Window = this.window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}

fun String?.toDateFormat(
    oldFormat: String = SERVER_DATE_FORMAT,
    newFormat: String = UI_DATE_FORMAT,
    isLocale: Boolean = false,
): String {
    if (this.isNullOrEmpty()) return ""
    val calendar = Calendar.getInstance()
    try {
        val dateTimeMillis =
            SimpleDateFormat(oldFormat, isLocaleDate(isLocale)).parse(this)?.time ?: 0L
        calendar.timeInMillis = dateTimeMillis
        calendar.timeZone = TimeZone.getTimeZone(TIME_ZONE_ID_JAKARTA)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return SimpleDateFormat(newFormat, isLocaleDate(isLocale)).format(calendar.time)
}

fun isLocaleDate(
    isLocale: Boolean,
): Locale {
    return if (isLocale) Locale("id", "ID")
    else Locale.ENGLISH
}