package com.template.mvvm.models

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.text.TextUtils

class AllBrandsViewModel : AbstractViewModel() {

    val description = ObservableField<String>()
    val snackbarMessage = MutableLiveData<String>()

    fun showClickFeedback(str: CharSequence) {
        when (!TextUtils.isEmpty(str)) {
            true -> snackbarMessage.value = str.toString()
        }
    }
}
