package com.template.mvvm.app.splash

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivitySplashBinding
import com.template.mvvm.app.home.HomeActivity
import com.template.mvvm.core.models.splash.SplashViewModel

class SplashActivity : AppBaseActivity<SplashViewModel>() {
    override @LayoutRes
    fun getLayout() = R.layout.activity_splash

    override fun requireViewModel() = SplashViewModel::class.java
    override fun createViewModelView() = SplashFragment.newInstance(application)
    lateinit var binding: ActivitySplashBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySplashBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtainViewModel().apply {
            startHome.observe(this@SplashActivity, Observer {
                HomeActivity.showInstance(this@SplashActivity)
                this@SplashActivity.finish()
            })
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUi(0)
    }
}