package com.template.mvvm.core.models.splash

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Handler
import com.template.mvvm.core.models.AbstractViewModel

class SplashViewModel : AbstractViewModel() {
    val startHome = MutableLiveData<Boolean>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onLifecycleStart() {
        Handler().postDelayed({
            startHome.value = true
        }, 1500)
    }
}