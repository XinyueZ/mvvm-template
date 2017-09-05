package com.template.mvvn.home

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import com.example.android.architecture.blueprints.todoapp.util.replaceFragmentInActivity
import com.template.mvvn.R
import com.template.mvvn.life.LifeActivity

class HomeActivity : LifeActivity() {
    override fun createViewModel(): Class<out AndroidViewModel> = HomeViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: HomeFragment.newInstance(application)) as LifecycleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragmentInActivity(obtainViewModelView(), R.id.contentFrame)
    }
}