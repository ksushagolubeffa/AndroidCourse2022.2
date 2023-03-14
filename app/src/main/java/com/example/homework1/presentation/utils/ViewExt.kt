package com.example.homework1.presentation.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    message: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT
) = Snackbar
    .make(this, message, duration)
    .show()

