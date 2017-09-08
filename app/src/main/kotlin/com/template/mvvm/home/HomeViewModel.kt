package com.template.mvvm.home

import android.app.Application
import android.databinding.ObservableField
import com.template.mvvm.App
import com.template.mvvm.R
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class HomeViewModel(app: Application) : LifeViewModel(app) {
    val title = ObservableField<String>("Home")
    val description = ObservableField<String>("The home of this application.")
    val drawerSubViewModel = NaviSubViewModel(getApplication())

    internal val snackbarMessage = SingleLiveData<String>()

    fun showClickFeedback() {
        snackbarMessage.value = getApplication<App>().getString(R.string.greeting)
    }

    fun toggleDrawer() {
        drawerSubViewModel.toggleDrawer()
    }
}
