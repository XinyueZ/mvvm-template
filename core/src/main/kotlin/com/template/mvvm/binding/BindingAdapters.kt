package com.template.mvvm.binding

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.template.mvvm.GlideApp
import com.template.mvvm.R

@BindingAdapter(value = *arrayOf("width", "height"), requireAll = true)
fun setViewSize_(view: View, width: Int, height: Int) {
    view.layoutParams.width = width
    view.layoutParams.height = height
}


@BindingAdapter("stopLoading")
fun stopLoading_(view: SwipeRefreshLayout, stopLoading: Boolean) {
    view.isRefreshing = !stopLoading
}


@BindingAdapter("reload")
fun swipeToReload_(view: SwipeRefreshLayout, l: OnReloadListener) {
    view.setOnRefreshListener {
        l.onReload()
    }
}

@BindingAdapter("remoteImageUri")
fun remoteImageUri_(view: View, uri: Uri?) {
    uri?.let {
        if (view is ImageView)
            GlideApp.with(view).load(uri).into(view)
    }
}

@BindingAdapter("goBack")
fun goBack_(view: View, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("dataLoaded")
fun dataLoaded_(view: View, loaded: Boolean) {
    view.visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("command")
fun command_(view: NavigationView, l: OnCommandListener) {
    view.setNavigationItemSelectedListener {
        it.isChecked = true
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter("command")
fun command_(toolbar: Toolbar, l: OnCommandListener) {
    toolbar.setNavigationOnClickListener { l.onCommand(R.id.action_app_bar_indicator) }
}

@BindingAdapter("command")
fun command_(view: BottomNavigationView, l: OnCommandListener) {
    view.setOnNavigationItemSelectedListener {
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter(value = *arrayOf("command", "vm"), requireAll = true)
fun command_(view: View, l: OnItemCommandListener, vm: ViewModel) {
    view.setOnClickListener {
        l.onCommand(vm)
    }
}

