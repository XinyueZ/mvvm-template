package com.template.mvvm.splash

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivitySplashBinding
import com.template.mvvm.home.HomeActivity
import com.template.mvvm.models.SplashViewModel

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
        hideSystemUi(0)
        obtainViewModel().apply {
            startHome.observe(this@SplashActivity, Observer {
                HomeActivity.showInstance(this@SplashActivity)
                this@SplashActivity.finish()
            })
        }
    }
}