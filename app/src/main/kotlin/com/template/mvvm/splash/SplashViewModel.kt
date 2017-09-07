package com.template.mvvm.splash

import android.app.Application
import android.arch.lifecycle.LifecycleRegistryOwner
import android.databinding.ObservableBoolean
import android.os.Handler
import android.util.Log
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.life.LifeViewModel
import com.template.mvvm.splash.msg.GoHome

class SplashViewModel(app: Application) : LifeViewModel(app) {
    private val TAG = "SplashViewModel"

    val startHome = ObservableBoolean(false)

    private fun goHome(msg: Message<Any>) {
        Log.d(TAG, "Gonna, msg: ${msg.getDetail()}")
        startHome.set(true)
    }

    private fun onActorError(error: Throwable) {
        Log.d(TAG, "onActorError $error")

    }

    override fun registerLifecycleOwner(lifecycleRegistryOwner: LifecycleRegistryOwner): Boolean {
        Interactor.start(lifecycleRegistryOwner)
                .subscribe(GoHome::class, this::goHome)
                .subscribeError(this::onActorError)
                .register()

        Handler().postDelayed({
            Interactor.post(GoHome("Go home message as demo"))
        }, 5000)
        return true
    }
}