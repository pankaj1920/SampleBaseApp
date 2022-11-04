package com.applaunch.appbase.utils_base

import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import com.applaunch.talent.utils.extensions.color


fun Activity.changeStatusBarColor(colorId: Int) {
    window.statusBarColor = this.resources.color(colorId)
}



fun Activity.forceLightTheme() {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}