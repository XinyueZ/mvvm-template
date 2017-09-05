package com.template.mvvn.life

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity
import com.example.android.architecture.blueprints.todoapp.util.obtainViewModel

abstract class LifeActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    internal fun obtainViewModel(): AndroidViewModel = obtainViewModel(createViewModel())

    abstract fun createViewModel(): Class<out AndroidViewModel>

    abstract fun obtainViewModelView(): LifecycleFragment
}