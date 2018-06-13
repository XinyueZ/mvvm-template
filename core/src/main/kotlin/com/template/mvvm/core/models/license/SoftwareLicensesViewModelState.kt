package com.template.mvvm.core.models.license

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.template.mvvm.core.R

class SoftwareLicensesViewModelState : BaseObservable(){

    val title = ObservableInt(R.string.software_licenses_title)
    val dataHaveNotReloaded = ObservableBoolean(true)
    //Return this view to home
    val goBack = ObservableBoolean(false)


}