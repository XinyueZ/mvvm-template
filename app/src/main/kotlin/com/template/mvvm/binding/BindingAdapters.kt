package com.template.mvvm.common

import android.app.Activity
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.template.mvvm.ext.loadRemoteImage
import com.template.mvvm.home.HomeActivity

@BindingAdapter("remoteImageUri")
fun setRemoteImage(view: ImageView, uri: Uri?) {
    uri?.let {
        view.loadRemoteImage(uri)
    }
}

@BindingAdapter("onIndicatorClick")
fun toolbarOnIndicatorClick(toolbar: Toolbar, l: OnIndicatorClickListener?) {
    l?.let {
        toolbar.setNavigationOnClickListener { l.onIndicatorClick() }
    }
}

@BindingAdapter("goBack")
fun goBack(view: View, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("command")
fun cmdNaviHandler(view: NavigationView, l: OnCommandListener?) {
    l?.let {
        view.setNavigationItemSelectedListener {
            it.isChecked = true
            l.onCommand(it.itemId)

            true
        }
    }
}

@BindingAdapter("command")
fun cmdNaviHandler(view: BottomNavigationView, l: OnCommandListener?) {
    l?.let {
        view.setOnNavigationItemSelectedListener {
            l.onCommand(it.itemId)

            true
        }
    }
}

@BindingAdapter("startHome")
fun startHome(view: ImageView, startHome: Boolean) {
    when (startHome) {
        true -> {
            with(view.context as Activity) {
                HomeActivity.showInstance(this)
                finish()
            }
        }
    }
}

@BindingAdapter("dataLoaded")
fun setLoaded(view: View, loaded: Boolean) {
    view.visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}