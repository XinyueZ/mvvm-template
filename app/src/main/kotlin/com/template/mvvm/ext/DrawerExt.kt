package com.template.mvvm.ext

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.template.mvvm.life.SingleLiveData

fun DrawerLayout.setup(lifecycleOwner: LifecycleOwner,
                       openTrue: SingleLiveData<Boolean>) {
    openTrue.observe(lifecycleOwner, Observer {
        it?.let {
            when (it) {
                true -> if (!isDrawerOpen(GravityCompat.START)) openDrawer(GravityCompat.START)
                false -> if (isDrawerOpen(GravityCompat.START)) closeDrawer(GravityCompat.START)
            }
        }
    })
}
