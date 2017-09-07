package com.template.mvvm.splash

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import com.template.mvvm.R
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.utils.SystemUiHelper

class SplashActivity : LifeActivity() {
    override fun getLayout() = R.layout.activity_splash
    override fun createViewModel() = SplashViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SplashFragment.newInstance(application)) as LifecycleFragment
}