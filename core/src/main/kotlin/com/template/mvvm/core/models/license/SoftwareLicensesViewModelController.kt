package com.template.mvvm.core.models.license

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.BaseObservable
import com.template.mvvm.core.arch.SingleLiveData
import com.template.mvvm.repository.domain.licenses.LibraryList

class SoftwareLicensesViewModelController : BaseObservable() {

    //Data of this view-model
    var libraryListSource: LibraryList? = null
    //For recyclerview data
    var libraryItemVmList: MutableLiveData<List<ViewModel>> = SingleLiveData()
    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> = SingleLiveData()
    val licenseDetailViewModel = MutableLiveData<LicenseDetailViewModel>()

}