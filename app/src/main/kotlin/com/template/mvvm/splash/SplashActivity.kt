package com.template.mvvm.splash

import android.arch.lifecycle.LifecycleFragment
import android.databinding.ViewDataBinding
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivitySplashBinding
import com.template.mvvm.life.LifeActivity

class SplashActivity : LifeActivity() {
    override fun getLayout() = R.layout.activity_splash
    override fun createViewModel() = SplashViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SplashFragment.newInstance(application)) as LifecycleFragment
    lateinit var binding: ActivitySplashBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySplashBinding
    }
}