package com.template.mvvm.models

import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.os.Handler
import android.util.Log
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.arch.SingleLiveData
import com.template.mvvm.msg.GoHome

class SplashViewModel(app: Application) : AbstractViewModel(app) {
    private val TAG = "SplashViewModel"

    val startHome = SingleLiveData<Boolean>()

    private fun goHome(msg: Message<Any>) {
        Log.d(TAG, "Gonna, msg: ${msg.getDetail()}")
        startHome.value = (true)
    }

    private fun onActorError(error: Throwable) {
        Log.d(TAG, "onActorError $error")

    }

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner): Boolean {
        Interactor.start(lifecycleOwner)
                .subscribe(GoHome::class, this::goHome)
                .subscribeError(this::onActorError)
                .register()

        Handler().postDelayed({
            Interactor.post(GoHome("Go home message as demo"))
        }, 1500)
        return true
    }
}