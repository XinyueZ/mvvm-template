package com.template.mvvm.base.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.template.mvvm.base.R
import com.template.mvvm.base.ext.android.app.replaceFragmentInActivity
import com.template.mvvm.base.utils.SystemUiHelper

abstract class LiveActivity<in B : ViewDataBinding> : AppCompatActivity() {
    private lateinit var uiHelper: SystemUiHelper

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun createLiveFragment(): LiveFragment
    protected abstract fun onCreate(binding: B)
    override fun onCreate(savedInstanceState: Bundle?) {
        uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        super.onCreate(savedInstanceState)
        onCreate(DataBindingUtil.setContentView<ViewDataBinding>(this, getLayout()) as B)
        replaceFragmentInActivity(
            supportFragmentManager.findFragmentById(R.id.contentFrame) ?: createLiveFragment(),
            R.id.contentFrame
        )
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