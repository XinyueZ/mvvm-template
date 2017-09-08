package com.template.mvvm.home

import android.app.Application
import android.databinding.ObservableBoolean
import com.template.mvvm.life.LifeViewModel

class NaviSubViewModel(context: Application) : LifeViewModel(context), OnCommandListener {
    var drawerToggle = ObservableBoolean(false)

    internal fun toggleDrawer() {
        drawerToggle.set(true)
        drawerToggle.notifyChange()
    }

    override fun onCommand(id: Int) {

    }

}