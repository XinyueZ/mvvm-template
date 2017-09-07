package com.template.mvvm.home

import android.app.Application
import android.databinding.ObservableBoolean
import com.template.mvvm.life.LifeViewModel

class DrawerSubViewModel(context: Application) : LifeViewModel(context) {
    var drawerToggle =  ObservableBoolean(false)

    internal fun toggleDrawer() {
        drawerToggle.set(true)
        drawerToggle.notifyChange()
    }
}