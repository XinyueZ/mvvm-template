package com.template.mvvm.core.models.splash

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.template.mvvm.core.arch.AbstractViewModel

class SplashViewModel : AbstractViewModel() {
    val startHome = MutableLiveData<Boolean>()

    override fun onLifecycleStart() {
        Handler().postDelayed({
            startHome.value = true
        }, 1500)
    }
}