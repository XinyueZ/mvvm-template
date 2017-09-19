package com.template.mvvm.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.template.mvvm.R
import com.template.mvvm.ext.replaceFragmentInActivity
import com.template.mvvm.utils.SystemUiHelper

abstract class LifeActivity<out T : ViewModel> : AppCompatActivity() {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    abstract fun obtainViewModel(): ViewModel

    abstract fun createViewModel(): Class<out ViewModel>

    private fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            createViewModelView()) as LifeFragment<T>

    abstract fun createViewModelView(): LifeFragment<T>

    abstract fun getLayout(): Int

    abstract fun setViewDataBinding(binding: ViewDataBinding)

    private lateinit var uiHelper: SystemUiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        super.onCreate(savedInstanceState)
        setViewDataBinding(DataBindingUtil.setContentView(this, getLayout()))
        replaceFragmentInActivity(obtainViewModelView(), R.id.contentFrame)
    }

    protected fun hideSystemUi(length: Long = 0) {
        if (length <= 0) uiHelper.hide() else uiHelper.delayHide(length)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideSystemUi()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}