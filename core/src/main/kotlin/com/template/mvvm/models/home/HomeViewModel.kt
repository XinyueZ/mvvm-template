package com.template.mvvm.models.home

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableInt
import com.template.mvvm.R
import com.template.mvvm.models.AbstractViewModel

class HomeViewModel : AbstractViewModel() {
    val title = ObservableInt(R.string.home_title)

    val openProduct = MutableLiveData<Boolean>()
    val openInternet = MutableLiveData<Boolean>()
    val openLicenses = MutableLiveData<Boolean>()
    val openAbout = MutableLiveData<Boolean>()
    val openItem2 = MutableLiveData<Boolean>()
    val openItem3 = MutableLiveData<Boolean>()
    val openItem4 = MutableLiveData<Boolean>()

    val drawerToggle = MutableLiveData<Boolean>()

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi = MediatorLiveData<Boolean>()

    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onCommand(id: Int) {
        when (id) {
            R.id.action_app_bar_indicator -> drawerToggle.value = true
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
            R.id.action_men -> openItem2.value = true
            R.id.action_women -> openItem3.value = true
            R.id.action_all_genders -> openItem4.value = true
        }
    }
    //-----------------------------------
}