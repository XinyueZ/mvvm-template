package com.template.mvvm.core.models.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModelController {
    val openProduct = MutableLiveData<Boolean>()
    val openInternet = MutableLiveData<Boolean>()
    val openLicenses = MutableLiveData<Boolean>()
    val openAbout = MutableLiveData<Boolean>()
    val openItem2 = MutableLiveData<Boolean>()
    val openItem3 = MutableLiveData<Boolean>()
    val openItem4 = MutableLiveData<Boolean>()
    val openItem5 = MutableLiveData<Boolean>()

    val drawerToggle = MutableLiveData<Boolean>()

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi = MediatorLiveData<Boolean>()
}