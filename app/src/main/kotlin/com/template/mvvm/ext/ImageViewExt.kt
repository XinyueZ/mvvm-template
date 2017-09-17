package com.template.mvvm.ext

import android.net.Uri
import android.widget.ImageView
import com.template.mvvm.GlideApp

fun ImageView.loadRemoteImage(uri: Uri) {
    if (uri == Uri.EMPTY) return
    GlideApp.with(this).load(uri).into(this)
}