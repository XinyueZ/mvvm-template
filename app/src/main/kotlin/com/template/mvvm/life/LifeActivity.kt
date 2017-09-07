package com.template.mvvm.life

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.template.mvvm.R
import com.template.mvvm.ext.obtainViewModel
import com.template.mvvm.ext.replaceFragmentInActivity
import com.template.mvvm.utils.SystemUiHelper

abstract class LifeActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val registry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = registry

    internal fun obtainViewModel(): AndroidViewModel = obtainViewModel(createViewModel())

    abstract fun createViewModel(): Class<out AndroidViewModel>

    abstract fun obtainViewModelView(): LifecycleFragment

    abstract fun getLayout(): Int

    private lateinit var uiHelper: SystemUiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        uiHelper = SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0)
        uiHelper.hide()
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        replaceFragmentInActivity(obtainViewModelView(), R.id.contentFrame)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        uiHelper.hide()
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> uiHelper.hide()
        }
        return super.onTouchEvent(event)
    }
}