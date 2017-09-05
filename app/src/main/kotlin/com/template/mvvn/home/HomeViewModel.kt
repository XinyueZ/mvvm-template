package com.template.mvvn.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField

class HomeViewModel(app: Application) : AndroidViewModel(app) {
    val title = ObservableField<String>("Home")
    val description = ObservableField<String>("The home of this application.")

}
