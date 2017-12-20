package com.template.mvvm.models.home

import com.template.mvvm.R
import com.template.mvvm.models.AbstractViewModel

class HomeViewModel : AbstractViewModel() {
    val state = HomeViewModelState()
    val controller = HomeViewModelController()

    //-----------------------------------
    //BindingAdapter handler
    //-----------------------------------
    fun onCommand(id: Int) {
        with(controller) {
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
    }
    //-----------------------------------
}