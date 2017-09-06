package com.template.mvvm.splash

import android.arch.lifecycle.LifecycleFragment
import com.template.mvvm.R
import com.template.mvvm.life.LifeActivity

class SplashActivity : LifeActivity() {
    override fun createViewModel() = SplashViewModel::class.java

    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SplashFragment.newInstance(application)) as LifecycleFragment
}