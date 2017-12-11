package com.template.mvvm.models.splash

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.template.mvvm.models.AbstractViewModel

class SplashViewModel : AbstractViewModel() {
    private val TAG = "SplashViewModel"

    val startHome = MutableLiveData<Boolean>()

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        Handler().postDelayed({
            startHome.value = true
        }, 1500)
        return true
    }
}