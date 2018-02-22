package com.template.mvvm.app.home

import android.net.Uri
import android.support.annotation.LayoutRes
import com.template.mvvm.app.R
import com.template.mvvm.app.databinding.ActivityHomeBinding
import com.template.mvvm.base.customtabs.CustomTabUtils
import com.template.mvvm.base.ext.android.app.newInstance
import com.template.mvvm.base.ext.android.app.setUpActionBar
import com.template.mvvm.base.ext.android.arch.lifecycle.setupObserve
import com.template.mvvm.base.ext.android.widget.setup
import com.template.mvvm.base.ext.lang.execute
import com.template.mvvm.base.ui.LiveActivity
import com.template.mvvm.core.generateViewModel
import com.template.mvvm.core.models.home.HomeViewModel
import com.template.mvvm.core.models.product.AllGendersViewModel
import com.template.mvvm.core.models.product.MenViewModel
import com.template.mvvm.core.models.product.WomenViewModel
import com.template.mvvm.core.obtainViewModel

class HomeActivity : LiveActivity<HomeViewModel, ActivityHomeBinding>() {

    @LayoutRes
    override fun getLayout() = R.layout.activity_home

    override fun createViewModelView() = HomeFragment::class.newInstance(application)
    override fun onCreate(binding: ActivityHomeBinding) {
        setUpActionBar(binding.toolbar)
        HomeViewModel::class.generateViewModel(this) {
            binding.vm = this
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

