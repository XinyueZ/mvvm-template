package com.template.mvvm.binding

import android.app.Activity
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.template.mvvm.GlideApp

@BindingAdapter("remoteImageUri")
fun remoteImageUrim(view: View, uri: Uri?) {
    uri?.let {
        if (view is ImageView)
            GlideApp.with(view).load(uri).into(view)
    }
}

@BindingAdapter("onIndicatorClick")
fun onIndicatorClickm(toolbar: Toolbar, l: OnIndicatorClickListener?) {
    l?.let {
        toolbar.setNavigationOnClickListener { l.onIndicatorClick() }
    }
}

@BindingAdapter("goBack")
fun goBackm(view: View, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("command")
fun commandm(view: NavigationView, l: OnCommandListener?) {
    l?.let {
        view.setNavigationItemSelectedListener {
            it.isChecked = true
            l.onCommand(it.itemId)

            true
        }
    }
}

@BindingAdapter("command")
fun commandm(view: BottomNavigationView, l: OnCommandListener?) {
    l?.let {
        view.setOnNavigationItemSelectedListener {
            l.onCommand(it.itemId)

            true
        }
    }
}

@BindingAdapter("dataLoaded")
fun dataLoadFinishedm(view: View, loaded: Boolean) {
    view.visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}