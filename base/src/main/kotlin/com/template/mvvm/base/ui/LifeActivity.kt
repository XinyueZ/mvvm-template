package com.template.mvvm.base.ui

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.template.mvvm.base.R
import com.template.mvvm.base.ext.replaceFragmentInActivity
import com.template.mvvm.base.utils.SystemUiHelper

abstract class LifeActivity<out T : ViewModel> : AppCompatActivity() {
    private lateinit var uiHelper: SystemUiHelper

    protected abstract fun obtainViewModel(): T
    protected abstract fun requireViewModel(): Class<out T>
    protected abstract fun createViewModelView(): LifeFragment<T>
    protected abstract @LayoutRes fun getLayout(): Int
    protected abstract fun setViewDataBinding(binding: ViewDataBinding)
    private fun obtainViewModelView() = supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            createViewModelView()

    override fun onCreate(savedInstanceState: Bundle?) {
        uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        super.onCreate(savedInstanceState)
        (DataBindingUtil.setContentView(this, getLayout()) as? ViewDataBinding) ?.run {
            setViewDataBinding(this)
            replaceFragmentInActivity(obtainViewModelView(), R.id.contentFrame)
        }
    }

    protected fun hideSystemUi(length: Long = 0) {
        if (length <= 0) uiHelper.hide() else uiHelper.delayHide(length)
    }

    protected fun showSystemUi() {
        uiHelper.show()
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