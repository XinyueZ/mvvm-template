package com.template.mvvm.home

import android.databinding.BindingAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar

interface OnIndicatorClickListener {
    fun onIndicatorClick()
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

@BindingAdapter("onIndicatorClick")
fun toolbarOnIndicatorClick(toolbar: Toolbar, l: OnIndicatorClickListener?) {
    l?.let {
        toolbar.setNavigationOnClickListener { l.onIndicatorClick() }
    }
}