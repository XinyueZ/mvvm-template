package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.TextUtils
import com.template.mvvm.Injection
import com.template.mvvm.R
import com.template.mvvm.ViewModelFactory
import com.template.mvvm.ext.obtainViewModel

class HomeViewModel : AbstractViewModel() {
    val title = ObservableInt(R.string.home_title)
    val description = ObservableField<String>()
    lateinit var drawerSubViewModel: AppNavigationViewModel
    val snackbarMessage = MutableLiveData<String>()

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

    override fun onCleared() {
        super.onCleared()
        ViewModelFactory.destroyInstance()
        Injection.destroyInstance()
    }
}
