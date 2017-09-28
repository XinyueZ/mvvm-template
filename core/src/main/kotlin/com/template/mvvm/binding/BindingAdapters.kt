package com.template.mvvm.binding

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import com.template.mvvm.GlideApp
import com.template.mvvm.R

@BindingAdapter("remoteImageUri")
fun a(view: View, uri: Uri?) {
    uri?.let {
        if (view is ImageView)
            GlideApp.with(view).load(uri).into(view)
    }
}

@BindingAdapter("goBack")
fun b(view: View, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("dataLoaded")
fun c(view: View, loaded: Boolean) {
    view.visibility = if (loaded) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("command")
fun d(view: NavigationView, l: OnCommandListener) {
    view.setNavigationItemSelectedListener {
        it.isChecked = true
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter("command")
fun e(toolbar: Toolbar, l: OnCommandListener) {
    toolbar.setNavigationOnClickListener { l.onCommand(R.id.action_hamburg) }
}

@BindingAdapter("command")
fun f(view: BottomNavigationView, l: OnCommandListener) {
    view.setOnNavigationItemSelectedListener {
        l.onCommand(it.itemId)

        true
    }
}

@BindingAdapter(value = *arrayOf("command", "vm"), requireAll = true)
fun g(view: View, l: OnItemCommandListener, vm: ViewModel) {
    view.setOnClickListener {
        l.onCommand(vm)
    }
}

