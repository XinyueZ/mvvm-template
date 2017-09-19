package com.template.mvvm.splash

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivitySplashBinding
import com.template.mvvm.home.HomeActivity
import com.template.mvvm.vm.models.SplashViewModel

class SplashActivity : AppBaseActivity<SplashViewModel>() {
    override fun getLayout() = R.layout.activity_splash
    override fun createViewModel() = SplashViewModel::class.java
    override fun createViewModelView() = SplashFragment.newInstance(application)
    lateinit var binding: ActivitySplashBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySplashBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi(0)
        with((obtainViewModel() as SplashViewModel)) {
            startHome.observe(this@SplashActivity, Observer {
                HomeActivity.showInstance(this@SplashActivity)
                this@SplashActivity.finish()
            })
        }
    }
}