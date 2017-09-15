package com.template.mvvm.home

import android.app.Application
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.TextUtils
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class HomeViewModel(app: Application) : LifeViewModel(app) {
    val title = ObservableInt(R.string.home_title)
    val description = ObservableField<String>()
    val drawerSubViewModel = AppNavigationViewModel(getApplication())

    internal val snackbarMessage = SingleLiveData<String>()

    fun showClickFeedback(str: CharSequence) {
        when (!TextUtils.isEmpty(str)) {
            true -> snackbarMessage.value = str.toString()
        }
    }

    fun onIndicator() {
        drawerSubViewModel.drawerToggle.value = true
    }
}
