package com.template.mvvm.home

import android.app.Application
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class AppNavigationViewModel(context: Application) : LifeViewModel(context) {
    internal val openProduct = SingleLiveData<Boolean>()
    internal val openInternet = SingleLiveData<Boolean>()
    internal val openLicenses = SingleLiveData<Boolean>()
    internal val openAbout = SingleLiveData<Boolean>()
    internal val openItem1 = SingleLiveData<Boolean>()
    internal val openItem2 = SingleLiveData<Boolean>()
    internal val openItem3 = SingleLiveData<Boolean>()

    internal val drawerToggle = SingleLiveData<Boolean>()

    fun onCommand(id: Int) {
        when (id) {
            R.id.action_products -> {
                drawerToggle.value = false
                openProduct.value = true
            }
            R.id.action_internet -> {
                drawerToggle.value = false
                openInternet.value = true
            }
            R.id.action_software_licenses -> {

                drawerToggle.value = false
                openLicenses.value = true
            }
            R.id.action_about -> {
                drawerToggle.value = false
                openAbout.value = true
            }
            R.id.action_1 -> openItem1.value = true
            R.id.action_2 -> openItem2.value = true
            R.id.action_3 -> openItem3.value = true
        }
    }
}