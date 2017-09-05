package com.template.mvvm.home

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import com.template.mvvm.R
import com.template.mvvm.ext.replaceFragmentInActivity
import com.template.mvvm.life.LifeActivity

class HomeActivity : LifeActivity() {
    override fun createViewModel(): Class<out AndroidViewModel> = HomeViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: HomeFragment.newInstance(application)) as LifecycleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragmentInActivity(obtainViewModelView(), R.id.contentFrame)
    }
}