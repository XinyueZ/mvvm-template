package com.template.mvvm.base.ext.android.app

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.Size
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.template.mvvm.base.ext.android.view.setUpGoldenRatioHeight
import com.template.mvvm.base.ext.android.view.setUpGoldenRatioInvertedWidth
import com.template.mvvm.base.ext.android.view.setUpGoldenRatioWidth
import kotlin.reflect.KClass

fun Activity?.setViewGoldenRatioHeight(view: View): Activity? {
    this?.apply {
        view.setUpGoldenRatioHeight(this)
    }
    return this
}

fun Activity?.setUpGoldenRatioInvertedHeight(view: View): Activity? {
    this?.apply {
        view.setUpGoldenRatioHeight(this)
    }
    return this
}

fun Activity?.setViewGoldenRatioWidth(view: View): Activity? {
    this?.apply {
        view.setUpGoldenRatioWidth(this)
    }
    return this
}

fun Activity?.setUpGoldenRatioInvertedWidth(view: View): Activity? {
    this?.apply {
        view.setUpGoldenRatioInvertedWidth(this)
    }
    return this
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
    args: Bundle? = null,
    options: ActivityOptionsCompat? = null
) =
    context?.run {
        with(Intent(this, this@showSingleTopActivity.java)) {
            if (args != null)
                putExtras(args)
            flags = FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(
                this@run,
                this,
                if (options != null) options.toBundle() else EMPTY
            )
        }
    }

fun <T : Any> Activity?.getExtras(key: String): T? = this?.let {
    intent.extras[key] as? T
} ?: kotlin.run {
    null
}

fun Activity?.createActivityOptions(view: View?, sharedName: String): ActivityOptionsCompat? {
    return if (this == null || view == null) null
    else {
        ViewCompat.setTransitionName(
            view,
            sharedName
        )
        ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            view,
            sharedName
        )
    }
}