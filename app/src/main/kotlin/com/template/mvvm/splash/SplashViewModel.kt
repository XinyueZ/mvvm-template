package com.template.mvvm.splash

import android.app.Application
import android.databinding.ObservableBoolean
import android.os.Handler
import android.util.Log
import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.splash.msg.GoToHome

class SplashViewModel(app: Application) : LifeViewModel(app) {
    private val TAG = "SplashViewModel"

    val startHome = ObservableBoolean(false)

    private fun goHome(msg: Message<Detail>) {
        startHome.set(true)
    }

    private fun onActorError(error: Throwable) {
        Log.d(TAG, "onActorError $error")
    }

    fun onVmAttached() {
        Interactor.start(this)
                .subscribe(GoToHome::class, this::goHome)
                .subscribeError(this::onActorError)
                .register()

        Handler().postDelayed({
            Interactor.post(GoToHome())
        }, 5000)
    }
}