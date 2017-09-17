package com.template.mvvm.splash

import android.databinding.ViewDataBinding
import android.os.Bundle
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivitySplashBinding
import com.template.mvvm.life.LifeActivity
import com.template.mvvm.life.LifeFragment

class SplashActivity : LifeActivity() {
    override fun getLayout() = R.layout.activity_splash
    override fun createViewModel() = SplashViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            SplashFragment.newInstance(application)) as LifeFragment
    lateinit var binding: ActivitySplashBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivitySplashBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi(0)
    }
}