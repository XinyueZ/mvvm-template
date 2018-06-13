package com.template.mvvm.core.models.license

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.template.mvvm.base.ext.android.arch.lifecycle.SingleLiveData
import com.template.mvvm.repository.domain.licenses.LibraryList

class SoftwareLicensesViewModelController : BaseObservable() {

    //Data of this view-model
    var libraryListSource: LibraryList? = null
    //For recyclerview data
    var libraryItemVmList: MutableLiveData<List<ViewModel>> =
        SingleLiveData()
    // True toggle the system-ui(navi-bar, status-bar etc.)
    val showSystemUi: MutableLiveData<Boolean> =
        SingleLiveData()
    val licenseDetailViewModel = MutableLiveData<LicenseDetailViewModel>()

}