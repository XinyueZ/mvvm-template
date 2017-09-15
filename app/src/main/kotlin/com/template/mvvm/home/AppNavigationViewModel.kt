package com.template.mvvm.home

import android.app.Application
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class AppNavigationViewModel(context: Application) : LifeViewModel(context) {
    internal val openProduct = SingleLiveData<Boolean>()
    internal val openLicenses = SingleLiveData<Boolean>()
    internal val openAbout = SingleLiveData<Boolean>()
    internal val openItem1 = SingleLiveData<Boolean>()
    internal val openItem2 = SingleLiveData<Boolean>()
    internal val openItem3 = SingleLiveData<Boolean>()

    internal val drawerToggle = SingleLiveData<Boolean>()

    fun onCommand(id: Int) {
        when (id) {
            R.id.action_products -> {

                //Use the actor to info activity to open next stage.
                //Interactor.post(OpenProducts("Open product-list"))

                //Use LiveData to open next stage.
                drawerToggle.value = false
                openProduct.value = true
            }
            R.id.action_software_licenses -> {

                drawerToggle.value = false
                openLicenses.value = true
            }
            R.id.action_about -> {

                //Use the actor to info activity to open next stage.
                //Interactor.post(OpenAbout("Open about"))

                //Use LiveData to open next stage.
                drawerToggle.value = false
                openAbout.value = true
            }
            R.id.action_1 -> {

                //Use the actor to info activity to open next stage.
                // Interactor.post(OpenItem(1))

                //Use LiveData to open next stage.
                openItem1.value = true
            }
            R.id.action_2 -> {

                //Use the actor to info activity to open next stage.
                // Interactor.post(OpenItem(2))

                //Use LiveData to open next stage.
                openItem2.value = true
            }
            R.id.action_3 -> {

                //Use the actor to info activity to open next stage.
                //Interactor.post(OpenItem(3))

                //Use LiveData to open next stage.
                openItem3.value = true
            }
        }
    }
}