package com.template.mvvm.base.ext.android.app

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.support.annotation.IdRes
import android.support.annotation.Size
import android.support.v4.app.ActivityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.template.mvvm.base.ext.android.view.setUpGoldenRatioHeight
import kotlin.reflect.KClass

fun Activity?.setViewGoldenRatioHeight(view: View) {
    this?.apply {
        view.setUpGoldenRatioHeight(this)
    }
}

fun Activity?.setUpActionBar(@IdRes toolbar: Toolbar, action: (ActionBar.() -> Unit)? = null) {
    when (this is AppCompatActivity) {
        true -> {
            (this as AppCompatActivity).run {
                setSupportActionBar(toolbar)
                supportActionBar?.run {
                    action?.let { it.invoke(this) }
                }
            }
        }
    }
}

@Size
fun Activity.getScreenSize() = com.template.mvvm.base.utils.getScreenSize(this, 0)

fun <E : Activity, T : KClass<out E>> T.showSingleTopActivity(
    context: E?,
    args: Bundle? = null
) =
    context?.run {
        with(Intent(this, this@showSingleTopActivity.java)) {
            if (args != null)
                putExtras(args)
            flags = FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(this@run, this, EMPTY)
        }
    }

fun <T : Any> Activity?.getExtras(key: String): T? = this?.let {
    intent.extras[key] as? T
} ?: kotlin.run {
    null
}