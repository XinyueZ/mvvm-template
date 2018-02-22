package com.template.mvvm.app.about

import android.support.annotation.LayoutRes
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityAboutBinding
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ui.LiveActivity
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.about.AboutViewModel

class AboutActivity : LiveActivity<AboutViewModel, ActivityAboutBinding>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_about

    override fun createViewModelView() = AboutFragment::class.newInstance(application)
    override fun onCreate(binding: ActivityAboutBinding) {
        hideSystemUi(0)
        AboutViewModel::class.generateViewModel(this) {
            binding.vm = this
        }
    }
}