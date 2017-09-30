package com.template.mvvm.home

import android.app.Activity
import android.content.Intent
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.AppBaseActivity
import com.template.mvvm.R
import com.template.mvvm.customtabs.CustomTabUtils
import com.template.mvvm.databinding.ActivityHomeBinding
import com.template.mvvm.ext.setup
import com.template.mvvm.models.HomeViewModel

class HomeActivity : AppBaseActivity<HomeViewModel>() {

    companion object {
        fun showInstance(cxt: Activity) {
            val intent = Intent(cxt, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, Bundle.EMPTY)
        }
    }

    override @LayoutRes
    fun getLayout() = R.layout.activity_home

    override fun requireViewModel() = HomeViewModel::class.java
    override fun createViewModelView() = HomeFragment.newInstance(application)
    lateinit var binding: ActivityHomeBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityHomeBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi(0)
        binding.apply {
            contentFrame.apply {
                vm = obtainViewModel().apply {
                    drawer.setup(this@HomeActivity, drawerToggle)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        CustomTabUtils.warmUp(this, Uri.parse(getString(R.string.internet_url)))
    }

    override fun onStop() {
        super.onStop()
        CustomTabUtils.clean(this)
    }
}

