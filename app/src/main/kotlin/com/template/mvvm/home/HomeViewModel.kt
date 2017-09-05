package com.template.mvvm.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.template.mvvm.App
import com.template.mvvm.R
import com.template.mvvm.life.SingleLiveData

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    val title = ObservableField<String>("Home")
    val description = ObservableField<String>("The home of this application.")
    internal val snackbarMessage = SingleLiveData<String>()

    fun showClickFeedback() {
        snackbarMessage.value = getApplication<App>().getString(R.string.greeting)
    }
}
