package com.template.mvvm.home

import android.app.Application
import android.databinding.ObservableBoolean
import com.template.mvvm.R
import com.template.mvvm.actor.Interactor
import com.template.mvvm.common.OnCommandListener
import com.template.mvvm.home.msg.OpenAbout
import com.template.mvvm.home.msg.OpenInternet
import com.template.mvvm.home.msg.OpenItem
import com.template.mvvm.home.msg.OpenProducts
import com.template.mvvm.life.LifeViewModel

class NaviSubViewModel(context: Application) : LifeViewModel(context), OnCommandListener {
    var drawerToggle = ObservableBoolean(false)

    internal fun toggleDrawer() {
        drawerToggle.set(true)
        drawerToggle.notifyChange()
    }

    override fun onCommand(id: Int) {
        when (id) {
            R.id.action_products -> {
                Interactor.post(OpenProducts("Open product-list"))
                toggleDrawer()
            }
            R.id.action_about -> {
                Interactor.post(OpenAbout("Open about"))
                toggleDrawer()
            }
            R.id.action_internet -> {
                Interactor.post(OpenInternet("Open internet"))
                toggleDrawer()
            }
            R.id.action_1 -> Interactor.post(OpenItem(1))
            R.id.action_2 -> Interactor.post(OpenItem(2))
            R.id.action_3 -> Interactor.post(OpenItem(3))
        }
    }
}