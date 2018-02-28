package com.template.mvvm.core.models.license

import android.databinding.BaseObservable
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import com.template.mvvm.core.R

class SoftwareLicensesViewModelState : BaseObservable(){

    val title = ObservableInt(R.string.software_licenses_title)
    val dataLoaded = ObservableBoolean(false)
    val dataHaveNotReloaded = ObservableBoolean(true)
    //Return this view to home
    val goBack = ObservableBoolean(false)


}