package com.template.mvvm.app.splash

import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivitySplashBinding
import com.template.mvvm.app.home.HomeActivity
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.showSingleTopActivity
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.core.models.splash.SplashViewModel

class SplashActivity : AppBaseActivity<SplashViewModel, ActivitySplashBinding>() {
    @LayoutRes
    override fun getLayout() = R.layout.activity_splash

    override fun requireViewModel() = SplashViewModel::class.java
    override fun createViewModelView() = SplashFragment::class.newInstance(application)
    override fun onCreate(binding: ActivitySplashBinding) {
        obtainViewModel().apply {
            startHome.setupObserve(this@SplashActivity) {
                HomeActivity::class.showSingleTopActivity(this@SplashActivity)
                this@SplashActivity.finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUi(0)
    }
}