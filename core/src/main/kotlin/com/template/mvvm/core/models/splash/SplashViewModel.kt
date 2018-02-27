package com.template.mvvm.core.models.splash

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.template.mvvm.core.models.AbstractViewModel

class SplashViewModel : AbstractViewModel() {
    val startHome = MutableLiveData<Boolean>()

    override fun onLifecycleStart() {
        Handler().postDelayed({
            startHome.value = true
        }, 1500)
    }
}