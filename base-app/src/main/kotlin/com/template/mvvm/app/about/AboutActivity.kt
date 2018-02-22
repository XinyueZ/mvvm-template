package com.template.mvvm.app.about

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityAboutBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.core.models.about.AboutViewModel

class AboutActivity : AppBaseActivity<AboutViewModel>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_about

    override fun requireViewModel() = AboutViewModel::class.java
    override fun createViewModelView() = AboutFragment::class.newInstance(application)
    lateinit var binding: ActivityAboutBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityAboutBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi(0)
        binding.vm = obtainViewModel()
    }
}