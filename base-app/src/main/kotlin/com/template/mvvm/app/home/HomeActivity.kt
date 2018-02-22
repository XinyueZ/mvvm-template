package com.template.mvvm.app.home

import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import com.template.mvvm.app.AppBaseActivity
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityHomeBinding
import com.template.mvvm.base.customtabs.CustomTabUtils
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.android.widget.setup
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.obtainViewModel

class HomeActivity : AppBaseActivity<HomeViewModel>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_home

    override fun requireViewModel() = HomeViewModel::class.java
    override fun createViewModelView() = HomeFragment::class.newInstance(application)
    lateinit var binding: ActivityHomeBinding
    override fun setViewDataBinding(binding: ViewDataBinding) {
        this.binding = binding as ActivityHomeBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpActionBar(binding.toolbar)
        binding.vm = obtainViewModel().apply {
            with(controller.showSystemUi) {
                setupObserve(this@HomeActivity) {
                    execute({ hideSystemUi(1500) }, { showSystemUi() })
                }
                addSource(obtainViewModel(MenViewModel::class.java).showSystemUi,
                    { this.value = it })
                addSource(obtainViewModel(WomenViewModel::class.java).showSystemUi,
                    { this.value = it })
                addSource(obtainViewModel(AllGendersViewModel::class.java).showSystemUi,
                    { this.value = it })
            }
            binding.drawer.setup(this@HomeActivity, controller.drawerToggle)
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

