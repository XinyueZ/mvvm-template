package com.template.mvvm.base.ext.android.graphics.drawable

import android.graphics.drawable.Drawable
import com.template.mvvm.base.ext.android.graphics.bytesEqualTo
import com.template.mvvm.base.ext.android.graphics.pixelsEqualTo
import com.template.mvvm.base.ext.android.graphics.toBitmap

fun <T : Drawable> T.bytesEqualTo(t: T?) = toBitmap().bytesEqualTo(t?.toBitmap(), true)

fun <T : Drawable> T.pixelsEqualTo(t: T?) = toBitmap().pixelsEqualTo(t?.toBitmap(), true)

