package com.template.mvvm.binding

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.template.mvvm.GlideApp
import com.template.mvvm.LL
import com.template.mvvm.R

@BindingAdapter(value = *arrayOf("width", "height"), requireAll = true)
fun a(view: View, width: Int, height: Int) {
    view.layoutParams.width = width
    view.layoutParams.height = height
}


@BindingAdapter("stopLoading")
fun b(view: SwipeRefreshLayout, stopLoading: Boolean) {
    view.isRefreshing = !stopLoading
}


@BindingAdapter("reload")
fun c(view: SwipeRefreshLayout, l: OnReloadListener) {
    view.setOnRefreshListener {
        l.onReload()
    }
}

@BindingAdapter("remoteImageUri")
fun d(view: View, uri: Uri?) {
    uri?.let {
        if (view is ImageView)
            GlideApp.with(view).load(uri).into(view)
    }
}

@BindingAdapter("goBack")
fun e(view: View, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("dataLoaded")
fun f(view: View, loaded: Boolean) {
    view.visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("command")
fun g(view: NavigationView, l: OnCommandListener) {
    view.setNavigationItemSelectedListener {
        it.isChecked = true
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter("command")
fun h(toolbar: Toolbar, l: OnCommandListener) {
    toolbar.setNavigationOnClickListener { l.onCommand(R.id.action_app_bar_indicator) }
}

@BindingAdapter("command")
fun i(view: BottomNavigationView, l: OnCommandListener) {
    view.disableShiftMode()
    view.setOnNavigationItemSelectedListener {
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter(value = *arrayOf("command", "vm"), requireAll = true)
fun j(view: View, l: OnItemCommandListener, vm: ViewModel) {
    view.setOnClickListener {
        l.onCommand(vm)
    }
}

// Some view-ext

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        LL.e("Unable to get shift mode field", e)
    } catch (e: IllegalAccessException) {
        LL.e("Unable to change value of shift mode", e)
    }
}

