package com.template.mvvm.app.home

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityHomeBinding
import com.template.mvvm.base.customtabs.CustomTabUtils
import com.template.mvvm.base.ext.setup
import com.template.mvvm.core.ext.obtainViewModel
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.WomenViewModel

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
        binding.apply {
            contentFrame.apply {
                vm = obtainViewModel().apply {
                    with(controller.showSystemUi) {
                        observe(
                                this@HomeActivity,
                                Observer {
                                    when (it) {
                                        true -> hideSystemUi(1500)
                                        false -> showSystemUi()
                                    }
                                }
                        )
                        addSource(obtainViewModel(MenViewModel::class.java).showSystemUi,
                                { this.value = it })
                        addSource(obtainViewModel(WomenViewModel::class.java).showSystemUi,
                                { this.value = it })
                        addSource(obtainViewModel(AllGendersViewModel::class.java).showSystemUi,
                                { this.value = it })
                    }
                    drawer.setup(this@HomeActivity, controller.drawerToggle)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        hideSystemUi(0)
        CustomTabUtils.warmUp(this, Uri.parse(getString(R.string.internet_url)))
    }

    override fun onStop() {
        super.onStop()
        CustomTabUtils.clean(this)
    }
}

