package com.template.mvvm.core.models.home

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.template.mvvm.core.arch.SingleLiveData

class HomeViewModelController {
    val openProduct = MutableLiveData<Boolean>()
    val openInternet = MutableLiveData<Boolean>()
    val openLicenses = MutableLiveData<Boolean>()
    val openAbout = MutableLiveData<Boolean>()
    val openItem2 = MutableLiveData<Boolean>()
    val openItem3 = MutableLiveData<Boolean>()
    val openItem4 = MutableLiveData<Boolean>()
    val defaultSelection = SingleLiveData<Int>()

    val drawerToggle = MutableLiveData<Boolean>()

    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi = MediatorLiveData<Boolean>()
}