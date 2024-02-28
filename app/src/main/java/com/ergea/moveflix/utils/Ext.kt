package com.ergea.moveflix.utils

import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar


fun View.showSnackBar(text: String) =
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
        .show()

fun View.show(visible: Boolean) {
    this.isVisible = visible
}