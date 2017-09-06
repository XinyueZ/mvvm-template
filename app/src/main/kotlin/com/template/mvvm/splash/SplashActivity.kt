package com.template.mvvm.splash

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.widget.Toast
import com.template.mvvm.R
import com.template.mvvm.actor.Detail
import com.template.mvvm.actor.Interactor
import com.template.mvvm.actor.Message
import com.template.mvvm.home.HomeActivity
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.splash.msg.GoToHome

class SplashActivity : LifeActivity() {
    override fun createViewModel() = SplashViewModel::class.java

    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SplashFragment.newInstance(application)) as LifecycleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Interactor.start(this)
                .subscribe(GoToHome::class, this::goHome)
                .subscribeError(this::onActorError)
                .register()
    }

    private fun goHome(msg: Message<Detail>) {
        HomeActivity.showInstance(this)
        finish()
    }

    private fun onActorError(error: Throwable) {
        Toast.makeText(this, String.format("Error: %s", error.message), Toast.LENGTH_LONG)
                .show()
    }
}