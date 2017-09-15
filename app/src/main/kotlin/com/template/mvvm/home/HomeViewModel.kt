package com.template.mvvm.home

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import com.template.mvvm.R
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.life.SingleLiveData

class HomeViewModel(app: Application) : LifeViewModel(app) {
    val title = ObservableInt(R.string.home_title)
    val description = ObservableField<String>()
    lateinit var drawerSubViewModel: AppNavigationViewModel
    internal val snackbarMessage = SingleLiveData<String>()

    fun showClickFeedback(str: CharSequence) {
        when (!TextUtils.isEmpty(str)) {
            true -> snackbarMessage.value = str.toString()
        }
    }

    fun onIndicator() {
        drawerSubViewModel.drawerToggle.value = true
    }

    override fun registerLifecycleOwner(lifecycleRegistryOwner: LifecycleRegistryOwner): Boolean {
        when (lifecycleRegistryOwner) {
            is Fragment -> {
                drawerSubViewModel = this@HomeViewModel.obtainViewModel(lifecycleRegistryOwner, AppNavigationViewModel::class.java)
            }
            is FragmentActivity -> {
                drawerSubViewModel = this@HomeViewModel.obtainViewModel(lifecycleRegistryOwner, AppNavigationViewModel::class.java)
            }
        }
        return true
    }
}
