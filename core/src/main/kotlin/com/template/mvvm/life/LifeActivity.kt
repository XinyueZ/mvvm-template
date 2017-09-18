package com.template.mvvm.life

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.template.mvvm.R
import com.template.mvvm.ext.replaceFragmentInActivity
import com.template.mvvm.utils.SystemUiHelper
import com.template.mvvm.vm.obtainViewModel

abstract class LifeActivity : AppCompatActivity() {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    fun obtainViewModel(): AndroidViewModel = obtainViewModel(createViewModel())

    abstract fun createViewModel(): Class<out AndroidViewModel>

    abstract fun obtainViewModelView(): LifeFragment

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