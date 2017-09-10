package com.template.mvvm.home

import android.databinding.BindingAdapter
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import com.example.android.architecture.blueprints.todoapp.util.showSnackbar
import com.template.mvvm.ext.showToast

interface OnIndicatorClickListener {
    fun onIndicatorClick()
}

interface OnCommandListener {
    fun onCommand(id: Int)
}

@BindingAdapter("drawerToggle")
fun drawerStatus(drawer: DrawerLayout, drawerToggle: Boolean) {
    if (drawerToggle) {
        with(drawer) {
            when (isDrawerOpen(GravityCompat.START)) {
                false -> openDrawer(GravityCompat.START)
                else -> closeDrawer(GravityCompat.START)
            }
        }
    }
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

@BindingAdapter("onIndicatorClick")
fun toolbarOnIndicatorClick(toolbar: Toolbar, l: OnIndicatorClickListener?) {
    l?.let {
        toolbar.setNavigationOnClickListener { l.onIndicatorClick() }
    }
}