package com.template.mvvm.models

import android.arch.lifecycle.MutableLiveData
import com.template.mvvm.R

class AppNavigationViewModel : AbstractViewModel() {
    val openProduct = MutableLiveData<Boolean>()
    val openInternet = MutableLiveData<Boolean>()
    val openLicenses = MutableLiveData<Boolean>()
    val openAbout = MutableLiveData<Boolean>()
    val openItem1 = MutableLiveData<Boolean>()
    val openItem2 = MutableLiveData<Boolean>()
    val openItem3 = MutableLiveData<Boolean>()

    val drawerToggle = MutableLiveData<Boolean>()

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