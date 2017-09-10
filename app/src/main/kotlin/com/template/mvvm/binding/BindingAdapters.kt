package com.template.mvvm.common

import android.app.Activity
import android.databinding.BindingAdapter
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import com.example.android.architecture.blueprints.todoapp.util.showSnackbar
import com.template.mvvm.ext.showToast
import com.template.mvvm.home.HomeActivity

@BindingAdapter("drawerToggle")
fun drawerToggle(drawer: DrawerLayout, drawerToggle: Boolean) {
    if (drawerToggle) {
        with(drawer) {
            when (isDrawerOpen(GravityCompat.START)) {
                false -> openDrawer(GravityCompat.START)
                else -> closeDrawer(GravityCompat.START)
            }
        }
    }
}

@BindingAdapter("onIndicatorClick")
fun toolbarOnIndicatorClick(toolbar: Toolbar, l: OnIndicatorClickListener?) {
    l?.let {
        toolbar.setNavigationOnClickListener { l.onIndicatorClick() }
    }
}

@BindingAdapter("goBack")
fun goBack(view: ConstraintLayout, goBack: Boolean) {
    if (goBack) ActivityCompat.finishAfterTransition(view.context as Activity)
}

@BindingAdapter("command")
fun cmdNaviHandler(view: NavigationView, l: OnCommandListener?) {
    l?.let {
        view.setNavigationItemSelectedListener {
            it.isChecked = true
            l.onCommand(it.itemId)
            view.showSnackbar(
                    "cmdNaviHandler drawer"
            )
            view.context.applicationContext.showToast("cmdNaviHandler drawer")
            true
        }
    }
}

@BindingAdapter("command")
fun cmdNaviHandler(view: BottomNavigationView, l: OnCommandListener?) {
    l?.let {
        view.setOnNavigationItemSelectedListener {
            l.onCommand(it.itemId)
            view.showSnackbar(
                    "cmdNaviHandler bottom"
            )
            view.context.applicationContext.showToast("cmdNaviHandler bottom")
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