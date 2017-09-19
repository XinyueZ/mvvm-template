package com.template.mvvm.models

import android.arch.lifecycle.LifecycleOwner
import android.os.Handler
import com.template.mvvm.arch.SingleLiveData

class SplashViewModel : AbstractViewModel() {
    private val TAG = "SplashViewModel"

    val startHome = SingleLiveData<Boolean>()




    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        Handler().postDelayed({
            startHome.value = (true)
        }, 1500)
        return true
    }
}