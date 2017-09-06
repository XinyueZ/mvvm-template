package com.template.mvvm.splash

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.Handler
import com.template.mvvm.actor.Interactor
import com.template.mvvm.splash.msg.GoToHome

class SplashViewModel(app: Application) : AndroidViewModel(app) {

    fun onVmAttached() {
        Handler().postDelayed({
            Interactor.post(GoToHome())
        }, 5000)
    }
}