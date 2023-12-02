package com.omouravictor.br_inco.util

import android.app.Activity
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

object SystemServiceUtils {

    fun hideKeyboard(activity: Activity, view: View) {
        view.clearFocus()
        WindowCompat.getInsetsController(activity.window, view).hide(WindowInsetsCompat.Type.ime())
    }

    fun showKeyboard(activity: Activity, view: View) {
        view.requestFocus()
        WindowCompat.getInsetsController(activity.window, view).show(WindowInsetsCompat.Type.ime())
    }

}