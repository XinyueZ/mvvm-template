package com.template.mvvm.splash

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import com.template.mvvm.R
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.utils.SystemUiHelper

class SplashActivity : LifeActivity() {

    override fun createViewModel() = SplashViewModel::class.java

    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SplashFragment.newInstance(application)) as LifecycleFragment

    private lateinit var uiHelper: SystemUiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.delayHide(1000)
        super.onCreate(savedInstanceState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        uiHelper.hide()
        super.onWindowFocusChanged(hasFocus)
    }
}