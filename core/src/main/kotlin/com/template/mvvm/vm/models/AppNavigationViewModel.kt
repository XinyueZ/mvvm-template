package com.template.mvvm.vm.models

import android.app.Application
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class AppNavigationViewModel(context: Application) : LifeViewModel(context) {
    val openProduct = SingleLiveData<Boolean>()
    val openInternet = SingleLiveData<Boolean>()
    val openLicenses = SingleLiveData<Boolean>()
    val openAbout = SingleLiveData<Boolean>()
    val openItem1 = SingleLiveData<Boolean>()
    val openItem2 = SingleLiveData<Boolean>()
    val openItem3 = SingleLiveData<Boolean>()

    val drawerToggle = SingleLiveData<Boolean>()

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