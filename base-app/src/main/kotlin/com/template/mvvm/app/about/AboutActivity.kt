package com.template.mvvm.app.about

import android.app.Activity
import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityAboutBinding
import com.template.mvvm.core.models.about.AboutViewModel

class AboutActivity : AppBaseActivity<AboutViewModel>() {

    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, AboutActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    @LayoutRes
    override fun getLayout() = R.layout.activity_about
    override fun requireViewModel() = AboutViewModel::class.java
    override fun createViewModelView() = AboutFragment.newInstance(application)
    lateinit var binding: ActivityAboutBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityAboutBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi(0)
        binding.apply {
            vm = obtainViewModel()
        }
    }
}