package com.template.mvvm.home

import android.arch.lifecycle.LifecycleFragment
import com.template.mvvm.R
import com.template.mvvm.life.LifeActivity

class HomeActivity : LifeActivity() {
    override fun getLayout() = R.layout.activity_home
    override fun createViewModel() = HomeViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: HomeFragment.newInstance(application)) as LifecycleFragment
}