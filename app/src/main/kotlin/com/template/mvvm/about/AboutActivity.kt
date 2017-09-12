package com.template.mvvm.about

import android.app.Activity
import android.arch.lifecycle.LifecycleFragment
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.template.mvvm.R
import com.template.mvvm.databinding.ActivityAboutBinding
import com.template.mvvm.life.LifeActivity

class AboutActivity : LifeActivity() {

    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, AboutActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override fun getLayout() = R.layout.activity_about
    override fun createViewModel() = AboutViewModel::class.java
    override fun obtainViewModelView() = (supportFragmentManager.findFragmentById(R.id.contentFrame) ?: AboutFragment.newInstance(application)) as LifecycleFragment

    lateinit var binding: ActivityAboutBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityAboutBinding
    }



}