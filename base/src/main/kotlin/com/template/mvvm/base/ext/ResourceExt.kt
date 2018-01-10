package com.template.mvvm.base.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.util.*

fun <T : Drawable> T.bytesEqualTo(t: T?) = toBitmap().bytesEqualTo(t?.toBitmap())

fun <T : Drawable> T.pixelsEqualTo(t: T?) = toBitmap().pixelsEqualTo(t?.toBitmap())

fun Bitmap.bytesEqualTo(otherBitmap: Bitmap?) = otherBitmap?.let { other ->
    if (width == other.width && height == other.height)
        toBytes().contentEquals(other.toBytes())
    else false
} ?: kotlin.run { false }

fun Bitmap.pixelsEqualTo(otherBitmap: Bitmap?) = otherBitmap?.let { other ->
    if (width == other.width && height == other.height)
        Arrays.equals(toPixels(), other.toPixels())
    else false
} ?: kotlin.run { false }

fun <T : Drawable> T.toBitmap(): Bitmap {
    if (this is BitmapDrawable) return bitmap

    val drawable: Drawable = this
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun Bitmap.toBytes(): ByteArray = ByteArrayOutputStream().use { stream ->
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    stream.toByteArray()
}

fun Bitmap.toPixels() = IntArray(width * height).apply { getPixels(this, 0, width, 0, 0, width, height) }




