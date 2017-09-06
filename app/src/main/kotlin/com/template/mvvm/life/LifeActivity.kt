package com.template.mvvm.life

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.template.mvvm.R
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ext.replaceFragmentInActivity

abstract class LifeActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    internal fun obtainViewModel(): AndroidViewModel = obtainViewModel(createViewModel())

    abstract fun createViewModel(): Class<out AndroidViewModel>

    abstract fun obtainViewModelView(): LifecycleFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragmentInActivity(obtainViewModelView(), R.id.contentFrame)
    }
}