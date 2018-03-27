package com.template.mvvm.base.ext.android.app

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.template.mvvm.base.ext.android.content.res.hasIdentifier
import kotlin.reflect.KClass

fun Context.hasDrawableRes(@DrawableRes id: Int) = resources.hasIdentifier(id, "drawable", packageName)
fun Context.hasMenuRes(@DrawableRes id: Int) = resources.hasIdentifier(id, "menu", packageName)
fun Context.hasStringRes(@StringRes id: Int) = resources.hasIdentifier(id, "string", packageName)
fun Context.hasDimenRes(@DimenRes id: Int) = resources.hasIdentifier(id, "dimen", packageName)
fun Context.hasColorRes(@ColorRes id: Int) = resources.hasIdentifier(id, "color", packageName)

fun Context.getDimensionPixel(@DimenRes res: Int) = resources.getDimensionPixelSize(res)

fun Context.getDimension(@DimenRes res: Int) = resources.getDimension(res)

@SuppressLint("WrongConstant")
fun Context.showToast(toastText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Toast.makeText(this, toastText, timeLength).show()
}

fun Context.setupToast(
    lifecycleOwner: LifecycleOwner,
    liveData: LiveData<String>, timeLength: Int = Toast.LENGTH_SHORT
) {
    liveData.observe(lifecycleOwner, Observer {
        it?.let { showToast(it, timeLength) }
    })
}

fun Context.isPreApi23() =
    android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M

fun Context.isPreApi24() =
    android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N

fun Context.isPreApi26() =
    android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O

fun <E : Context, T : KClass<out E>> T.showNewTaskActivity(
    context: E?,
    args: Bundle? = null
) =
    context?.run {
        with(Intent(this, this@showNewTaskActivity.java)) {
            if (args != null)
                putExtras(args)
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            ActivityCompat.startActivity(this@run, this, EMPTY)
        }
    }