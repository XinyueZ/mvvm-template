package com.template.mvvm.base.ext.android.widget

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun DrawerLayout.setup(
    lifecycleOwner: LifecycleOwner,
    openTrue: LiveData<Boolean>
) {
    openTrue.observe(lifecycleOwner, Observer {
        it?.let {
            when (it) {
                true -> if (!isDrawerOpen(GravityCompat.START)) openDrawer(GravityCompat.START)
                false -> if (isDrawerOpen(GravityCompat.START)) closeDrawer(GravityCompat.START)
            }
        }
    })
}

fun DrawerLayout.isDrawerTurnOn(drawerGravity: Int) =
    isDrawerOpen(drawerGravity) && isDrawerVisible(drawerGravity)
