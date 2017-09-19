package com.template.mvvm.models

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import com.template.mvvm.R
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.ext.obtainViewModel

class HomeViewModel(app: Application) : AbstractViewModel(app) {
    val title = ObservableInt(R.string.home_title)
    val description = ObservableField<String>()
    lateinit var drawerSubViewModel: AppNavigationViewModel
    val snackbarMessage = SingleLiveData<String>()

    fun showClickFeedback(str: CharSequence) {
        when (!TextUtils.isEmpty(str)) {
            true -> snackbarMessage.value = str.toString()
        }
    }

    fun onIndicator() {
        drawerSubViewModel.drawerToggle.value = true
    }

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        when (lifecycleOwner) {
            is Fragment -> {
                drawerSubViewModel = obtainViewModel(lifecycleOwner, AppNavigationViewModel::class.java)
            }
            is FragmentActivity -> {
                drawerSubViewModel = obtainViewModel(lifecycleOwner, AppNavigationViewModel::class.java)
            }
        }
        return true
    }
}
